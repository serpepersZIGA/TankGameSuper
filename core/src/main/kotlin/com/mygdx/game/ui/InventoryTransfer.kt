package com.mygdx.game.ui

import com.mygdx.game.Event.EventGame
import com.mygdx.game.Event.EventTransferItemClient
import com.mygdx.game.Inventory.Inventory
import com.mygdx.game.Inventory.Item
import com.mygdx.game.main.ClientMain
import com.mygdx.game.main.Main
import com.mygdx.game.unit.Unit

/**
 * Shared by Inventory and Equipment's slot drag targets - dragging works the
 * same whether the drop lands in the same grid (reorder) or the other one
 * (equip/unequip), the only difference is which Inventory object each side
 * points at. Mirrors what the old SlotBuffer/SlotPasteClient did over the
 * network for a client, since the host is authoritative over both grids.
 */
object InventoryTransfer {
    fun dropItem(
        sourceInv: Inventory, sourceIsEquipment: Boolean, sx: Int, sy: Int,
        targetInv: Inventory, targetIsEquipment: Boolean, tx: Int, ty: Int,
        unit: Unit
    ) {
        if (sourceInv === targetInv && sx == tx && sy == ty) return
        val dragged = sourceInv.InventorySlots[sx][sy] ?: return
        val displaced = targetInv.InventorySlots[tx][ty]

        if (Main.GameHost) {
            targetInv.InventorySlots[tx][ty] = dragged
            sourceInv.InventorySlots[sx][sy] = displaced
        } else {
            var clientIndex = -1
            for (i in 0 until Main.UnitList.size) {
                if (Main.IDClient == Main.UnitList[i].nConnect) {
                    clientIndex = i
                    break
                }
            }
            if (clientIndex < 0) return
            val event = EventTransferItemClient()
            event.i = clientIndex
            event.x = sx; event.y = sy
            event.x2 = tx; event.y2 = ty
            event.item1 = dragged.ID
            event.item2 = displaced?.ID
            event.InventoryType = targetIsEquipment
            event.InventoryType2 = sourceIsEquipment
            ClientMain.Client.sendTCP(event)
            // optimistic local update so the drag feels instant - the host
            // owns the real state and nothing here contradicts it
            targetInv.InventorySlots[tx][ty] = dragged.clone()
            sourceInv.InventorySlots[sx][sy] = displaced?.clone()
        }
    }

    /** Click-to-use, network-aware the same way the old InventoryUs/InventoryUsClient were. */
    fun useItem(item: Item, unit: Unit, ix: Int, iy: Int, isEquipment: Boolean): Boolean {
        if (!Main.GameHost) {
            for (i in 0 until Main.UnitList.size) {
                if (Main.IDClient == Main.UnitList[i].nConnect) {
                    EventGame.EventGameClient(item.ID, i, ix, iy, isEquipment)
                    break
                }
            }
        }
        return item.Use(unit)
    }
}
