package com.mygdx.game.Inventory;

import com.mygdx.game.unit.Unit;

import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.StrictMath.sqrt;

public class Inventory implements Cloneable{
    public Item[][]InventorySlots;
    public boolean ConfRefactor;
    public static int MoneyAdd;
    public static int Money;
    public static ArrayList<Item> AssortmentList = new ArrayList<>();
    public String[][]inventoryStr;
    public int xySize;
    public Inventory(Item[][]InventorySlots,int conf){
        switch (conf) {
            case 0:
            xySize = (int) sqrt(AssortmentList.size()) + 1;
            this.InventorySlots = new Item[xySize][xySize];
            int ii = 0;
            int i2 = 0;
            //System.out.println("DID "+AssortmentList.size());
            for (Item item : AssortmentList) {
                this.InventorySlots[ii][i2] = item;
                ii++;
                if (ii == xySize) {
                    i2++;
                }

            }
            break;
            case 1:
                ConfRefactor = true;
                this.InventorySlots = InventorySlots;
                inventoryStr = new String[InventorySlots.length][InventorySlots[0].length];
                for (int ix = 0;ix<InventorySlots.length;ix++) {
                    for (int iy = 0;iy<InventorySlots[ix].length;iy++) {
                        if (InventorySlots[ix][iy] != null) {
                            inventoryStr[ix][iy] = InventorySlots[ix][iy].ID;
                        }


                    }
                }
                break;

        }
    }
    public static void AssortmentAdd(Item item){
        AssortmentList.add(item);
    }
    public void ItemAdd(int x,int y,Item item){
        //if(item != null) {
        //ConfRefactor = true;
        InventorySlots[x][y] = item.clone();
        //inventoryStr[x][y] = item.ID;
            return;
        //}
        //InventorySlots[x][y] = null;
    }
    public void ItemClear(){
        ConfRefactor = true;
        for (Item[] inventorySlot : InventorySlots) {
            Arrays.fill(inventorySlot, null);
        }
        for(String[] inventoryStr : inventoryStr){
            Arrays.fill(inventoryStr, null);
        }
    }
    public void ItemAdd(int x,int y,String item){
        ConfRefactor = true;
        if(item != null) {
            InventorySlots[x][y] = Item.IDListItem.get(item);
            inventoryStr[x][y] = item;
            return;
        }
        InventorySlots[x][y] = null;
        inventoryStr[x][y] = null;
    }
    public boolean ItemAdd(Item item){
        ConfRefactor = true;
        for (int iX =0;iX<InventorySlots.length;iX++) {
            for (int iY =0;iY<InventorySlots[iX].length;iY++) {
                if(InventorySlots[iX][iY]==null){
                    InventorySlots[iX][iY] = item.clone();
                    inventoryStr[iX][iY] = item.ID;
                    return true;
                }
            }
        }
        return false;
    }
    public void ItemRemove(int x,int y){
        ConfRefactor = true;
        InventorySlots[x][y] = null;
        inventoryStr[x][y] = null;
    }
    public void ItemUse(int x, int y, Unit unit){
        ConfRefactor = true;
        if(InventorySlots[x][y]!= null) {
            if(InventorySlots[x][y].Use(unit)){
                inventoryStr[x][y] = null;
                InventorySlots[x][y] = null;
            }
        }
    }
    public boolean ItemUse(Item item, Unit unit){
        int ix = 0;
        int iy = 0;
        ConfRefactor = true;
        for (Item[] inventorySlot : InventorySlots) {
            for (Item value : inventorySlot) {
                if (value == item) {
                    if(value.Use(unit)){
                        InventorySlots[ix][iy] = null;
                        inventoryStr[ix][iy] = null;
                    }
                    return true;
                }
                iy++;
            }
            iy = 0;
            ix++;
        }
        return false;
    }
    public boolean ItemUseType(TypeItem type, Unit unit){
        ConfRefactor = true;
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
    public boolean ItemUseTeg(TegItem teg, Unit unit){
        int ix = 0;
        int iy = 0;
        ConfRefactor = true;
        for (Item[] inventorySlot : InventorySlots) {
            for (Item value : inventorySlot) {
                for(TegItem tegItem : value.teg){
                    if(teg == tegItem){
//                        unit.GunUse.Reload = unit.reload;
//                        unit.reload = value.Reload;
//                        unit.GunUse = value;
                        if(value.Use(unit)){
                            InventorySlots[ix][iy] = null;
                            inventoryStr[ix][iy] = null;
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
