package com.mygdx.game.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Align
import com.mygdx.game.main.Main

/** The game's front door: Play, Settings, Exit. */
object MainMenuScreen : MenuScreen() {

    override fun buildContent(skin: GameSkin): Table {
        val root = Table()
        val table = Table()
        table.center()

        val title = Label(Localization.tr("menu.main.title"), skin.titleLabelStyle)

        val playButton = TextButton(Localization.tr("menu.main.play"), skin.buttonStyle)
        playButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                // "local" game is really just self-hosting without ever
                // showing the address/port screen - it's still joinable over
                // the network by anyone who knows the IP, it just doesn't
                // make the player who's starting it deal with that screen
                Main.GameHost = true
                TankSelectScreen.openFrom(MainMenuScreen)
            }
        })

        val networkButton = TextButton(Localization.tr("menu.main.network"), skin.buttonStyle)
        networkButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                Main.ActionGameMain = HostJoinScreen
                HostJoinScreen.show()
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
        table.add(networkButton).width(320f).height(72f).padBottom(16f).row()
        table.add(settingsButton).width(320f).height(72f).padBottom(16f).row()
        table.add(devButton).width(320f).height(72f).padBottom(16f).row()
        table.add(exitButton).width(320f).height(72f)

        root.add(table).expand().row()
        val versionLabel = Label("v${GameVersion.VERSION}", skin.hintLabelStyle)
        root.add(versionLabel).expand(false, false).align(Align.bottomRight).pad(16f)

        return root
    }
}
