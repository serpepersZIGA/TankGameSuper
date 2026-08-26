package com.mygdx.game.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.mygdx.game.main.Main

/** The game's front door: Play, Settings, Exit. */
object MainMenuScreen : MenuScreen() {

    override fun buildContent(skin: GameSkin): Table {
        val table = Table()
        table.center()

        val title = Label(Localization.tr("menu.main.title"), skin.titleLabelStyle)

        val playButton = TextButton(Localization.tr("menu.main.play"), skin.buttonStyle)
        playButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                Main.ActionGameMain = TankSelectScreen
                TankSelectScreen.show()
            }
        })

        val settingsButton = TextButton(Localization.tr("menu.main.settings"), skin.buttonStyle)
        settingsButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                SettingsScreen.openFrom(MainMenuScreen)
            }
        })

        val devButton = TextButton(Localization.tr("menu.dev.title"), skin.buttonStyle)
        devButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                DevMenuScreen.openFrom(MainMenuScreen)
            }
        })

        val exitButton = TextButton(Localization.tr("menu.main.exit"), skin.buttonStyle)
        exitButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                Gdx.app.exit()
            }
        })

        table.add(title).padBottom(64f).row()
        table.add(playButton).width(320f).height(72f).padBottom(16f).row()
        table.add(settingsButton).width(320f).height(72f).padBottom(16f).row()
        table.add(devButton).width(320f).height(72f).padBottom(16f).row()
        table.add(exitButton).width(320f).height(72f)

        return table
    }
}
