package com.mygdx.game.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.List as GdxList
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Slider
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Disposable

/** The full Unicode block FreeType's DEFAULT_CHARS is missing: Cyrillic letters. */
private val CYRILLIC_CHARS = (0x0400..0x04FF).map { it.toChar() }.joinToString("")
private val SUPPORTED_CHARS = FreeTypeFontGenerator.DEFAULT_CHARS + CYRILLIC_CHARS

/**
 * A small set of Scene2D widget styles, generated at runtime instead of a
 * hand-authored texture atlas. Visual language is "armor plating" rather
 * than the generic rounded-rectangle web-app button look: panels and
 * buttons are octagons with their corners chamfered off like cut steel
 * plate, with a warning-yellow accent stripe standing in for a hazard
 * marking, on a gunmetal base instead of the ubiquitous fantasy-UI green.
 */
class GameSkin : Disposable {

    val titleLabelStyle: Label.LabelStyle
    val bodyLabelStyle: Label.LabelStyle
    val hintLabelStyle: Label.LabelStyle
    val buttonStyle: TextButton.TextButtonStyle
    val toggleButtonStyle: TextButton.TextButtonStyle
    val sliderStyle: Slider.SliderStyle
    val textFieldStyle: TextField.TextFieldStyle
    val panelStyle: Window.WindowStyle
    val scrollPaneStyle: ScrollPane.ScrollPaneStyle
    val listStyle: GdxList.ListStyle

    private val ownedTextures = mutableListOf<Texture>()

    init {
        val titleFont = generateFont(44)
        val bodyFont = generateFont(24)
        val hintFont = generateFont(18)

        titleLabelStyle = Label.LabelStyle(titleFont, AMBER)
        bodyLabelStyle = Label.LabelStyle(bodyFont, Color(0.86f, 0.87f, 0.83f, 1f))
        hintLabelStyle = Label.LabelStyle(hintFont, Color(0.55f, 0.57f, 0.52f, 1f))

        buttonStyle = TextButton.TextButtonStyle().apply {
            up = raisedPlate(STEEL, STEEL_DARK, accentStripe = AMBER)
            down = pressedPlate(STEEL_DARK, Color.BLACK, accentStripe = AMBER_DARK)
            over = raisedPlate(STEEL.cpy().lerp(Color.WHITE, 0.12f), STEEL_DARK, accentStripe = AMBER)
            disabled = raisedPlate(Color(0.32f, 0.32f, 0.30f, 1f), Color(0.16f, 0.16f, 0.15f, 1f), accentStripe = null)
            font = bodyFont
            fontColor = Color(0.92f, 0.93f, 0.90f, 1f)
            disabledFontColor = Color(0.55f, 0.55f, 0.53f, 1f)
        }

        toggleButtonStyle = TextButton.TextButtonStyle().apply {
            up = raisedPlate(STEEL, STEEL_DARK, accentStripe = null)
            down = pressedPlate(STEEL_DARK, Color.BLACK, accentStripe = null)
            over = raisedPlate(STEEL.cpy().lerp(Color.WHITE, 0.12f), STEEL_DARK, accentStripe = null)
            checked = pressedPlate(RUST_DARK, Color.BLACK, accentStripe = RUST)
            font = bodyFont
            fontColor = Color(0.92f, 0.93f, 0.90f, 1f)
        }

        sliderStyle = Slider.SliderStyle().apply {
            background = groovePlate(Color(0.09f, 0.10f, 0.09f, 1f), STEEL_DARK)
            knob = raisedPlate(AMBER, AMBER_DARK, accentStripe = null, size = 22, chamfer = 6, inset = 8)
            knobOver = raisedPlate(AMBER.cpy().lerp(Color.WHITE, 0.15f), AMBER_DARK, accentStripe = null, size = 22, chamfer = 6, inset = 8)
            knobDown = pressedPlate(AMBER_DARK, Color.BLACK, accentStripe = null, size = 22, chamfer = 6, inset = 8)
        }

        textFieldStyle = TextField.TextFieldStyle().apply {
            font = bodyFont
            fontColor = Color(0.92f, 0.93f, 0.90f, 1f)
            background = groovePlate(Color(0.08f, 0.09f, 0.08f, 1f), STEEL_DARK)
            cursor = solid(AMBER)
            selection = solid(Color(0.55f, 0.42f, 0.16f, 0.55f))
            messageFontColor = Color(0.5f, 0.51f, 0.48f, 1f)
        }

        panelStyle = Window.WindowStyle(titleFont, AMBER, panelPlate())

        scrollPaneStyle = ScrollPane.ScrollPaneStyle().apply {
            background = null
            vScrollKnob = raisedPlate(STEEL, STEEL_DARK, accentStripe = null, size = 16, chamfer = 3, inset = 6)
            vScroll = groovePlate(Color(0.08f, 0.09f, 0.08f, 1f), Color(0.22f, 0.24f, 0.21f, 1f))
        }

        listStyle = GdxList.ListStyle().apply {
            font = bodyFont
            fontColorSelected = Color(0.10f, 0.09f, 0.06f, 1f)
            fontColorUnselected = Color(0.82f, 0.84f, 0.80f, 1f)
            selection = raisedPlate(AMBER, AMBER_DARK, accentStripe = null, size = 16, chamfer = 3, inset = 5)
        }
    }

