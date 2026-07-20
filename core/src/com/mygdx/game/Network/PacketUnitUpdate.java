package com.mygdx.game.Network;

import com.mygdx.game.Inventory.ItemPacket;

import java.util.ArrayList;

public class PacketUnitUpdate {
    public boolean ConfDebris,ConfUnit,ConfItem;
    public ArrayList<ItemPacket>ItemPack;
    public ArrayList<BullPacket>bull;
}
