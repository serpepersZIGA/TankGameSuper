package com.mygdx.game.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.mygdx.game.Inventory.Inventory
import com.mygdx.game.main.Main
import com.mygdx.game.unit.Unit

// The player's own tank stats as a labeled panel (bottom-left) instead of
// unlabeled bars crammed around the tank sprite - HP, armor, and one
// reload row per weapon. The old per-tank floating bars are untouched
// (still useful for a glance at teammates/enemies in the world); this is
// specifically "my own status, clearly explained".
object PlayerHud {
    private const val PANEL_X = 24f
    private const val BAR_WIDTH = 170f
    private const val BAR_HEIGHT = 14f
    private const val ROW_HEIGHT = 34f

    private val barBackground = Color(0f, 0f, 0f, 0.45f)
    private val hpHighColor = Color(0.25f, 0.75f, 0.3f, 0.9f)
    private val hpMidColor = Color(0.85f, 0.7f, 0.2f, 0.9f)
    private val hpLowColor = Color(0.85f, 0.2f, 0.2f, 0.9f)
    private val reloadReadyColor = Color(0.3f, 0.75f, 0.9f, 0.9f)
    private val reloadChargingColor = Color(0.5f, 0.5f, 0.55f, 0.9f)

    // Some weapons (flamethrower/acid) actually reload in well under a
    // tenth of a second - the raw ratio flips 0->1 in a couple of frames,
    // which just reads as the bar flickering rather than anything
    // legible. This smooths what's DISPLAYED toward the real value over a
    // minimum window, and holds the "charging" label/color for a minimum
    // time too - it changes how the bar looks, not how fast the weapon
    // actually fires.
    private const val REVEAL_SPEED = 3f
    private const val MIN_CHARGING_DISPLAY_SECONDS = 0.35f
    private val displayedReloadRatio = java.util.WeakHashMap<Unit, Float>()
    private val chargingHoldTimer = java.util.WeakHashMap<Unit, Float>()

    fun render() {
        val unit = Main.RC?.MainUnit ?: return
        val font = Main.font2 ?: return

        var y = 24f
        // weapons first (bottom row), armor and HP stacked above so HP -
        // the thing you glance at most - ends up highest and least likely
        // to be covered by other UI near the bottom edge
        for ((index, tower) in unit.TowerUnitList.withIndex()) {
            drawReloadRow(tower, index, y, font)
            y += ROW_HEIGHT
        }
        drawArmorRow(unit, y, font)
        y += ROW_HEIGHT
        drawHpRow(unit, y, font)
        y += ROW_HEIGHT
        drawMoneyRow(y, font)
    }

    private fun drawMoneyRow(y: Float, font: BitmapFont) {
        Main.Batch.shader = null
        Main.Batch.begin()
        font.draw(Main.Batch, "Деньги: ${Inventory.Money}", PANEL_X, y+BAR_HEIGHT)
        Main.Batch.end()
    }

    private fun drawHpRow(unit: Unit, y: Float, font: BitmapFont) {
        val ratio = if (unit.max_hp > 0) unit.hp.toFloat()/unit.max_hp else 0f
        val fillColor = when {
            ratio > 0.5f -> hpHighColor
            ratio > 0.2f -> hpMidColor
            else -> hpLowColor
        }
        drawBarWithLabel(y, ratio.coerceIn(0f, 1f), fillColor, "HP ${unit.hp}/${unit.max_hp}", font)
    }

    private fun drawArmorRow(unit: Unit, y: Float, font: BitmapFont) {
        Main.Batch.shader = null
        Main.Batch.begin()
        font.draw(Main.Batch, "Броня: ${unit.armorFront.toInt()}", PANEL_X, y+BAR_HEIGHT)
        Main.Batch.end()
    }

    private fun drawReloadRow(tower: Unit, index: Int, y: Float, font: BitmapFont) {
        val actuallyReady = tower.reload_max <= 0f || tower.reload <= 0f
        // inverted from the raw reload countdown on purpose - full bar
        // reading as "ready" matches how every other bar in this panel
        // works (full = good), instead of full meaning "just fired, not
        // ready yet" the way the value itself counts down
        val actualRatio = if (tower.reload_max > 0f) 1f-(tower.reload/tower.reload_max) else 1f
        val dt = Gdx.graphics.deltaTime

        val previousRatio = displayedReloadRatio.getOrDefault(tower, actualRatio)
        val maxStep = REVEAL_SPEED*dt
        val smoothedRatio = (previousRatio + (actualRatio-previousRatio).coerceIn(-maxStep, maxStep)).coerceIn(0f, 1f)
        displayedReloadRatio[tower] = smoothedRatio

        var holdRemaining = chargingHoldTimer.getOrDefault(tower, 0f)
        holdRemaining = if (!actuallyReady) MIN_CHARGING_DISPLAY_SECONDS else (holdRemaining-dt).coerceAtLeast(0f)
        chargingHoldTimer[tower] = holdRemaining
        val displayReady = actuallyReady && holdRemaining <= 0f

        val fillColor = if (displayReady) reloadReadyColor else reloadChargingColor
        val label = "Оружие ${index+1}: " +
                if (displayReady) "готово" else "перезарядка"
        drawBarWithLabel(y, smoothedRatio, fillColor, label, font)
    }

    private fun drawBarWithLabel(y: Float, ratio: Float, fillColor: Color, label: String, font: BitmapFont) {
        Main.Render.polyBatch.shader = null
        Main.Render.polyBatch.begin()
        Main.Render.rect(PANEL_X, y, BAR_WIDTH, BAR_HEIGHT, barBackground)
        if (ratio > 0f) Main.Render.rect(PANEL_X, y, BAR_WIDTH*ratio, BAR_HEIGHT, fillColor)
        Main.Render.polyBatch.end()

        Main.Batch.shader = null
        Main.Batch.begin()
        font.draw(Main.Batch, label, PANEL_X, y+BAR_HEIGHT+16f)
        Main.Batch.end()
    }
}
