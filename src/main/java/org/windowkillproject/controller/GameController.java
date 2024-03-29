package org.windowkillproject.controller;

import org.windowkillproject.application.Config;

import javax.swing.*;



public class GameController {
    public static Timer gameTimer;
    public void start(){
        gameTimer = new Timer(Config.DELAY, e -> {

        }
        );
        gameTimer.start();
    }
}
