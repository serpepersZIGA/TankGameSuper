package com.mygdx.game.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.mygdx.game.main.Main
import com.mygdx.game.method.Keyboard
import com.mygdx.game.unit.Unit

/**
 * Drives Inventory (E), Equipment (Z) and Shop (B) together. They share one
 * Stage and one DragAndDrop instance - that's what lets an item be dragged
 * straight from Inventory into Equipment (or back) to equip/unequip it,
 * since Scene2D's DragAndDrop only matches sources and targets registered
 * on the same instance. One Stage also means one input-processor swap
 * instead of three independent ones stepping on each other.
 */
object PlayerMenus {
    val stage = Stage(ScreenViewport())
    val dragAndDrop = DragAndDrop()

    private var builtForUnit: Unit? = null
    private var wasAnyOpen = false

    fun render() {
        val unit = Main.RC?.MainUnit
        if (unit !== builtForUnit) {
            stage.clear()
            dragAndDrop.clear()
            if (unit != null) {
                InventoryOverlay.build(stage, dragAndDrop, unit)
                EquipmentOverlay.build(stage, dragAndDrop, unit)
                ShopOverlay.build(stage, unit)
            }
            builtForUnit = unit
        }
        if (unit == null) return

        val invOpen = Main.inventoryMain.InventoryConf
        val eqOpen = Main.equipmentMain.InventoryConf
        val shopOpen = Main.shopMain.InventoryConf
        val anyOpen = invOpen || eqOpen || shopOpen

        InventoryOverlay.setVisible(invOpen)
        EquipmentOverlay.setVisible(eqOpen)
        ShopOverlay.setVisible(shopOpen)

        if (anyOpen != wasAnyOpen) {
            if (anyOpen) {
                Gdx.input.setInputProcessor(InputMultiplexer(stage, Main.KeyboardObj))
            } else {
                // held key/button surviving a processor swap - see DeathScreen
                Keyboard.resetHeldInputState()
                Gdx.input.setInputProcessor(Main.KeyboardObj)
            }
            wasAnyOpen = anyOpen
        }
        if (!anyOpen) return

        if (invOpen) InventoryOverlay.refresh()
        if (eqOpen) EquipmentOverlay.refresh()
        if (shopOpen) ShopOverlay.refresh()

        stage.act(Gdx.graphics.deltaTime)
        stage.draw()
    }
}
