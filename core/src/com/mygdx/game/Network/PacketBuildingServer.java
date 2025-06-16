package com.mygdx.game.Network;

import com.mygdx.game.Network.BuildPacket;
import com.mygdx.game.Network.PacketMapObject;

import java.util.ArrayList;

public class PacketBuildingServer {
    public ArrayList<BuildPacket>BuildPack = new ArrayList<>();
    public ArrayList<ArrayList<PacketMapObject>>ObjectMapPack = new ArrayList<>();
    public float FlameLight;
}
