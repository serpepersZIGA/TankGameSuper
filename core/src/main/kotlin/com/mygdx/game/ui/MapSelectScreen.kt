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
import com.badlogic.gdx.utils.Align
import com.mygdx.game.MapFunction.MapBaseAdd
import com.mygdx.game.MapFunction.MapScan
import com.mygdx.game.MapFunction.ProceduralMapGenerator
import com.mygdx.game.MapFunction.ProceduralTerrainPainter
import com.mygdx.game.main.Main

/** Map selection: same scrollable-list approach as TankSelectScreen. */
object MapSelectScreen : MenuScreen() {

    private var selected: FileHandle? = null

    init {
        onEscape = { goBack() }
    }

    private fun goBack() {
        Main.ActionGameMain = TankSelectScreen
        TankSelectScreen.show()
    }

    private fun mapFiles(): List<FileHandle> {
        var files = Gdx.files.internal("Map/maps").list()
        if (files.isEmpty()) {
            MapBaseAdd.AddMap()
            files = Gdx.files.internal("Map/maps").list()
        }
        return files.sortedBy { it.name() }
    }

    private fun isGenerated(file: FileHandle) = procedureSeed(file.nameWithoutExtension()) != null

    override fun buildContent(skin: GameSkin): Table {
        val root = Table()
        root.center()

        val title = Label(Localization.tr("menu.map.title"), skin.titleLabelStyle)

        val files = mapFiles()
        if (selected == null || files.none { it.path() == selected?.path() }) {
            selected = files.firstOrNull()
        }
        // split so a growing pile of throwaway generated maps doesn't bury
        // the handful of hand-made default ones in the same list
        val defaultFiles = files.filterNot { isGenerated(it) }
        val generatedFiles = files.filter { isGenerated(it) }

        val group = ButtonGroup<TextButton>()
        group.setMinCheckCount(0)
        group.setMaxCheckCount(1)

        val listTable = Table()
        listTable.add(Label(Localization.tr("menu.map.section.default"), skin.hintLabelStyle))
            .align(Align.left).padBottom(4f).row()
        for (file in defaultFiles) {
            addMapRow(listTable, group, skin, file, deletable = false)
        }
        listTable.add(Label(Localization.tr("menu.map.section.generated"), skin.hintLabelStyle))
            .align(Align.left).padTop(16f).padBottom(4f).row()
        for (file in generatedFiles) {
            addMapRow(listTable, group, skin, file, deletable = true)
        }

        val scrollPane = ScrollPane(listTable, skin.scrollPaneStyle)
        scrollPane.setScrollingDisabled(true, false)
        scrollPane.setFadeScrollBars(false)

        val backButton = TextButton(Localization.tr("menu.map.back"), skin.buttonStyle)
        backButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                goBack()
            }
        })

        val generateButton = TextButton(Localization.tr("menu.map.generate"), skin.buttonStyle)
        generateButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                val seed = System.currentTimeMillis()
                val size = ProceduralMapGenerator.DEFAULT_SIZE
                val path = ProceduralMapGenerator.generateLayoutAndSave(seed, size, size, "Map/maps/Procedural_$seed.mapt")
                selected = Gdx.files.internal(path)
                invalidate()
            }
        })

        val continueButton = TextButton(Localization.tr("menu.map.continue"), skin.buttonStyle)
        continueButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                selected?.let { file ->
                    MapScan.MapSize(file.path())
                    MapScan.MapInput(file.path())
                    // a procedurally-generated map's ground was never baked into
                    // the file - repaint it now from the seed in its own name,
                    // deterministically reproducing the same terrain every time
                    // this particular file is loaded
                    val seed = procedureSeed(file.nameWithoutExtension())
                    if (seed != null) {
                        ProceduralTerrainPainter.paint(seed, Main.xMap, Main.yMap)
                    }
                }
                Main.ActionGameMain = HostJoinScreen
                HostJoinScreen.show()
            }
        })

        val buttonRow = Table()
        buttonRow.add(backButton).width(220f).height(64f).padRight(20f)
        buttonRow.add(continueButton).width(220f).height(64f)

        root.add(title).padBottom(32f).row()
        root.add(scrollPane).width(420f).height(420f).padBottom(16f).row()
        root.add(generateButton).height(56f).padBottom(24f).row()
        root.add(buttonRow)

        return root
    }

    private fun addMapRow(listTable: Table, group: ButtonGroup<TextButton>, skin: GameSkin, file: FileHandle, deletable: Boolean) {
        val label = MapScan.MapName(file.path()).ifBlank { file.nameWithoutExtension() }
        val button = TextButton(label, skin.toggleButtonStyle)
        button.isChecked = file.path() == selected?.path()
        group.add(button)
        button.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                if (button.isChecked) selected = file
            }
        })
        val row = Table()
        row.add(button).width(if (deletable) 300f else 360f).height(64f)
        if (deletable) {
            val deleteButton = TextButton(Localization.tr("menu.map.delete"), skin.buttonStyle)
            deleteButton.addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    file.delete()
                    if (selected?.path() == file.path()) selected = null
                    invalidate()
                }
            })
            row.add(deleteButton).width(52f).height(64f).padLeft(8f)
        }
        listTable.add(row).pad(6f).row()
    }

    private fun procedureSeed(name: String): Long? =
        Regex("^Procedural_(-?\\d+)$").find(name)?.groupValues?.get(1)?.toLongOrNull()
}
