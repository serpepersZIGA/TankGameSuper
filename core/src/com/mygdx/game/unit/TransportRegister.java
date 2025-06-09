package com.mygdx.game.unit;

import com.mygdx.game.main.PacketUnitUpdate;
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
        PlayerCannonFlameA1 = new UnitPattern("PanzFlA1",
                RegisterModuleCorpus.CorpusT1, RegisterModuleEngine.Engine1E,list,new int[][]{{-12,52},{12,52},{1,18}},
                ClassUnit.Transport,0);

        list = new ArrayList<>();
        list.add(RegisterModuleCannon.CannonFlameAuxiliary);
        list.add(RegisterModuleCannon.CannonFlameAuxiliary);
        list.add(RegisterModuleCannon.CannonMortar);
        PlayerCannonMortarA1 = new UnitPattern("PanzMortA1",
                RegisterModuleCorpus.CorpusT1, RegisterModuleEngine.Engine1E,list,new int[][]{{-12,52},{12,52},{1,18}},
                ClassUnit.Transport,0);

        PlayerCannonFlameA2 = new UnitPattern("PanzFlA2",
                RegisterModuleCorpus.CorpusT1, RegisterModuleEngine.Engine2E,list,new int[][]{{-12,52},{12,52},{1,18}},
                ClassUnit.Transport,0);

        list = new ArrayList<>();
        list.add(RegisterModuleCannon.CannonMachineGunAuxiliary);
        list.add(RegisterModuleCannon.CannonMachineGunAuxiliary);
        list.add(RegisterModuleCannon.CannonMachineGun);
        PlayerCannonMachineGunA1 = new UnitPattern("PanzMachGunA1",
                RegisterModuleCorpus.CorpusT1, RegisterModuleEngine.Engine1E,list,new int[][]{{-12,52},{12,52},{1,18}}
        ,ClassUnit.Transport,0);

        list = new ArrayList<>();
        list.add(RegisterModuleCannon.CannonMachineGunAuxiliary);
        list.add(RegisterModuleCannon.CannonMachineGunAuxiliary);
        list.add(RegisterModuleCannon.CannonAcid);
        PlayerCannonAcidA1 = new UnitPattern("PanzAcA1",
                RegisterModuleCorpus.CorpusT1, RegisterModuleEngine.Engine3E,list,new int[][]{{-12,52},{12,52},{1,18}},
                ClassUnit.Transport,0);

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
