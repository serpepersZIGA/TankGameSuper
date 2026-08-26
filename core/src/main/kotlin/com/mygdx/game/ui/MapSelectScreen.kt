package com.mygdx.game.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.mygdx.game.MapFunction.MapBaseAdd
import com.mygdx.game.MapFunction.MapScan
import com.mygdx.game.main.Main

/** Map selection: same scrollable-list approach as TankSelectScreen. */
object MapSelectScreen : MenuScreen() {

    private var selected: FileHandle? = null

    private fun mapFiles(): List<FileHandle> {
        var files = Gdx.files.internal("Map/maps").list()
        if (files.isEmpty()) {
            MapBaseAdd.AddMap()
            files = Gdx.files.internal("Map/maps").list()
        }
        return files.sortedBy { it.name() }
    }

    override fun buildContent(skin: GameSkin): Table {
        val root = Table()
        root.center()

        val title = Label(Localization.tr("menu.map.title"), skin.titleLabelStyle)

        val files = mapFiles()
        if (selected == null || files.none { it.path() == selected?.path() }) {
            selected = files.firstOrNull()
        }

        val listTable = Table()
        val group = ButtonGroup<TextButton>()
        group.setMinCheckCount(1)
        group.setMaxCheckCount(1)
        for (file in files) {
            val label = MapScan.MapName(file.path()).ifBlank { file.nameWithoutExtension() }
            val button = TextButton(label, skin.toggleButtonStyle)
            button.isChecked = file.path() == selected?.path()
            group.add(button)
            button.addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    if (button.isChecked) selected = file
                }
            })
            listTable.add(button).width(360f).height(64f).pad(6f).row()
        }
        val scrollPane = ScrollPane(listTable, skin.scrollPaneStyle)
        scrollPane.setScrollingDisabled(true, false)
        scrollPane.setFadeScrollBars(false)

        val backButton = TextButton(Localization.tr("menu.map.back"), skin.buttonStyle)
        backButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                Main.ActionGameMain = TankSelectScreen
                TankSelectScreen.show()
            }
        })

        val continueButton = TextButton(Localization.tr("menu.map.continue"), skin.buttonStyle)
        continueButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                selected?.let { file ->
                    MapScan.MapSize(file.path())
                    MapScan.MapInput(file.path())
                }
                Main.ActionGameMain = HostJoinScreen
                HostJoinScreen.show()
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
