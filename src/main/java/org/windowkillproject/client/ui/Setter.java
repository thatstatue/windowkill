package org.windowkillproject.client.ui;


import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

import javax.swing.*;

import static org.windowkillproject.Constants.*;
import static org.windowkillproject.client.ui.listeners.EpsilonKeyListener.*;
import static org.windowkillproject.client.ui.panels.etc.SettingsPanel.monoDialog;

public class Setter {
    public static void setDifficulty(String rate) {
        if (rate.equals("LOW")) {
            ENEMY_RADIUS = 25;
            BOUND = 4;
            MAX_ENEMY_SPEED = 3;
        } else if (rate.equals("HIGH")) {
            ENEMY_RADIUS = 15;
            BOUND = 6;
            MAX_ENEMY_SPEED = 4;
        } else if (rate.equals("MEDIUM")) {
            ENEMY_RADIUS = 20;
            BOUND = 5;
            MAX_ENEMY_SPEED = 4;
        } else {
            System.out.println("didn't get any rate for difficulty");
        }
    }

    public static int key;

    public static boolean setButton(int pressedKey, int code) { //TODO WHY PASS PRESSED KEY
        if (buttonIsUsed(code)) {
            JOptionPane.showMessageDialog(null,
                    "this key is already used");
            return false;
        } else {
            switch (key) {
                case UP_CODE -> {
                    UP_KEY = code;
                }
                case DOWN_CODE -> {
                    DOWN_KEY = code;
                }
                case LEFT_CODE -> {
                    LEFT_KEY = code;
                }
                case RIGHT_CODE -> {
                    RIGHT_KEY = code;
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
        if ( UP_KEY == pressedKey || DOWN_KEY == pressedKey
        || LEFT_KEY == pressedKey || RIGHT_KEY == pressedKey) return true;
        System.out.println();
        if (NativeKeyEvent.VC_SPACE == pressedKey || NativeKeyEvent.VC_SHIFT == pressedKey
        || NativeKeyEvent.VC_ESCAPE == pressedKey || NativeKeyEvent.VC_SLASH==pressedKey) return true;

        return false;
    }


}
