package com.mygdx.game.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack
import com.mygdx.game.main.Main
import com.mygdx.game.object_map.MapObject
import com.mygdx.game.unit.ClassUnit
import com.mygdx.game.unit.Unit

// Top-right minimap: whole map terrain baked into one small texture once
// per map load, team/enemy dots on top, and a fog-of-war overlay that's
// only "lifted" in a zoom-dependent area around the local player and
// their living teammates.
//
// Orientation note (this was wrong once already - written out so it isn't
// wrong again): a Pixmap is authored row-0-at-top like any normal image,
// but SpriteBatch.draw() of a whole Texture built from one already shows
// it "right side up" (matching how a loaded PNG looks) - so pixmap row 0
// ends up at the TOP of the screen quad. The game's own world has +Y
// meaning "further up/north" on screen (see RenderCenter.render_objZoom -
// a direct multiply, no flip). So the LARGEST world Y (block row
// height-1) needs to land at pixmap row 0 to end up at screen-top too -
// i.e. baking must store block row y at pixmap row (height-1-y), a
// vertical flip. Getting this backwards is exactly what made the whole
// map read as mirrored/rotated relative to actual driving direction.
//
// Honest limitation: a true circular reveal needs a shader (radial alpha
// mask) - this clips (via ScissorStack, in plain screen pixels, no
// texture-space math involved at all) to a SQUARE region around each
// visible unit instead. Reads as "revealed area around you", square-edged
// rather than a round spotlight.
object Minimap {
    private const val PANEL_SIZE = 220f
    private const val MARGIN_TOP = 18f
    private const val MARGIN_RIGHT = 18f
    private const val BORDER_OUTER = 10f
    private const val BORDER_INNER = 4f
    // world-block radius revealed at Zoom == 1; scales inversely with Zoom
    // the same way the camera's own visible width does (RC.WidthRenderZoom
    // = RC.WidthRender / Zoom) - zoomed out sees more, so the minimap
    // reveals more too
    private const val BASE_VISIBILITY_RADIUS_BLOCKS = 30f
    private const val DOT_RADIUS = 3.2f
    // spawn zone markers - bigger than a unit dot so a cluster of spawn
    // points still reads as "a zone", not more team dots
    private const val SPAWN_MARKER_RADIUS = 5f

    private var terrainTexture: Texture? = null
    private var bakedWidth = -1
    private var bakedHeight = -1

    private val fogColor = Color(0.05f, 0.05f, 0.05f, 0.72f)
    private val playerColor = Color(0.95f, 0.85f, 0.15f, 1f)
    private val allyColor = Color(0.25f, 0.65f, 0.95f, 1f)
    private val enemyColor = Color(0.9f, 0.2f, 0.2f, 1f)
    private val playerSpawnMarkerColor = Color(0.3f, 0.9f, 0.4f, 0.8f)
    private val enemySpawnMarkerColor = Color(0.9f, 0.35f, 0.15f, 0.8f)
    private val unpack = Color()

    private fun ensureTerrainTexture(width: Int, height: Int) {
        if (width <= 0 || height <= 0) return
        if (terrainTexture != null && bakedWidth == width && bakedHeight == height) return
        terrainTexture?.dispose()

        val pixmap = Pixmap(width, height, Pixmap.Format.RGBA8888)
        for (y in 0 until height) {
            for (x in 0 until width) {
                val block = Main.BlockList2D[y][x]
                if (block.hasTerrainPaint) {
                    Color.abgr8888ToColor(unpack, block.terrainColorBL)
                } else {
                    unpack.set(0.3f, 0.42f, 0.23f, 1f)
                }
                pixmap.setColor(unpack)
                // vertical flip on purpose - see class comment
                pixmap.drawPixel(x, height-1-y)
            }
        }
        terrainTexture = Texture(pixmap)
        pixmap.dispose()
        bakedWidth = width
        bakedHeight = height
    }

