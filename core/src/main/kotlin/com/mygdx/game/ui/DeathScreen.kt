package com.mygdx.game.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.mygdx.game.main.Main

/**
 * Shown when the player's own tank is destroyed. Before this, the game had
 * no player-death handling at all - the unit was simply removed from the
 * world, leaving the camera and controls pointed at nothing.
 */
object DeathScreen : MenuScreen() {

    override fun buildContent(skin: GameSkin): Table {
        val table = Table()
        table.center()

        val title = Label(Localization.tr("death.title"), skin.titleLabelStyle)

        val respawnButton = TextButton(Localization.tr("death.respawn"), skin.buttonStyle)
        respawnButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                if (Main.GameHost) {
                    // The host is authoritative over units, so it can just
                    // create one directly.
                    com.mygdx.game.main.ActionMenu.SpawnPlayer()
                } else {
                    // A client doesn't own the unit list - it has to ask the
                    // server for a new unit the same way it did when first
                    // joining.
                    com.mygdx.game.main.ActionGameClient.ActionGameClientIteration()
                }
                Main.ActionGameMain = Main.ActionGameTotal
                Gdx.input.setInputProcessor(Main.KeyboardObj)
            }
        })

        table.add(title).padBottom(48f).row()
        table.add(respawnButton).width(280f).height(72f)

        return table
    }
}
