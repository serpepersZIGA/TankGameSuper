package com.mygdx.game.unit.Controller;

import com.mygdx.game.main.Main;
import com.mygdx.game.method.Keyboard;
import com.mygdx.game.unit.Unit;

import static com.mygdx.game.main.ClientMain.Client;
import static com.mygdx.game.main.Main.*;
import static com.mygdx.game.method.Keyboard.LeftMouse;

public class ControllerPlayer extends Controller {
    public void ControllerIteration(Unit unit){
//        for (ArrayList<Block> i2:Main.BlockList2D){
//            for (Block i3:i2){
//                i3.render_block = UpdateRegister.GrassUpdate;
//            }
//        }
//
//        Main.BlockList2D.get(unit.YMap).get(unit.XMap).render_block = UpdateRegister.Update3;



        unit.left_mouse = LeftMouse;
        unit.right_mouse = Keyboard.RightMouse;
        unit.press_w = Keyboard.PressW;
        unit.press_a = Keyboard.PressA;
        unit.press_s = Keyboard.PressS;
        unit.press_d = Keyboard.PressD;
        unit.press_f = Keyboard.PressF;
        unit.TargetX = Keyboard.MouseX- RC.width_2;
        unit.TargetY = Keyboard.MouseY- RC.height_2;
        Main.RC.x = unit.tower_x;
        Main.RC.y = unit.tower_y;
        // every tower used to get left_mouse only, so on a tank with two
        // guns both fired from the left button and the right button did
        // nothing for towers at all - the first tower now fires on the
        // left button, every other tower on the right one
        int towerIndex = 0;
        for(Unit Tower : unit.TowerUnitList){
            if (towerIndex == 0) {
                Tower.left_mouse = LeftMouse;
                Tower.right_mouse = false;
            } else {
                Tower.left_mouse = false;
                Tower.right_mouse = Keyboard.RightMouse;
            }
            Tower.TargetX = unit.TargetX+Tower.tower_x;
            Tower.TargetY = unit.TargetY+Tower.tower_y;
            towerIndex++;
        }
//        if(Keyboard.PressE){
//            inventoryMain = new InventoryInterface(unit.inventory,200,500,600,350);
////            //Keyboard.PressE = false;
//        }
        // using an item is now handled directly by com.mygdx.game.ui.InventoryOverlay's
        // own Scene2D click listener on each slot - this used to fire
        // InventoryUs() for ANY left click anywhere while the inventory was
        // open (using the OLD, no-longer-rendered slot positions), which
        // could "use" an item just from clicking somewhere in the game
        // world with the inventory open in the background

    }
    public void ControllerIterationClientAnHost(Unit unit){
//        for (Packet_client pack : Clients) {
//            if (pack != null) {
//                if (pack.IDClient == unit.nConnect) {
//                    unit.left_mouse = pack.left_mouse;
//                    unit.right_mouse = pack.right_mouse;
//                    unit.press_w = pack.press_w;
//                    unit.press_a = pack.press_a;
//                    unit.press_s = pack.press_s;
//                    unit.press_d = pack.press_d;
//                    unit.TargetX = pack.mouse_x;
//                    unit.TargetY = pack.mouse_y;
//                    unit.TowerControlPlayerClient();
//                    //unit.FireControl();
//                    for (Unit Tower : unit.tower_obj) {
//                        Tower.left_mouse = pack.left_mouse;
//                        Tower.TargetX = pack.mouse_x;
//                        Tower.TargetY = pack.mouse_y;
//                        Tower.TowerControlPlayerClient();
//                        Tower.FireControl();
//                    }
//                    return;
//                }
//            }
//        }
    }
    public void ControllerIterationClientAnClient(Unit unit){
        //System.out.println(unit.tower_x+" "+unit.tower_y);
        Main.RC.x = unit.tower_x;
        Main.RC.y = unit.tower_y;
        PacketClient.press_w = Keyboard.PressW;
        PacketClient.press_a = Keyboard.PressA;
        PacketClient.press_s = Keyboard.PressS;
        PacketClient.press_d = Keyboard.PressD;
        PacketClient.press_f = Keyboard.PressF;
        PacketClient.left_mouse = Keyboard.LeftMouse;
        PacketClient.right_mouse = Keyboard.RightMouse;
        PacketClient.mouse_x = (int) (Keyboard.MouseX-RC.width_2);
        PacketClient.mouse_y = (int) (Keyboard.MouseY-RC.height_2);
        PacketClient.IDClient = IDClient;
        //if(Keyboard.PressE){
            //inventoryMain.InventoryConf = new InventoryInterface(unit.inventory,200,500,600,350);
            //Keyboard.PressE = false;
        //}
        //if(Keyboard.PressZ){
           // equipmentMain = new EquipmentInterface(unit.equipment);
        //}
        // see the comment in ControllerIteration() - InventoryOverlay's own
        // click listener handles item use now
        Client.sendUDP(PacketClient);

    }
}
