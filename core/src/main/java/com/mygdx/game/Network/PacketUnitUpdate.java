package com.mygdx.game.Network;

import com.mygdx.game.Inventory.ItemPacket;

import java.util.ArrayList;

public class PacketUnitUpdate {
    public boolean ConfDebris,ConfUnit,ConfItem,ConfInventory;
    public PackerServer packServer;
    public ArrayList<ItemPacket>ItemPack;
    public ArrayList<BullPacket>bull;
    public ArrayList<String[][]> inventory,equipment;
    public int IDClient;

}
