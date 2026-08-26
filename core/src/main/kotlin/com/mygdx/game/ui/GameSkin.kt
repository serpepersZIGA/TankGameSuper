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
 * hand-authored texture atlas: layered, bordered nine-patches for buttons and
 * panels (not flat, empty rectangles), and BitmapFonts that include Cyrillic
 * glyphs so Russian text actually renders.
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

        titleLabelStyle = Label.LabelStyle(titleFont, Color.WHITE)
        bodyLabelStyle = Label.LabelStyle(bodyFont, Color.WHITE)
        hintLabelStyle = Label.LabelStyle(hintFont, Color(0.75f, 0.78f, 0.8f, 1f))

        val accent = Color(0.30f, 0.62f, 0.32f, 1f)
        val accentDark = Color(0.14f, 0.30f, 0.16f, 1f)
        val neutral = Color(0.28f, 0.30f, 0.34f, 1f)
        val neutralDark = Color(0.14f, 0.15f, 0.17f, 1f)

        buttonStyle = TextButton.TextButtonStyle().apply {
            up = raisedPatch(accent, accentDark)
            down = pressedPatch(accentDark, Color.BLACK)
            over = raisedPatch(accent.cpy().lerp(Color.WHITE, 0.15f), accentDark)
            disabled = raisedPatch(Color(0.3f, 0.3f, 0.3f, 1f), Color(0.15f, 0.15f, 0.15f, 1f))
            font = bodyFont
            fontColor = Color.WHITE
            disabledFontColor = Color(0.6f, 0.6f, 0.6f, 1f)
        }

        toggleButtonStyle = TextButton.TextButtonStyle().apply {
            up = raisedPatch(neutral, neutralDark)
            down = pressedPatch(neutralDark, Color.BLACK)
            over = raisedPatch(neutral.cpy().lerp(Color.WHITE, 0.15f), neutralDark)
            checked = pressedPatch(accentDark, Color.BLACK)
            font = bodyFont
            fontColor = Color.WHITE
        }

        sliderStyle = Slider.SliderStyle().apply {
            background = grooveNinePatch(Color(0.12f, 0.13f, 0.15f, 1f), Color(0.35f, 0.37f, 0.4f, 1f))
            knob = raisedPatch(accent, accentDark, size = 20, inset = 6)
            knobOver = raisedPatch(accent.cpy().lerp(Color.WHITE, 0.15f), accentDark, size = 20, inset = 6)
            knobDown = pressedPatch(accentDark, Color.BLACK, size = 20, inset = 6)
        }

        textFieldStyle = TextField.TextFieldStyle().apply {
            font = bodyFont
            fontColor = Color.WHITE
            background = grooveNinePatch(Color(0.10f, 0.11f, 0.13f, 1f), Color(0.35f, 0.37f, 0.4f, 1f))
            cursor = solid(Color.WHITE)
            selection = solid(Color(0.30f, 0.45f, 0.75f, 0.6f))
            messageFontColor = Color(0.55f, 0.57f, 0.6f, 1f)
        }

        panelStyle = Window.WindowStyle(titleFont, Color.WHITE, panelNinePatch())

        scrollPaneStyle = ScrollPane.ScrollPaneStyle().apply {
            background = null
            vScrollKnob = raisedPatch(neutral, neutralDark, size = 16, inset = 5)
            vScroll = grooveNinePatch(Color(0.10f, 0.11f, 0.13f, 1f), Color(0.25f, 0.27f, 0.3f, 1f))
        }

        listStyle = GdxList.ListStyle().apply {
            font = bodyFont
            fontColorSelected = Color.WHITE
            fontColorUnselected = Color(0.82f, 0.84f, 0.86f, 1f)
            selection = raisedPatch(accent, accentDark, size = 16, inset = 4)
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

    /** A bordered rectangle with a subtle top-lit gradient, for an "unpressed" raised look. */
    private fun raisedPatch(base: Color, border: Color, size: Int = 32, inset: Int = 10): NinePatchDrawable =
        gradientPatch(base, border, size, inset, lighterAtTop = true)

    /** The same shape but darker and lit from the bottom, for a "pushed in" look. */
    private fun pressedPatch(base: Color, border: Color, size: Int = 32, inset: Int = 10): NinePatchDrawable =
        gradientPatch(base, border, size, inset, lighterAtTop = false)

    private fun gradientPatch(base: Color, border: Color, size: Int, inset: Int, lighterAtTop: Boolean): NinePatchDrawable {
        val pixmap = Pixmap(size, size, Pixmap.Format.RGBA8888)
        pixmap.setColor(border)
        pixmap.fillRectangle(0, 0, size, size)
        val borderWidth = 2
        val innerSize = size - borderWidth * 2
        for (y in 0 until innerSize) {
            val t = if (lighterAtTop) y / (innerSize - 1f) else 1f - y / (innerSize - 1f)
            val shade = base.cpy().lerp(Color.BLACK, t * 0.35f)
            pixmap.setColor(shade)
            pixmap.drawLine(borderWidth, borderWidth + y, size - borderWidth - 1, borderWidth + y)
        }
        val texture = Texture(pixmap)
        pixmap.dispose()
        ownedTextures.add(texture)
        return NinePatchDrawable(NinePatch(texture, inset, inset, inset, inset))
    }

    /** A recessed/inset look for sliders, text fields and scrollbar tracks. */
    private fun grooveNinePatch(base: Color, border: Color, size: Int = 24, inset: Int = 8): NinePatchDrawable {
        val pixmap = Pixmap(size, size, Pixmap.Format.RGBA8888)
        pixmap.setColor(border)
        pixmap.fillRectangle(0, 0, size, size)
        pixmap.setColor(base)
        pixmap.fillRectangle(1, 1, size - 2, size - 2)
        val texture = Texture(pixmap)
        pixmap.dispose()
        ownedTextures.add(texture)
        return NinePatchDrawable(NinePatch(texture, inset, inset, inset, inset))
    }

    private fun panelNinePatch(size: Int = 40, inset: Int = 16): NinePatchDrawable {
        val pixmap = Pixmap(size, size, Pixmap.Format.RGBA8888)
        pixmap.setColor(Color(0.35f, 0.37f, 0.40f, 1f))
        pixmap.fillRectangle(0, 0, size, size)
        pixmap.setColor(Color(0.09f, 0.10f, 0.12f, 0.94f))
        pixmap.fillRectangle(2, 2, size - 4, size - 4)
        val texture = Texture(pixmap)
        pixmap.dispose()
        ownedTextures.add(texture)
        return NinePatchDrawable(NinePatch(texture, inset, inset, inset, inset))
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
}
