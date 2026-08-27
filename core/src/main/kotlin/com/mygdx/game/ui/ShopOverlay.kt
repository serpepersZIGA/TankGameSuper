package com.mygdx.game.ui

import Data.DataImage.TextureAtl
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Scaling
import com.mygdx.game.Event.EventUseClient
import com.mygdx.game.Inventory.Inventory
import com.mygdx.game.Inventory.Item
import com.mygdx.game.main.ClientMain
import com.mygdx.game.main.Main
import com.mygdx.game.unit.Unit

// Shop (B key) is a fixed catalog - clicking a slot buys it straight into
// the player's Inventory if there's enough money, no dragging involved.
// Same bordered look as Inventory/Equipment, with the price stamped on
// each slot.
object ShopOverlay {
    private const val SLOT_SIZE = 76f
    private const val SLOT_PAD = 6f
    private const val CLOSE_BUTTON_SIZE = 28f

    private var window: Window? = null
    private var priceLabels: Array<Array<Label>> = arrayOf()

    fun build(stage: Stage, unit: Unit) {
        val catalog = Main.shopMain.inventory
        val font = Main.font2
        val style = Window.WindowStyle(font, MenuPanelStyle.titleColor, MenuPanelStyle.windowBackground)
        val win = Window(Localization.tr("menu.shop.title"), style)
        win.isMovable = false
        win.isModal = false

        val closeButtonStyle = TextButton.TextButtonStyle().apply {
            this.font = font
            fontColor = MenuPanelStyle.titleColor
        }
        val closeButton = TextButton("X", closeButtonStyle)
        closeButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: com.badlogic.gdx.scenes.scene2d.Actor?) {
                Main.shopMain.InventoryConf = false
            }
        })
        win.titleTable.add(closeButton).size(CLOSE_BUTTON_SIZE, CLOSE_BUTTON_SIZE).padRight(4f)

        val priceLabelStyle = Label.LabelStyle(font, Color(0.9f, 0.85f, 0.6f, 1f))
        val grid = Table()
        grid.pad(14f)
        val rows = catalog.InventorySlots.size
        val cols = if (rows > 0) catalog.InventorySlots[0].size else 0
        val labels = Array(rows) { arrayOfNulls<Label>(cols) }

        for (ix in 0 until rows) {
            for (iy in 0 until cols) {
                val slotStack = Stack()
                val background = Image(MenuPanelStyle.slotBackground)
                val icon = Image()
                icon.setScaling(Scaling.fit)
                val priceLabel = Label("", priceLabelStyle)
                priceLabel.setAlignment(Align.bottomRight)
                slotStack.add(background)
                slotStack.add(icon)
                slotStack.add(priceLabel)
                labels[ix][iy] = priceLabel

                val item = catalog.InventorySlots[ix][iy]
                if (item != null) {
                    icon.drawable = TextureRegionDrawable(TextureAtl.findRegion(item.image))
                    priceLabel.setText(item.Price.toString())
                }

                slotStack.addListener(object : ClickListener() {
                    override fun clicked(event: InputEvent?, x: Float, y: Float) {
                        buy(catalog.InventorySlots[ix][iy], unit)
                    }
                })
                grid.add(slotStack).size(SLOT_SIZE, SLOT_SIZE).pad(SLOT_PAD)
            }
            grid.row()
        }
        @Suppress("UNCHECKED_CAST")
        priceLabels = Array(rows) { r -> Array(cols) { c -> labels[r][c]!! } }

        win.add(grid)
        win.pack()
        win.setPosition(
            (Gdx.graphics.width - win.width) / 2f,
            Gdx.graphics.height*0.06f
        )
        win.isVisible = false
        stage.addActor(win)
        window = win
    }

    fun setVisible(visible: Boolean) {
        window?.isVisible = visible
    }

    fun refresh() {
        // catalog never changes at runtime, nothing to sync each frame
    }

    private fun buy(item: Item?, unit: Unit) {
        if (item == null || item.Price >= Inventory.Money) return
        if (Main.GameHost) {
            Main.inventoryMain.inventory.ItemAdd(item)
            Inventory.Money -= item.Price
        } else {
            for (i in 0 until Main.UnitList.size) {
                if (Main.IDClient == Main.UnitList[i].nConnect) {
                    val event = EventUseClient()
                    event.str = item.ID
                    event.ID = i
                    event.conf = false
                    event.ConfUse = true
                    event.MoneyAdd = true
                    ClientMain.Client.sendTCP(event)

                    Main.inventoryMain.inventory.ItemAdd(item)
                    Inventory.Money -= item.Price
                }
            }
        }
    }
}
