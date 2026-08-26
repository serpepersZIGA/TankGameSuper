package com.mygdx.game.unit.Controller;

public class RegisterController {
    public Controller controllerBot,controllerPlayer,controllerTowerBot,controllerTowerPlayer,controllerBotSupport,
    controllerSoldatTransport,controllerSoldatBot,controllerHelicopter,controllerVoid;
    public RegisterController(){
        controllerVoid = new ControllerVoid();
        controllerHelicopter = new ControllerHelicopterBot();
        controllerSoldatBot = new ControllerSoldatBot();
        controllerBot = new ControllerBot();
        controllerPlayer = new ControllerPlayer();
        controllerTowerBot = new ControllerTowerBot();
        controllerTowerPlayer = new ControllerTowerPlayer();
        controllerBotSupport = new ControllerBotSupport();
        controllerSoldatTransport = new ControllerSoldatTransport();
    }
}
