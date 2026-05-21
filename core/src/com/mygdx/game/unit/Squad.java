package com.mygdx.game.unit;

import com.mygdx.game.main.Main;
import com.mygdx.game.method.rand;

import java.util.ArrayList;

import static com.mygdx.game.main.Main.Ai;
import static com.mygdx.game.main.Main.BlockList2D;
import static com.mygdx.game.method.Method.detection_near_transport_i;
import static com.mygdx.game.method.pow2.pow2;
import static com.mygdx.game.unit.Unit.AIScan;
import static java.lang.StrictMath.abs;

public class Squad{
    public ArrayList<Unit>UnitSquad;
    public byte Team;
    public int[][]UnitSquadXY;
    public int x,y;
    public Unit EnemySquad;
    public ClassUnit classSquad;
    public static int MaxRadius = 12;
    public static int MaxQuantity = 4;

    public boolean AttackSquad,Max;
    public Squad(Unit unit){
        unit.ConfSquad = true;
        UnitSquad = new ArrayList<>();
        UnitSquad.add(unit);
        UnitSquadXY = new int[MaxQuantity][];
        unit.SquadAlly = this;
        x = unit.XMap;
        y = unit.YMap;
        classSquad = ClassUnit.Transport;
        Team = unit.team;
        UnitDetected();
        for(int i = 0;i<UnitSquad.size();i++){
            System.out.println(UnitSquad.get(i).ID);
        }
    }
    public void UnitDetected(){
        for (int i = 0; i < Main.UnitList.size(); i++) {
            Unit unit = Main.UnitList.get(i);
            if(unit != null) {
                if (abs(unit.XMap - x) < MaxRadius &
                        abs(unit.YMap - y) < MaxRadius &
                        Team == unit.team & !unit.ConfSquad & classSquad == unit.classUnit) {
                    UnitSquad.add(unit);
                    unit.SquadAlly = this;
                    unit.ConfSquad = true;
                    if(MaxQuantity==UnitSquad.size()){
                        Max = true;
                        return;
                    }
                }
            }
        }


    }
    public void SquadIteration() {
        if (!UnitSquad.isEmpty()) {
            EnemyDetected();
        } else {
            AI.SquadList.remove(this);
        }

    }
    public void EnemyDetected(){
        AttackSquad = false;
        if(EnemySquad!= null) {
            if (abs(x - EnemySquad.XMap)>20 & abs(y - EnemySquad.YMap)>20) {
                EnemySquad = null;
                for (int i = 0; i < UnitSquad.size(); i++) {
                    Unit unit = UnitSquad.get(i);
                    unit.TargetUnit = null;
                }

            }
        }
        for (int i = 0; i < UnitSquad.size(); i++) {
            Unit unit = UnitSquad.get(i);
            x = unit.XMap;
            y = unit.YMap;
            //Object[] sp = detection_near_transport(unit);
            Object[]sp = unit.less_hp_bot();
            unit.EnemyFire = detection_near_transport_i(unit);
            Unit unit2 = (Unit) sp[0];
            if(unit2 != null) {
                if (unit2.team != Team & EnemySquad == null) {
                    EnemySquad = (Unit) sp[0];
                    //if(null == Unit.findIntersection(unit.x,unit.y,EnemySquad.x,EnemySquad.y)){
                    AttackSquad = true;
                        //unit.Enemy = unit2;
                        //unit.RadiusTarget = (float) sp[1];
                    //}
                }
                else if(unit2.team == Team){
                    unit.TargetUnit = unit2;
                    unit.RadiusTarget = (float) sp[1];
                }

                //EnemyDetectedInit();

                //}
//                EnemyDetectedInit();
                //
                //return;

            } else if (unit.EnemyFire != null) {
                if(EnemySquad == null){
                    EnemySquad = unit.EnemyFire;
                    AttackSquad = true;
                }

            }
            //unit.AttackSquad =AttackSquad;
        }
        if(EnemySquad!= null) {
            EnemyDetectedInit();
        }
        for (int i2 = 0; i2 < UnitSquad.size(); i2++) {
            Unit unit = UnitSquad.get(i2);
            unit.AttackSquad = AttackSquad;
            if(!AttackSquad){
                unit.trigger_attack = false;
            }
        }
//        if(!AttackSquad){
//            if(UnitSquadXY[0]!= null) {
//                SquadPositionFait();
//            }
//        }
//        else{
//            Arrays.fill(UnitSquadXY, null);
//        }
        //EnemyDetectedInit();


    }
    public void SquadPositionFait(){
        for (int i = 0; i < UnitSquad.size(); i++) {
            Unit unit = UnitSquad.get(i);
            boolean t = true;
            while (t) {
                int x2 = -4 + rand.rand(8) + x;
                int y2 = -4 + rand.rand(8) + y;
                if(x2>0 & y2>0&x2<Main.xMap&y2<Main.yMap) {
                    if (!BlockList2D.get(y2).get(x2).passability) {
                        t = false;
                        UnitSquadXY[i] = new int[]{x2, y2};
                        Ai.pathAIAStar3(unit, unit.XMap, unit.YMap, x2, y2);
                    }
                }
            }
        }
    }
    public void EnemyDetectedInit(){
        //if(!UnitSquad.isEmpty()) {
            //Object[] sp = detection_near_transport(UnitSquad.get(0));
                for (int i = 0; i < UnitSquad.size(); i++) {
                    Unit unit = UnitSquad.get(i);
                    if(unit.TargetUnit == null){
                        unit.TargetUnit = EnemySquad;
                        //unit.EnemyFire = EnemySquad;
                        continue;
                    }
                    else if (unit.TargetUnit.team != Team) {
                        unit.TargetUnit = EnemySquad;
                        //unit.EnemyFire = EnemySquad;
                    }
                    //}

                }
    }
    public static void EnemyDetectedInit(Squad Squad){
        //if(!UnitSquad.isEmpty()) {
        //Object[] sp = detection_near_transport(UnitSquad.get(0));
        for (int i = 0; i < Squad.UnitSquad.size(); i++) {
            Unit unit = Squad.UnitSquad.get(i);
            //if (unit != null) {
            if (unit.TargetUnit.team != Squad.Team) {
                unit.TargetUnit = Squad.EnemySquad;
            }
            //}

        }
    }

        //}
        //else{
            //AI.SquadList.remove(this);
        //}
//        for(int i = 0; i < Main.UnitList.size(); i++){
//            Unit unit = Main.UnitList.get(i);
//            if(unit != null) {
//                if(unit.Enemy != null){
//                    EnemySquad = unit.Enemy;
//                }
//
//            }
//        }
    public void AttackUnit(){


    }
    public static void SquadCreate(){
        boolean n;
        for(int i = 0;i<Main.UnitList.size();i++){
            Unit unit = Main.UnitList.get(i);
            if(!unit.ConfSquad & ClassUnit.Transport == unit.classUnit) {
               n = false;
                for(int i2 = 0;i2<AI.SquadList.size();i2++) {
                    Squad squad = AI.SquadList.get(i2);
                    if(!squad.Max & unit.team == squad.Team){
                        squad.UnitSquad.add(unit);
                        unit.ConfSquad = true;
                        unit.SquadAlly = squad;
                        n = true;
                        if(Squad.MaxQuantity <= AI.SquadList.size()){
                            squad.Max = true;
                        }
                        break;
                    }
                }
                if(!n) {
                    AI.SquadList.add(new Squad(unit));
                }

            }
        }
    }



}
