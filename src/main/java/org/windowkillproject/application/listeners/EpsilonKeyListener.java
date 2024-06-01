package org.windowkillproject.application.listeners;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import org.windowkillproject.application.Config;
import org.windowkillproject.model.Writ;
import org.windowkillproject.model.entities.EpsilonModel;

import java.awt.event.KeyEvent;

import static org.windowkillproject.application.Application.*;
import static org.windowkillproject.application.Config.*;
import static org.windowkillproject.application.Setter.key;
import static org.windowkillproject.application.Setter.setButton;
import static org.windowkillproject.application.panels.SettingsPanel.monoDialog;
import static org.windowkillproject.controller.ElapsedTime.getTotalSeconds;

public class EpsilonKeyListener implements NativeKeyListener {
    public static boolean changingButtons;
    public static boolean isLeftPressed;
    public static boolean isRightPressed;
    public static boolean isUpPressed;
    public static boolean isDownPressed;
    private static boolean started;

    public static boolean isStarted() {
        return started;
    }

    public static int UP_KEY = NativeKeyEvent.VC_UP;
    public static String UP_KEY_NAME = "UP";
    public static String DOWN_KEY_NAME = "DOWN";
    public static String LEFT_KEY_NAME = "LEFT";
    public static String RIGHT_KEY_NAME = "RIGHT";
    public static int DOWN_KEY = NativeKeyEvent.VC_DOWN;
    public static int LEFT_KEY = NativeKeyEvent.VC_LEFT;
    public static int RIGHT_KEY = NativeKeyEvent.VC_RIGHT;

    public void startListener() {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            // Terminate the program if registration failed.
            System.exit(1);
        }

        GlobalScreen.addNativeKeyListener(this);
        started = true;
    }

    public void stopListener() throws NativeHookException {
        GlobalScreen.removeNativeKeyListener(this);
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        if (getGameFrame().isVisible()) {
            int keyCode = e.getKeyCode();
            if (keyCode == LEFT_KEY) {
                isLeftPressed = true;
            } else if (keyCode == RIGHT_KEY) {
                isRightPressed = true;
            } else if (keyCode == UP_KEY) {
                isUpPressed = true;
            } else if (keyCode == DOWN_KEY) {
                isDownPressed = true;
            }
        }

    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        int keyCode = e.getKeyCode();
        try {
            Thread.sleep(Config.SENSITIVITY_RATE);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        if(getGameFrame().isVisible() || getShopFrame().isVisible()) {
            switch (keyCode) {
                case NativeKeyEvent.VC_SPACE -> initShFrame();
                case NativeKeyEvent.VC_ESCAPE -> hideShFrame();
                case NativeKeyEvent.VC_SLASH -> {
                    if (EpsilonModel.getINSTANCE().getXp() >= 100) {
                        if (Writ.getChosenSkill() != null) {
                            long deltaT = getTotalSeconds() - Writ.getInitSeconds();
                            if (deltaT <= 0 || deltaT >= WRIT_COOL_DOWN_SECONDS) {
                                Writ.setInitSeconds();
                                Writ.acceptedClicksAddIncrement();
                            }
                        }
                    }
                }
                default -> {
                    if (keyCode == LEFT_KEY) isLeftPressed = false;
                    if (keyCode == RIGHT_KEY) isRightPressed = false;
                    if (keyCode == UP_KEY) isUpPressed = false;
                    if (keyCode == DOWN_KEY) isDownPressed = false;
                }
            }
        }else{
            if( keyCode == NativeKeyEvent.VC_ESCAPE) {
                changingButtons = false;
                monoDialog.dispose();
            }
            else if (changingButtons) {
                if (setButton(e.getRawCode(), e.getKeyCode())) {
                    switch (key) {
                        case UP_CODE -> UP_KEY_NAME = KeyEvent.getKeyText(e.getRawCode());
                        case DOWN_CODE -> DOWN_KEY_NAME = KeyEvent.getKeyText(e.getRawCode());
                        case LEFT_CODE -> LEFT_KEY_NAME = KeyEvent.getKeyText(e.getRawCode());
                        case RIGHT_CODE -> RIGHT_KEY_NAME = KeyEvent.getKeyText(e.getRawCode());
                    }
                    changingButtons = false;
                }
            }
        }
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {


    }
}