    private fun generateFont(size: Int): com.badlogic.gdx.graphics.g2d.BitmapFont {
        val generator = FreeTypeFontGenerator(Gdx.files.internal("font/Base/BaseFont.ttf"))
        val parameter = FreeTypeFontGenerator.FreeTypeFontParameter()
        parameter.characters = SUPPORTED_CHARS
        parameter.size = size
        val font = generator.generateFont(parameter)
        generator.dispose()
        return font
    }

    private fun solid(color: Color): TextureRegionDrawable {
        val pixmap = Pixmap(4, 4, Pixmap.Format.RGBA8888)
        pixmap.setColor(color)
        pixmap.fill()
        return drawableFrom(pixmap)
    }

    /** A chamfered "armor plate" with a subtle top-lit gradient and an optional
     * hazard-stripe accent along its top edge, for an "unpressed" raised look. */
    private fun raisedPlate(base: Color, border: Color, accentStripe: Color?, size: Int = 32, chamfer: Int = 8, inset: Int = 12): NinePatchDrawable =
        platePixmap(base, border, accentStripe, size, chamfer, inset, lighterAtTop = true)

    /** The same shape but darker and lit from the bottom, for a "pushed in" look. */
    private fun pressedPlate(base: Color, border: Color, accentStripe: Color?, size: Int = 32, chamfer: Int = 8, inset: Int = 12): NinePatchDrawable =
        platePixmap(base, border, accentStripe, size, chamfer, inset, lighterAtTop = false)

    private fun platePixmap(base: Color, border: Color, accentStripe: Color?, size: Int, chamfer: Int, inset: Int, lighterAtTop: Boolean): NinePatchDrawable {
        val pixmap = Pixmap(size, size, Pixmap.Format.RGBA8888)
        val borderWidth = 2
        val shade = if (lighterAtTop) base.cpy().lerp(Color.WHITE, 0.08f) else base.cpy().lerp(Color.BLACK, 0.15f)
        fillOctagon(pixmap, size, chamfer, border)
        fillOctagonInset(pixmap, size, chamfer, borderWidth, shade)

        if (accentStripe != null) {
            pixmap.setColor(accentStripe)
            pixmap.fillRectangle(chamfer, borderWidth, size - chamfer * 2, borderWidth + 1)
        }

        val texture = Texture(pixmap)
        pixmap.dispose()
        ownedTextures.add(texture)
        return NinePatchDrawable(NinePatch(texture, inset, inset, inset, inset))
    }

    /** A recessed/inset chamfered look for sliders, text fields and scrollbar tracks. */
    private fun groovePlate(base: Color, border: Color, size: Int = 24, chamfer: Int = 5, inset: Int = 9): NinePatchDrawable {
        val pixmap = Pixmap(size, size, Pixmap.Format.RGBA8888)
        fillOctagon(pixmap, size, chamfer, border)
        pixmap.setColor(base)
        fillOctagonInset(pixmap, size, chamfer, 1, base)
        val texture = Texture(pixmap)
        pixmap.dispose()
        ownedTextures.add(texture)
        return NinePatchDrawable(NinePatch(texture, inset, inset, inset, inset))
    }

