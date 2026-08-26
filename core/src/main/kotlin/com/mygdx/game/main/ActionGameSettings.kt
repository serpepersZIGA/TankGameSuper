package com.mygdx.game.main

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Slider
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.mygdx.game.ui.GameSettings
import com.mygdx.game.ui.GameSkin

/**
 * A proper Scene2D settings screen, reachable from the main menu, so options can
 * be changed from inside the game instead of only by editing files or code.
 * Plugs into the existing ActionGame state-machine (Main.ActionGameMain) the
 * same way the host/client/menu states already do, so no other game code has
 * to change to support it.
 */
class ActionGameSettings : ActionGame() {

    private var stage: Stage? = null
    private var skin: GameSkin? = null
    private var volumeValueLabel: Label? = null

    override fun action() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        val activeStage = stage ?: buildStage()
        activeStage.act(Gdx.graphics.deltaTime)
        activeStage.draw()
    }

    private fun buildStage(): Stage {
        val skin = GameSkin()
        this.skin = skin

        val newStage = Stage(ScreenViewport())
        stage = newStage
        Gdx.input.setInputProcessor(newStage)

        val title = Label("Settings", skin.titleLabelStyle)

        val volumeCaption = Label("Sound volume", skin.bodyLabelStyle)
        val volumeValueLabel = Label(volumePercentText(GameSettings.soundVolume), skin.bodyLabelStyle)
        this.volumeValueLabel = volumeValueLabel

        val volumeSlider = Slider(0f, 1f, 0.01f, false, skin.sliderStyle)
        volumeSlider.value = GameSettings.soundVolume
        volumeSlider.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                GameSettings.setSoundVolume(volumeSlider.value)
                volumeValueLabel.setText(volumePercentText(volumeSlider.value))
            }
        })

        val backButton = TextButton("Back", skin.buttonStyle)
        backButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                returnToMenu()
            }
        })

        val table = Table()
        table.setFillParent(true)
        table.center()
        table.add(title).padBottom(48f).colspan(2).row()
        table.add(volumeCaption).padRight(20f)
        table.add(volumeValueLabel).width(60f).row()
        table.add(volumeSlider).colspan(2).width(420f).padTop(8f).padBottom(48f).row()
        table.add(backButton).colspan(2).width(220f).height(64f)

        newStage.addActor(table)
        return newStage
    }

    private fun returnToMenu() {
        Main.ActionGameMain = ActionGame.ActionMenu
        Gdx.input.setInputProcessor(Main.KeyboardObj)
    }

    private fun volumePercentText(value: Float) = "${Math.round(value * 100)}%"
}
