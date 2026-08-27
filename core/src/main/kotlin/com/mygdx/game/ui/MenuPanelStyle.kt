package com.mygdx.game.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable

// same "armor plating" bordered look everywhere: Inventory, Equipment and
// Shop windows/slots, matching Minimap's frame - one place for it instead
// of three copies of the same pixmap code.
object MenuPanelStyle {
    val titleColor: Color = Color(0.85f, 0.65f, 0.20f, 1f)

    val windowBackground: NinePatchDrawable by lazy {
        borderedNinePatch(
            outer = Color(0.12f, 0.11f, 0.09f, 0.92f),
            inner = Color(0.55f, 0.5f, 0.4f, 1f),
            fill = Color(0.05f, 0.06f, 0.05f, 0.72f),
            outerThickness = 6, innerThickness = 3
        )
    }

    // slots barely showed up against the window's own see-through
    // background before - a visible border of their own fixes that
    val slotBackground: NinePatchDrawable by lazy {
        borderedNinePatch(
            outer = Color(0.5f, 0.46f, 0.38f, 1f),
            inner = Color(0.06f, 0.06f, 0.06f, 0.9f),
            fill = Color(0.16f, 0.17f, 0.15f, 0.9f),
            outerThickness = 2, innerThickness = 1
        )
    }

    fun borderedNinePatch(outer: Color, inner: Color, fill: Color, outerThickness: Int, innerThickness: Int): NinePatchDrawable {
        val border = outerThickness + innerThickness
        val size = border*2 + 4
        val pixmap = Pixmap(size, size, Pixmap.Format.RGBA8888)
        pixmap.setColor(outer)
        pixmap.fill()
        pixmap.setColor(inner)
        pixmap.fillRectangle(outerThickness, outerThickness, size-outerThickness*2, size-outerThickness*2)
        pixmap.setColor(fill)
        pixmap.fillRectangle(border, border, size-border*2, size-border*2)
        val texture = Texture(pixmap)
        pixmap.dispose()
        return NinePatchDrawable(NinePatch(texture, border, border, border, border))
    }
}
