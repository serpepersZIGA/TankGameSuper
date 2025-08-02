package com.mygdx.game.unit;

import com.mygdx.game.Network.PacketUnitUpdate;
import com.mygdx.game.Network.DebrisPacket;
import com.mygdx.game.Network.TransportPacket;
import com.mygdx.game.unit.moduleUnit.*;

import java.util.ArrayList;

public class TransportRegister {
    public static ArrayList<TransportPacket> PacketUnit = new ArrayList<>();
    public static PacketUnitUpdate packetUnitUpdate = new PacketUnitUpdate();
    public static ArrayList<DebrisPacket> PacketDebris = new ArrayList<>();
    public static Unit TrackRemountT1, TrackSoldatT1, Helicopter_t1
            ,PlayerCannonFlameA1,PlayerCannonFlameA2,PlayerCannonMortarA1,PlayerCannonMachineGunA1,
            PlayerCannonAcidA1,
    Veteran,Soldat,Jaeger;

    public static void Create() {
        ArrayList<Cannon>list = new ArrayList<>();
        list.add(RegisterModuleCannon.CannonFlameAuxiliary);
        list.add(RegisterModuleCannon.CannonFlameAuxiliary);
        list.add(RegisterModuleCannon.CannonFlame);
        ArrayList<String>listr = new ArrayList<>();
        listr.add("Flk4CL");
        listr.add("Flk4CL");
        listr.add("Flk4C");

        list = new ArrayList<>();
        TrackSoldatT1 = new UnitPattern("TrSolS1",RegisterModuleCorpus.CorpusSoldatTrackS1,
                RegisterModuleEngine.Engine1E,list,new int[][]{},ClassUnit.SupportTransport,0);
        TrackSoldatT1.time_spawn_soldat_max = 200;

        TrackRemountT1 = new UnitPattern("TrRemR1",RegisterModuleCorpus.CorpusRemountTrackR1,
                RegisterModuleEngine.Engine1E,list,new int[][]{},ClassUnit.SupportTransport,1);

        Veteran = new UnitPattern("Vet",RegisterModuleSoldat.Veteran);

        Soldat = new UnitPattern("Sol",RegisterModuleSoldat.Soldat);

        Jaeger = new UnitPattern("Jeg",RegisterModuleSoldat.Jaeger);
//        this.time_spawn_soldat_max = 200;
//        functional.Add(RegisterFunctionalComponent.SoldatSpawn);

    }
}
