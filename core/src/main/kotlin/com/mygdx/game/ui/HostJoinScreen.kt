package com.mygdx.game.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.mygdx.game.main.ActionGame
import com.mygdx.game.main.ClientMain
import com.mygdx.game.main.Main

/**
 * Host-or-join screen. Replaces the old Swing pop-up window that used to open
 * on every launch: the address/port are now entered right here, in-game, and
 * the last few servers connected to are remembered and offered as a one-click
 * list instead of having to retype them.
 */
object HostJoinScreen : MenuScreen() {

    private const val DEFAULT_PORT = 27950

    override fun buildContent(skin: GameSkin): Table {
        val root = Table()
        root.center()

        val title = Label(Localization.tr("menu.hostjoin.title"), skin.titleLabelStyle)

        val hostButton = TextButton(Localization.tr("menu.hostjoin.host"), skin.buttonStyle)
        hostButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                Main.GameStart = true
                Main.GameHost = true
                Main.ActionGameMain = ActionGame.ActionMenu
                Gdx.input.setInputProcessor(Main.KeyboardObj)
            }
        })

        val addressField = TextField(ClientMain.IP ?: "127.0.0.1", skin.textFieldStyle)
        val portField = TextField(DEFAULT_PORT.toString(), skin.textFieldStyle)
        portField.setTextFieldFilter { _, c -> c.isDigit() }

        val addressLabel = Label(Localization.tr("menu.hostjoin.address"), skin.bodyLabelStyle)
        val portLabel = Label(Localization.tr("menu.hostjoin.port"), skin.bodyLabelStyle)

        val connectButton = TextButton(Localization.tr("menu.hostjoin.connect"), skin.buttonStyle)
        connectButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                val address = addressField.text.trim().ifBlank { "127.0.0.1" }
                val port = portField.text.toIntOrNull() ?: DEFAULT_PORT
                ClientMain.IP = address
                Main.tcpPort = port
                Main.udpPort = port
                GameSettings.rememberServer(address, port)
                Main.GameStart = true
                Main.GameHost = false
                Main.ActionGameMain = ActionGame.ActionMenu
                Gdx.input.setInputProcessor(Main.KeyboardObj)
            }
        })

        val recentLabel = Label(Localization.tr("menu.hostjoin.recent"), skin.hintLabelStyle)
        val recentList = Table()
        val recent = GameSettings.recentServers
        if (recent.isEmpty()) {
            recentList.add(Label(Localization.tr("menu.hostjoin.norecent"), skin.hintLabelStyle))
        } else {
            for (server in recent) {
                val entryButton = TextButton(server.toString(), skin.toggleButtonStyle)
                entryButton.addListener(object : ChangeListener() {
                    override fun changed(event: ChangeEvent?, actor: Actor?) {
                        addressField.text = server.address
                        portField.text = server.port.toString()
                        entryButton.isChecked = false
                    }
                })
                recentList.add(entryButton).width(300f).height(48f).pad(4f).row()
            }
        }
        val recentScroll = ScrollPane(recentList, skin.scrollPaneStyle)
        recentScroll.setScrollingDisabled(true, false)
        recentScroll.setFadeScrollBars(false)

        val backButton = TextButton(Localization.tr("menu.hostjoin.back"), skin.buttonStyle)
        backButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                Main.ActionGameMain = MapSelectScreen
                MapSelectScreen.show()
            }
        })

        val joinRow = Table()
        joinRow.add(addressLabel).padRight(8f)
        joinRow.add(addressField).width(220f).padRight(20f)
        joinRow.add(portLabel).padRight(8f)
        joinRow.add(portField).width(100f).padRight(20f)
        joinRow.add(connectButton).width(180f).height(56f)

        root.add(title).padBottom(32f).colspan(2).row()
        root.add(hostButton).width(260f).height(64f).colspan(2).padBottom(32f).row()
        root.add(joinRow).colspan(2).padBottom(24f).row()
        root.add(recentLabel).padBottom(8f).colspan(2).row()
        root.add(recentScroll).width(340f).height(220f).colspan(2).padBottom(32f).row()
        root.add(backButton).width(220f).height(64f).colspan(2)

        return root
    }
}
