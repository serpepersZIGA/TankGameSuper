package com.mygdx.game.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.mygdx.game.main.Main
import com.mygdx.game.menu.button.ButtonTank.ListTankPlayerAdd

/** Tank selection: a scrollable list built with a real ScrollPane/Table instead
 * of hand-computed pixel offsets, so it can never end up positioned off-screen. */
object TankSelectScreen : MenuScreen() {

    init {
        onEscape = { goBack() }
    }

    private fun goBack() {
        Main.ActionGameMain = MainMenuScreen
        MainMenuScreen.show()
    }

    private fun tankIds(): List<String> {
        var files = Gdx.files.internal("PlayerAllSpawnList").list()
        if (files.isEmpty()) {
            ListTankPlayerAdd.AddListTank()
            files = Gdx.files.internal("PlayerAllSpawnList").list()
        }
        return files.map { it.name() }.sorted()
    }

    private fun displayName(tankId: String): String {
        val key = "tank.${tankId.lowercase().replace("-", "")}.name"
        return Localization.trOrDefault(key, tankId)
    }

    override fun buildContent(skin: GameSkin): Table {
        val root = Table()
        root.center()

        val title = Label(Localization.tr("menu.tank.title"), skin.titleLabelStyle)

        val ids = tankIds()
        if (Main.SpawnIDPlayer == null || ids.none { it == Main.SpawnIDPlayer }) {
            Main.SpawnIDPlayer = ids.firstOrNull()
        }

        val listTable = Table()
        val group = ButtonGroup<TextButton>()
        group.setMinCheckCount(1)
        group.setMaxCheckCount(1)
        for (id in ids) {
            val button = TextButton(displayName(id), skin.toggleButtonStyle)
            button.isChecked = id == Main.SpawnIDPlayer
            group.add(button)
            button.addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    if (button.isChecked) Main.SpawnIDPlayer = id
                }
            })
            listTable.add(button).width(360f).height(64f).pad(6f).row()
        }
        val scrollPane = ScrollPane(listTable, skin.scrollPaneStyle)
        scrollPane.setScrollingDisabled(true, false)
        scrollPane.setFadeScrollBars(false)

        val backButton = TextButton(Localization.tr("menu.tank.back"), skin.buttonStyle)
        backButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                goBack()
            }
        })

        val continueButton = TextButton(Localization.tr("menu.tank.continue"), skin.buttonStyle)
        continueButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                Main.ActionGameMain = MapSelectScreen
                MapSelectScreen.show()
            }
        })

        val buttonRow = Table()
        buttonRow.add(backButton).width(220f).height(64f).padRight(20f)
        buttonRow.add(continueButton).width(220f).height(64f)

        root.add(title).padBottom(32f).row()
        root.add(scrollPane).width(420f).height(420f).padBottom(32f).row()
        root.add(buttonRow)

        return root
    }
}
