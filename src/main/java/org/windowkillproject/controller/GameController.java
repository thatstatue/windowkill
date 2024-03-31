package org.windowkillproject.controller;

import org.windowkillproject.application.Config;
import org.windowkillproject.application.frames.GamePanel;
import org.windowkillproject.model.entities.Entity;

import javax.swing.*;
import java.util.ArrayList;


public class GameController {
    private static ArrayList<GamePanel> gamePanels;
    public static Timer gameTimer;
    public static void init(ArrayList<GamePanel> gamePanels){
        GameController.gamePanels = gamePanels;
    }
    public static void start(){
        gameTimer = new Timer(Config.DELAY, e -> {
            for (int i = 0 ; i< gamePanels.size(); i++) {
                GamePanel gp = gamePanels.get(i);
                for (Entity entity : gp.getEntities()) {
                    entity.rotate();
                }
                gp.repaint();
            }
        }
        );
        gameTimer.start();
    }

    private void wave(){

    }
}
