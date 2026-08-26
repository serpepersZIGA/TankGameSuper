package com.mygdx.game.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Slider
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Disposable

/**
 * A small set of flat-color Scene2D widget styles, generated at runtime from the
 * game's existing TrueType font instead of a hand-authored texture atlas. New
 * screens (Settings, dialogs, etc.) can share one instance instead of each
 * re-implementing their own ad hoc drawing code.
 */
class GameSkin : Disposable {

    val titleLabelStyle: Label.LabelStyle
    val bodyLabelStyle: Label.LabelStyle
    val buttonStyle: TextButton.TextButtonStyle
    val sliderStyle: Slider.SliderStyle
    val textFieldStyle: TextField.TextFieldStyle

    private val ownedTextures = mutableListOf<Texture>()

    init {
        val titleFont = generateFont(40)
        val bodyFont = generateFont(24)

        titleLabelStyle = Label.LabelStyle(titleFont, Color.WHITE)
        bodyLabelStyle = Label.LabelStyle(bodyFont, Color.WHITE)

        buttonStyle = TextButton.TextButtonStyle().apply {
            up = solid(Color(0.20f, 0.45f, 0.20f, 1f))
            down = solid(Color(0.14f, 0.32f, 0.14f, 1f))
            over = solid(Color(0.27f, 0.58f, 0.27f, 1f))
            font = bodyFont
            fontColor = Color.WHITE
        }

        sliderStyle = Slider.SliderStyle().apply {
            background = solid(Color(0.25f, 0.25f, 0.28f, 1f))
            knob = solid(Color(0.80f, 0.80f, 0.85f, 1f))
        }

        textFieldStyle = TextField.TextFieldStyle().apply {
            font = bodyFont
            fontColor = Color.WHITE
            background = solid(Color(0.16f, 0.16f, 0.18f, 1f))
            cursor = solid(Color.WHITE)
            selection = solid(Color(0.30f, 0.45f, 0.75f, 0.6f))
        }
    }

    private fun generateFont(size: Int) = FreeTypeFontGenerator(Gdx.files.internal("font/Base/BaseFont.ttf")).use { generator ->
        val parameter = FreeTypeFontGenerator.FreeTypeFontParameter()
        parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS
        parameter.size = size
        generator.generateFont(parameter)
    }

    private fun solid(color: Color): TextureRegionDrawable {
        val pixmap = Pixmap(4, 4, Pixmap.Format.RGBA8888)
        pixmap.setColor(color)
        pixmap.fill()
        val texture = Texture(pixmap)
        pixmap.dispose()
        ownedTextures.add(texture)
        return TextureRegionDrawable(TextureRegion(texture))
    }

    override fun dispose() {
        titleLabelStyle.font.dispose()
        bodyLabelStyle.font.dispose()
        ownedTextures.forEach { it.dispose() }
        ownedTextures.clear()
    }
}

private inline fun <T : com.badlogic.gdx.utils.Disposable, R> T.use(block: (T) -> R): R {
    try {
        return block(this)
    } finally {
        this.dispose()
    }
}
