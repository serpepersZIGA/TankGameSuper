package com.mygdx.game.Parsing;

import com.mygdx.game.unit.Fire.FireRegister;

import static com.mygdx.game.FunctionalComponent.FunctionalUnit.FunctionalComponentUnitRegister.*;

public class Parser {
    public static Object[] BooleanPars(String TxT,int u){
        String TotalTxT = "";
        boolean Conf;
        //int XMax = xy[0];
        //int YMax = xy[1];

        for (int i = u+1;i < TxT.length(); i++) {
            char c = TxT.charAt(i);
            if(c ==';'){
                if(TotalTxT.equals("true")){
                    return new Object[]{true,i};
                }
                else if(TotalTxT.equals("false")){
                    return new Object[]{false,i};
                }
                else {
                    throw new RuntimeException();
                }
            }
            else if (c != '\n' &  c != ' '& c !='{'& c !='}'& c !='='){
                TotalTxT = TotalTxT + c;
            }

        }
        throw new RuntimeException();
    }
    public static Object[] FloatPars(String TxT,int u){
        String TotalTxT = "";
        boolean Conf;
        //int XMax = xy[0];
        //int YMax = xy[1];

        for (int i = u+1;i < TxT.length(); i++) {
            char c = TxT.charAt(i);
            if(c ==';'){
                return new Object[]{Float.valueOf(TotalTxT.trim()),i};
            }
            else if (c != '\n' &  c != ' '& c !='{'& c !='}'& c !='='){
                TotalTxT = TotalTxT + c;
            }

        }
        throw new RuntimeException();
    }
    public static Object[] IntegerPars(String TxT,int u){
        String TotalTxT = "";
        boolean Conf;
        //int XMax = xy[0];
        //int YMax = xy[1];

        for (int i = u+1;i < TxT.length(); i++) {
            char c = TxT.charAt(i);
            if(c ==';'){
                return new Object[]{Integer.valueOf(TotalTxT.trim()),i};
            }
            else if (c != '\n' &  c != ' '& c !='{'& c !='}'& c !='='){
                TotalTxT = TotalTxT + c;
            }

        }
        throw new RuntimeException();
    }
    public static Object[] TextPars(String TxT,int u){
        String TotalTxT = "";
        boolean Conf;
        //int XMax = xy[0];
        //int YMax = xy[1];

        for (int i = u+1;i < TxT.length(); i++) {
            char c = TxT.charAt(i);
            if(c ==';'){
                return new Object[]{TotalTxT.trim(),i};
            }
            else if (c != '\n' &  c != ' '& c !='{'& c !='}'& c !='='){
                TotalTxT = TotalTxT + c;
            }

        }
        throw new RuntimeException();
    }
    public static Object[] ComponentParsing(String TxT,int u){
        Object[]n = TextPars(TxT,u);

        switch (((String) n[0]).trim()){
            case "ComponentBuildingCollision":
                return new Object[]{BuildCollision,n[1]};
            case "ComponentFireControl":
                return new Object[]{FireControl,n[1]};
            case "ComponentHill":
                return new Object[]{Hill,n[1]};
            case "ComponentMotorControl":
                return new Object[]{MotorControl,n[1]};
            case "ComponentMoveDebris":
                return new Object[]{MoveDebris,n[1]};
            case "ComponentSoldatControl":
                return new Object[]{SoldatControl,n[1]};
            case "ComponentSoldatCorrect":
                return new Object[]{SoldatCorrect,n[1]};
            case "ComponentSoldatSpawn":
                return new Object[]{SoldatSpawn,n[1]};
            case "ComponentTowerIteration":
                return new Object[]{TowerIteration,n[1]};
            case "ComponentTowerControl":
                return new Object[]{TowerControl,n[1]};
            case "ComponentTowerXY":
                return new Object[]{TowerXY,n[1]};
            case "ComponentWorkBlade":
                return new Object[]{WorkBlade,n[1]};

        }
        throw new RuntimeException();
    }
    public static Object[] FireParsing(String TxT,int u){
        Object[]n = TextPars(TxT,u);
        //System.out.println((String) n[0]);

        switch (((String) n[0]).trim()){
            case "Flame":
                return new Object[]{FireRegister.FireFlame,n[1]};
            case "Acid":
                return new Object[]{FireRegister.FireAcid,n[1]};
            case "Bullet":
                return new Object[]{FireRegister.FireBullet,n[1]};
            case "Mortar":
                return new Object[]{FireRegister.FireMortar,n[1]};

        }
        throw new RuntimeException();
    }
}