    fun render() {
        val unit = Main.RC?.MainUnit ?: return
        val width = Main.xMap
        val height = Main.yMap
        ensureTerrainTexture(width, height)
        val texture = terrainTexture ?: return
        val blockSize = Main.width_block.toFloat()

        val panelX = Gdx.graphics.width - PANEL_SIZE - MARGIN_RIGHT
        val panelY = Gdx.graphics.height - PANEL_SIZE - MARGIN_TOP

        // living teammates (including self) - each one lifts the fog
        // around itself; enemies only ever show up if inside that same
        // revealed area
        val visibilityRadiusBlocks = BASE_VISIBILITY_RADIUS_BLOCKS / Main.Zoom.coerceAtLeast(0.1f)
        val teammates = ArrayList<Unit>()
        val enemies = ArrayList<Unit>()
        Main.R_LOCK.lock()
        try {
            for (u in Main.UnitList) {
                if (u.classUnit != ClassUnit.Transport) continue
                if (u.team == unit.team) teammates.add(u) else enemies.add(u)
            }
        } finally {
            Main.R_LOCK.unlock()
        }

        // world (x,y) -> a point in screen space within the panel rect.
        // Direct on both axes - the game's own +X/+Y already point the same
        // way as screen +X/+Y (see RenderCenter.render_objZoom), and this
        // matches the vertical flip baked into the terrain texture above.
        fun toPanel(worldX: Float, worldY: Float): FloatArray {
            val bx = worldX/blockSize/width
            val by = worldY/blockSize/height
            return floatArrayOf(panelX + bx*PANEL_SIZE, panelY + by*PANEL_SIZE)
        }

        Main.Render.polyBatch.shader = null
        Main.Render.polyBatch.begin()
        // frame - a couple of nested rects instead of a plain single
        // outline, in the same "armor plating" spirit as the rest of the UI
        Main.Render.rect(panelX-BORDER_OUTER, panelY-BORDER_OUTER, PANEL_SIZE+BORDER_OUTER*2, PANEL_SIZE+BORDER_OUTER*2, Color(0.12f, 0.11f, 0.09f, 1f))
        Main.Render.rect(panelX-BORDER_INNER, panelY-BORDER_INNER, PANEL_SIZE+BORDER_INNER*2, PANEL_SIZE+BORDER_INNER*2, Color(0.55f, 0.5f, 0.4f, 1f))
        Main.Render.polyBatch.end()

        Main.Batch.shader = null
        Main.Batch.begin()
        Main.Batch.setColor(Color.WHITE)
        Main.Batch.draw(texture, panelX, panelY, PANEL_SIZE, PANEL_SIZE)
        Main.Batch.end()

        Main.Render.polyBatch.shader = null
        Main.Render.polyBatch.begin()
        Main.Render.rect(panelX, panelY, PANEL_SIZE, PANEL_SIZE, fogColor)
        Main.Render.polyBatch.end()

        // "punch" the fog: clip to a square around every living teammate
        // (self included) and redraw the SAME whole-texture draw again -
        // clipped by ScissorStack in plain screen pixels, so there's no
        // texture-space math (and no way to get a flip wrong) here at all
        val revealSizePanel = (visibilityRadiusBlocks*2f/width)*PANEL_SIZE
        for (mate in teammates) {
            val p = toPanel(mate.x, mate.y)
            val rx = (p[0]-revealSizePanel/2f).coerceIn(panelX, panelX+PANEL_SIZE)
            val ry = (p[1]-revealSizePanel/2f).coerceIn(panelY, panelY+PANEL_SIZE)
            val rw = revealSizePanel.coerceAtMost(panelX+PANEL_SIZE-rx)
            val rh = revealSizePanel.coerceAtMost(panelY+PANEL_SIZE-ry)
            if (rw <= 0f || rh <= 0f) continue
            val scissor = Rectangle(rx, ry, rw, rh)
            if (ScissorStack.pushScissors(scissor)) {
                Main.Batch.begin()
                Main.Batch.draw(texture, panelX, panelY, PANEL_SIZE, PANEL_SIZE)
                Main.Batch.end()
                ScissorStack.popScissors()
            }
        }

        // spawn zone markers - always visible, not fog-gated, since where
        // your own team spawns (and where the enemy does) isn't something
        // you need to have scouted to know
        Main.Render.polyBatch.shader = null
        Main.Render.polyBatch.begin()
        for ((iy, ix) in MapObject.PlayerSpawnList) {
            val block = Main.BlockList2D[iy][ix]
            val p = toPanel(block.x.toFloat(), block.y.toFloat())
            Main.Render.rect(p[0]-SPAWN_MARKER_RADIUS, p[1]-SPAWN_MARKER_RADIUS, SPAWN_MARKER_RADIUS*2f, SPAWN_MARKER_RADIUS*2f, playerSpawnMarkerColor)
        }
        for ((iy, ix) in MapObject.SpawnerList) {
            val block = Main.BlockList2D[iy][ix]
            val p = toPanel(block.x.toFloat(), block.y.toFloat())
            Main.Render.rect(p[0]-SPAWN_MARKER_RADIUS, p[1]-SPAWN_MARKER_RADIUS, SPAWN_MARKER_RADIUS*2f, SPAWN_MARKER_RADIUS*2f, enemySpawnMarkerColor)
        }
        Main.Render.polyBatch.end()

        // dots on top: teammates/self, then enemies only within reveal range of any teammate
        Main.Render.polyBatch.shader = null
        Main.Render.polyBatch.begin()
        for (mate in teammates) {
            val p = toPanel(mate.x, mate.y)
            val color = if (mate === unit) playerColor else allyColor
            Main.Render.rect(p[0]-DOT_RADIUS, p[1]-DOT_RADIUS, DOT_RADIUS*2f, DOT_RADIUS*2f, color)
        }
        for (enemy in enemies) {
            val visible = teammates.any { mate ->
                val dx = enemy.x-mate.x
                val dy = enemy.y-mate.y
                Math.sqrt((dx*dx+dy*dy).toDouble()) <= visibilityRadiusBlocks*blockSize
            }
            if (!visible) continue
            val p = toPanel(enemy.x, enemy.y)
            Main.Render.rect(p[0]-DOT_RADIUS, p[1]-DOT_RADIUS, DOT_RADIUS*2f, DOT_RADIUS*2f, enemyColor)
        }
        Main.Render.polyBatch.end()
    }
}
