package org.windowkillproject.client.ui.listeners;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import org.windowkillproject.client.GameClient;
import org.windowkillproject.client.ui.Setter;

import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;

import static org.windowkillproject.Constants.*;
import static org.windowkillproject.Request.REQ_WRIT_INIT;
import static org.windowkillproject.client.ui.panels.etc.SettingsPanel.monoDialog;

public class EpsilonKeyListener implements NativeKeyListener {
    public EpsilonKeyListener(GameClient client) {
        super();
        this.client = client;
    }

    private final GameClient client;
    private int SENSITIVITY_RATE = 0;


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
        if (client.getApp().getGameFrame().isVisible() /*&& !SmileyHeadModel.isAppearing()*/) {
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
            Thread.sleep(SENSITIVITY_RATE);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        if ((client.getApp().getGameFrame().isVisible() || client.getApp().getShopFrame().isVisible()) /*&& !smileyHeadModel.isAppearing()*/) {
            switch (keyCode) {
                case NativeKeyEvent.VC_SPACE -> client.getApp().initShFrame();
                case NativeKeyEvent.VC_ESCAPE -> client.getApp().hideShFrame();
                case NativeKeyEvent.VC_SLASH -> client.sendMessage(REQ_WRIT_INIT);

                default -> {
                    if (keyCode == LEFT_KEY) isLeftPressed = false;
                    if (keyCode == RIGHT_KEY) isRightPressed = false;
                    if (keyCode == UP_KEY) isUpPressed = false;
                    if (keyCode == DOWN_KEY) isDownPressed = false;
                }
            }
        } else {
            if (keyCode == NativeKeyEvent.VC_ESCAPE) {
                changingButtons = false;
                monoDialog.dispose();
            } else if (changingButtons) {
                if (Setter.setButton(e.getRawCode(), e.getKeyCode())) {
                    switch (Setter.key) {
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