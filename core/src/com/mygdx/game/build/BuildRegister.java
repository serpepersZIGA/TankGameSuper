package com.mygdx.game.build;

import com.mygdx.game.FunctionalComponent.FunctionalBuilding.FunctionalComponentBuildingRegister;
import com.mygdx.game.FunctionalComponent.FunctionalList;

import java.util.ArrayList;

import static com.mygdx.game.FunctionalComponent.FunctionalBuilding.FunctionalComponentBuildingRegister.FlameBuild;

public class BuildRegister {
    public static ArrayList<BuildPacket>PacketBuilding = new ArrayList<>();
    public static ArrayList<Object[]> BuildingID = new ArrayList<>();
    public static void Create(){
        boolean [][] ConstructBuilding = new boolean[][]{
                {true,true,true,true,false,false,true,true,true,true},
                {true,true,true,true,false,false,true,true,true,true},
                {true,true,true,true,false,false,true,true,true,true},
                {true,true,true,true,false,false,true,true,true,true},
                {true,true,true,true,false,false,true,true,true,true},
                {true,true,true,true,false,false,true,true,true,true}
        };
        FunctionalComponentBuildingRegister.FunctionalComponentBuildingRegisters();
        FunctionalList func = new FunctionalList();
        func.Add(FlameBuild);
        BuildingID.add(new Object[]{new Building(0,0,"BigBuildingWood1",ConstructBuilding,
                "big_build_wood_1",func),"BigBuildingWood1"});
        //Building building = (Building)BuildingID.get(0)[0];
        //building.ListFunc.Add(FlameBuild);

    }
}