    private fun panelPlate(size: Int = 44, chamfer: Int = 14, inset: Int = 18): NinePatchDrawable {
        val pixmap = Pixmap(size, size, Pixmap.Format.RGBA8888)
        fillOctagon(pixmap, size, chamfer, STEEL_DARK)
        fillOctagonInset(pixmap, size, chamfer, 3, Color(0.07f, 0.08f, 0.07f, 0.95f))
        val texture = Texture(pixmap)
        pixmap.dispose()
        ownedTextures.add(texture)
        return NinePatchDrawable(NinePatch(texture, inset, inset, inset, inset))
    }

    /** Fills an octagon (a square with its four corners chamfered at 45 degrees). */
    private fun fillOctagon(pixmap: Pixmap, size: Int, chamfer: Int, color: Color) {
        pixmap.setColor(color)
        pixmap.fillRectangle(chamfer, 0, size - chamfer * 2, size)
        pixmap.fillRectangle(0, chamfer, size, size - chamfer * 2)
        pixmap.fillTriangle(chamfer, 0, chamfer, chamfer, 0, chamfer)
        pixmap.fillTriangle(size - chamfer, 0, size, chamfer, size - chamfer, chamfer)
        pixmap.fillTriangle(0, size - chamfer, chamfer, size - chamfer, chamfer, size)
        pixmap.fillTriangle(size - chamfer, size, size - chamfer, size - chamfer, size, size - chamfer)
    }

    /** Fills a smaller octagon inset by [border] pixels inside an existing bordered octagon. */
    private fun fillOctagonInset(pixmap: Pixmap, size: Int, chamfer: Int, border: Int, color: Color) {
        val innerSize = size - border * 2
        val innerChamfer = (chamfer - border).coerceAtLeast(1)
        pixmap.setColor(color)
        pixmap.fillRectangle(border + innerChamfer, border, innerSize - innerChamfer * 2, innerSize)
        pixmap.fillRectangle(border, border + innerChamfer, innerSize, innerSize - innerChamfer * 2)
        val x0 = border
        val y0 = border
        pixmap.fillTriangle(x0 + innerChamfer, y0, x0 + innerChamfer, y0 + innerChamfer, x0, y0 + innerChamfer)
        pixmap.fillTriangle(x0 + innerSize - innerChamfer, y0, x0 + innerSize, y0 + innerChamfer, x0 + innerSize - innerChamfer, y0 + innerChamfer)
        pixmap.fillTriangle(x0, y0 + innerSize - innerChamfer, x0 + innerChamfer, y0 + innerSize - innerChamfer, x0 + innerChamfer, y0 + innerSize)
        pixmap.fillTriangle(x0 + innerSize - innerChamfer, y0 + innerSize, x0 + innerSize - innerChamfer, y0 + innerSize - innerChamfer, x0 + innerSize, y0 + innerSize - innerChamfer)
    }

    private fun drawableFrom(pixmap: Pixmap): TextureRegionDrawable {
        val texture = Texture(pixmap)
        pixmap.dispose()
        ownedTextures.add(texture)
        return TextureRegionDrawable(TextureRegion(texture))
    }

    override fun dispose() {
        titleLabelStyle.font.dispose()
        bodyLabelStyle.font.dispose()
        hintLabelStyle.font.dispose()
        ownedTextures.forEach { it.dispose() }
        ownedTextures.clear()
    }

    companion object {
        // Gunmetal + warning-amber "armor plate" palette, instead of the
        // default-feeling green/neutral UI-kit look.
        private val STEEL = Color(0.29f, 0.31f, 0.27f, 1f)
        private val STEEL_DARK = Color(0.12f, 0.13f, 0.11f, 1f)
        private val AMBER = Color(0.85f, 0.65f, 0.20f, 1f)
        private val AMBER_DARK = Color(0.55f, 0.40f, 0.10f, 1f)
        private val RUST = Color(0.72f, 0.32f, 0.16f, 1f)
        private val RUST_DARK = Color(0.40f, 0.16f, 0.08f, 1f)
    }
}
