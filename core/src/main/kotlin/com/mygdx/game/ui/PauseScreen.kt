package com.mygdx.game.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.mygdx.game.MapFunction.MapScan
import com.mygdx.game.block.Block
import com.mygdx.game.main.ActionGame
import com.mygdx.game.main.ClientMain
import com.mygdx.game.main.Main
import com.mygdx.game.main.ServerMain
import com.mygdx.game.method.RenderCenter

// Esc menu during a match. Replaces the old Play2/ExitPlay/Exit buttons.
object PauseScreen : MenuScreen() {

    override fun buildContent(skin: GameSkin): Table {
        val table = Table()
        table.center()

        val title = Label(Localization.tr("pause.title"), skin.titleLabelStyle)

        val resumeButton = TextButton(Localization.tr("pause.resume"), skin.buttonStyle)
        resumeButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                Main.ActionGameMain = Main.ActionGameTotal
                Gdx.input.setInputProcessor(Main.KeyboardObj)
            }
        })

        val exitButton = TextButton(Localization.tr("pause.exit"), skin.buttonStyle)
        exitButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                exitToMenu()
            }
        })

        val quitButton = TextButton(Localization.tr("pause.quit"), skin.buttonStyle)
        quitButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                Gdx.app.exit()
            }
        })

        table.add(title).padBottom(48f).row()
        table.add(resumeButton).width(280f).height(64f).padBottom(16f).row()
        table.add(exitButton).width(280f).height(64f).padBottom(16f).row()
        table.add(quitButton).width(280f).height(64f)

        return table
    }

    // tears down the running match (host or client) and drops back to the main menu
    private fun exitToMenu() {
        if (Main.ActionGameTotal === ActionGame.ActionGameH) {
            ServerMain.Server.close()
            ServerMain.nConnect = 0
            ServerMain.Server = null
        } else {
            ClientMain.Client.close()
            ClientMain.Client = null
        }
        Main.ActionGameTotal = null
        RenderCenter.IndBuilding.clear()
        Block.passability_detected2()
        Main.UnitList.clear()
        Main.BuildingList.clear()
        Main.BulletList.clear()
        Main.LightSystem.lights.clear()
        Main.LightSystem.lightsRender.clear()
        Main.BlockList2D.clear()
        MapScan.MapSize("Map/maps/MapBase.mapt")
        MapScan.MapInput("Map/maps/MapBase.mapt")

        Main.ActionGameMain = MainMenuScreen
        MainMenuScreen.show()
    }
}
