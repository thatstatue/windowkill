package org.windowkillproject.application;


import javax.swing.*;

import static org.windowkillproject.application.listeners.EpsilonKeyListener.*;
import static org.windowkillproject.application.panels.SettingsPanel.monoDialog;

public class Setter {
    public static void setDifficulty(String rate) {
        if (rate.equals("LOW")) {
            Config.ENEMY_RADIUS = 25;
            Config.BOUND = 4;
            Config.MAX_ENEMY_SPEED = 3;
        } else if (rate.equals("HIGH")) {
            Config.ENEMY_RADIUS = 15;
            Config.BOUND = 6;
            Config.MAX_ENEMY_SPEED = 4;
        } else if (rate.equals("MEDIUM")) {
            Config.ENEMY_RADIUS = 20;
            Config.BOUND = 5;
            Config.MAX_ENEMY_SPEED = 4;
        } else {
            System.out.println("didn't get any rate for difficulty");
        }
    }

    public static int key;

    public static boolean setButton(int pressedKey) {
        if (buttonIsUsed(pressedKey)) {
            JOptionPane.showMessageDialog(null,
                    "this key is already used");
            return false;
        } else {
            switch (key) {
                case Config.UP_CODE -> {
                    UP_KEY = pressedKey;
                }
                case Config.DOWN_CODE -> {
                    DOWN_KEY = pressedKey;
                }
                case Config.LEFT_CODE -> {
                    LEFT_KEY = pressedKey;
                }
                case Config.RIGHT_CODE -> {
                    RIGHT_KEY = pressedKey;
                }
                default -> throw new IllegalStateException("Unexpected value: " + key);
            }
            JOptionPane.showMessageDialog(null, "done!");
            changingButtons = false;
            monoDialog.dispose();
            return true;
        }
    }

    private static boolean buttonIsUsed(int pressedKey) {
//        if ( UP_KEY == pressedKey){
//            System.out.println("uppp");
//        }
//        if( DOWN_KEY == pressedKey){
//            System.out.println("doown");
//        }
//        if (LEFT_KEY == pressedKey){
//            System.out.println("leeft");
//        }
//        if (RIGHT_KEY == pressedKey)
//            System.out.println("righht");
//        if (NativeKeyEvent.VC_ESCAPE == pressedKey)
//            System.out.println("esc");
//        if(NativeKeyEvent.VC_SPACE == pressedKey)
//            System.out.println("spc");
//        if (NativeKeyEvent.VC_SHIFT == pressedKey)
//            System.out.println("shft");
        return false;
    }


}
