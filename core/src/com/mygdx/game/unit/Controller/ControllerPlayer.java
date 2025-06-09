package com.mygdx.game.unit.Controller;

import com.mygdx.game.main.Main;
import com.mygdx.game.method.Keyboard;
import com.mygdx.game.Inventory.InventoryInterface;
import com.mygdx.game.unit.Unit;

import static com.mygdx.game.main.ClientMain.Client;
import static com.mygdx.game.main.Main.*;
import static com.mygdx.game.method.Keyboard.LeftMouse;
import static com.mygdx.game.Inventory.InventoryInterface.InventoryConf;

public class ControllerPlayer extends Controller {
    public void ControllerIteration(Unit unit){
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
        for(Unit Tower : unit.tower_obj){
            Tower.left_mouse = LeftMouse;
            Tower.TargetX = unit.TargetX+Tower.tower_x;
            Tower.TargetY = unit.TargetY+Tower.tower_y;
        }
        if(Keyboard.PressE){
            inventoryMain = new InventoryInterface(unit.inventory,200,500,600,350);
            //Keyboard.PressE = false;
        }
        if(InventoryConf & LeftMouse){
            inventoryMain.InventoryUs(unit);
        }

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
        PacketClient.mouse_x = Keyboard.MouseX;
        PacketClient.mouse_y = Keyboard.MouseY;
        PacketClient.IDClient = IDClient;
        if(Keyboard.PressE){
            inventoryMain = new InventoryInterface(unit.inventory,200,500,600,350);
            //Keyboard.PressE = false;
        }
        if(InventoryConf & LeftMouse){
            inventoryMain.InventoryUsClient(unit);
        }
        Client.sendUDP(PacketClient);

    }
}
