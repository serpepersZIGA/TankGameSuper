package com.mygdx.game.ui

import Data.DataImage.TextureAtl
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Scaling
import com.mygdx.game.Inventory.Item
import com.mygdx.game.main.Main
import com.mygdx.game.unit.Unit

// Same window style as Inventory, but backed by the unit's equipment grid
// (upgrade slots) instead. Drag an item here from the Inventory window to
// equip it, or back to unequip - both windows share one DragAndDrop (wired
// up in PlayerMenus), so a drop lands correctly whichever grid it started
// in. Sits on the opposite side of the screen from Inventory so the two can
// be open together for the drag.
object EquipmentOverlay {
    private const val SLOT_SIZE = 76f
    private const val SLOT_PAD = 6f
    private const val CLOSE_BUTTON_SIZE = 28f

    private var window: Window? = null
    private var slotImages: Array<Array<Image>> = arrayOf()
    private var inventory: com.mygdx.game.Inventory.Inventory? = null

    fun build(stage: Stage, dragAndDrop: DragAndDrop, unit: Unit) {
        val inv = unit.equipment
        inventory = inv
        val font = Main.font2
        val style = Window.WindowStyle(font, MenuPanelStyle.titleColor, MenuPanelStyle.windowBackground)
        val win = Window(Localization.tr("menu.equipment.title"), style)
        win.isMovable = false
        win.isModal = false

        val closeButtonStyle = TextButton.TextButtonStyle().apply {
            this.font = font
            fontColor = MenuPanelStyle.titleColor
        }
        val closeButton = TextButton("X", closeButtonStyle)
        closeButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: com.badlogic.gdx.scenes.scene2d.Actor?) {
                Main.equipmentMain.InventoryConf = false
            }
        })
        win.titleTable.add(closeButton).size(CLOSE_BUTTON_SIZE, CLOSE_BUTTON_SIZE).padRight(4f)

        val grid = Table()
        grid.pad(14f)
        val rows = inv.InventorySlots.size
        val cols = if (rows > 0) inv.InventorySlots[0].size else 0
        val images = Array(rows) { arrayOfNulls<Image>(cols) }

        for (ix in 0 until rows) {
            for (iy in 0 until cols) {
                val slotStack = Stack()
                val background = Image(MenuPanelStyle.slotBackground)
                val icon = Image()
                icon.setScaling(Scaling.fit)
                slotStack.add(background)
                slotStack.add(icon)
                images[ix][iy] = icon

                wireSlot(slotStack, icon, dragAndDrop, inv, unit, ix, iy)
                grid.add(slotStack).size(SLOT_SIZE, SLOT_SIZE).pad(SLOT_PAD)
            }
            grid.row()
        }
        @Suppress("UNCHECKED_CAST")
        slotImages = Array(rows) { r -> Array(cols) { c -> images[r][c]!! } }

        win.add(grid)
        win.pack()
        // right side - Inventory sits on the left, so the two never overlap
        // when open at once for a drag between them
        win.setPosition(
            Gdx.graphics.width*0.92f - win.width,
            (Gdx.graphics.height - win.height) / 2f
        )
        win.isVisible = false
        stage.addActor(win)
        window = win
    }

    fun setVisible(visible: Boolean) {
        window?.isVisible = visible
    }

    fun refresh() {
        val inv = inventory ?: return
        for (ix in slotImages.indices) {
            for (iy in slotImages[ix].indices) {
                val item: Item? = inv.InventorySlots.getOrNull(ix)?.getOrNull(iy)
                val icon = slotImages[ix][iy]
                icon.drawable = if (item == null) null else TextureRegionDrawable(TextureAtl.findRegion(item.image))
            }
        }
    }

    private fun wireSlot(slotStack: Stack, icon: Image, dragAndDrop: DragAndDrop, inventory: com.mygdx.game.Inventory.Inventory, unit: Unit, ix: Int, iy: Int) {
        slotStack.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                val item = inventory.InventorySlots[ix][iy] ?: return
                if (InventoryTransfer.useItem(item, unit, ix, iy, true)) {
                    inventory.InventorySlots[ix][iy] = null
                    icon.drawable = null
                }
            }
        })

        dragAndDrop.addSource(object : DragAndDrop.Source(slotStack) {
            override fun dragStart(event: InputEvent?, x: Float, y: Float, pointer: Int): DragAndDrop.Payload? {
                val item = inventory.InventorySlots[ix][iy] ?: return null
                val payload = DragAndDrop.Payload()
                payload.`object` = SlotRef(inventory, true, ix, iy)
                val drag = Image(TextureRegionDrawable(TextureAtl.findRegion(item.image)))
                drag.setSize(SLOT_SIZE, SLOT_SIZE)
                payload.dragActor = drag
                return payload
            }
        })

        dragAndDrop.addTarget(object : DragAndDrop.Target(slotStack) {
            override fun drag(source: DragAndDrop.Source?, payload: DragAndDrop.Payload?, x: Float, y: Float, pointer: Int): Boolean = true
            override fun drop(source: DragAndDrop.Source?, payload: DragAndDrop.Payload?, x: Float, y: Float, pointer: Int) {
                val from = payload?.`object` as? SlotRef ?: return
                InventoryTransfer.dropItem(from.inventory, from.isEquipment, from.ix, from.iy, inventory, true, ix, iy, unit)
                Main.equipmentMain.InventoryReload(unit)
            }
        })
    }
}
