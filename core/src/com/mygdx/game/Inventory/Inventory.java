package com.mygdx.game.Inventory;

import com.mygdx.game.unit.Unit;

import java.util.Arrays;

public class Inventory implements Cloneable{
    public Item[][]InventorySlots;
    public Inventory(Item[][]InventorySlots){
        this.InventorySlots = InventorySlots;
    }
    public void ItemAdd(int x,int y,Item item){
        if(item != null) {
            InventorySlots[x][y] = item.clone();
            return;
        }
        InventorySlots[x][y] = null;
    }
    public void ItemClear(){
        for (Item[] inventorySlot : InventorySlots) {
            Arrays.fill(inventorySlot, null);
        }
    }
    public void ItemAdd(int x,int y,String item){
        if(item != null) {
            for(Object[] obj : Item.IDListItem) {
                if(item.equals(obj[1])) {
                    InventorySlots[x][y] = (Item) obj[0];
                }
            }
            return;
        }
        InventorySlots[x][y] = null;
    }
    public boolean ItemAdd(Item item){
        for (int iX =0;iX<InventorySlots.length;iX++) {
            for (int iY =0;iY<InventorySlots[iX].length;iY++) {
                if(InventorySlots[iX][iY]==null){
                    InventorySlots[iX][iY] = item.clone();
                    return true;
                }
            }
        }
        return false;
    }
    public void ItemRemove(int x,int y){
        InventorySlots[x][y] = null;
    }
    public void ItemUse(int x, int y, Unit unit){
        if(InventorySlots[x][y]!= null) {
            InventorySlots[x][y].Use(unit);
        }
    }
    public boolean ItemUse(Item item, Unit unit){
        int ix = 0;
        int iy = 0;
        for (Item[] inventorySlot : InventorySlots) {
            for (Item value : inventorySlot) {
                if (value == item) {
                    if(value.Use(unit)){
                        InventorySlots[ix][iy] = null;
                    }
                    return true;
                }
                iy++;
            }
            ix++;
        }
        return false;
    }
    public boolean ItemUseType(TypeItem type, Unit unit){
        for (Item[] inventorySlot : InventorySlots) {
            for (Item value : inventorySlot) {
                if (value.typeItem == type) {
                    //unit.GunUse.Reload = unit.reload;
                    //unit.reload = value.Reload;
                    value.UseNull(unit);
                    unit.GunUse = value;
                    return true;
                }
            }
        }
        return false;
    }
    public static int ix,iy;
    public boolean ItemUseTeg(TegItem teg, Unit unit){
        ix = 0;
        iy = 0;
        for (Item[] inventorySlot : InventorySlots) {
            for (Item value : inventorySlot) {
                for(TegItem tegItem : value.teg){
                    if(teg == tegItem){
//                        unit.GunUse.Reload = unit.reload;
//                        unit.reload = value.Reload;
//                        unit.GunUse = value;
                        if(value.Use(unit)){
                            InventorySlots[ix][iy] = null;
                        }
                        return true;
                    }
                    iy++;
                }
                ix++;
                iy = 0;

            }
        }
        return false;
    }

    public void SoldatGunUse(){

    }
    public Inventory clone(){
        try {
            return (Inventory) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

    }

}
