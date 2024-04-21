package org.windowkillproject.application.listeners;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import org.windowkillproject.application.Config;
import org.windowkillproject.application.frames.ShopFrame;
import org.windowkillproject.model.entities.EpsilonModel;

import static org.windowkillproject.application.Application.*;
import static org.windowkillproject.controller.Update.frameUpdateTimer;
import static org.windowkillproject.controller.Update.modelUpdateTimer;

public class EpsilonKeyListener implements NativeKeyListener {
    public static boolean isLeftPressed;
    public static boolean isRightPressed;
    public static boolean isUpPressed;
    public static boolean isDownPressed;
    private int UP_KEY =  NativeKeyEvent.VC_UP ;
    private int DOWN_KEY =  NativeKeyEvent.VC_DOWN ;
    private int LEFT_KEY =  NativeKeyEvent.VC_LEFT ;
    private int RIGHT_KEY =  NativeKeyEvent.VC_RIGHT ;


    public void startListener() {
        try {
            GlobalScreen.registerNativeHook();
        }
        catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            // Terminate the program if registration failed.
            System.exit(1);
        }

        GlobalScreen.addNativeKeyListener(this);
    }

    public void stopListener() throws NativeHookException {
        GlobalScreen.unregisterNativeHook();
    }
    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == NativeKeyEvent.VC_LEFT) {
            isLeftPressed = true;
        } else if (keyCode == NativeKeyEvent.VC_RIGHT) {
            isRightPressed = true;
        } else if (keyCode == NativeKeyEvent.VC_UP) {
            isUpPressed = true;
        } else if (keyCode == NativeKeyEvent.VC_DOWN) {
            isDownPressed = true;
        }

    }

    @Override
     public void nativeKeyReleased(NativeKeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == NativeKeyEvent.VC_LEFT) {
            isLeftPressed = false;
        } else if (keyCode == NativeKeyEvent.VC_RIGHT) {
            isRightPressed = false;
        } else if (keyCode == NativeKeyEvent.VC_UP) {
            isUpPressed = false;
        } else if (keyCode == NativeKeyEvent.VC_DOWN) {
            isDownPressed = false;
        }
        else if (e.getKeyCode() == NativeKeyEvent.VC_SPACE){
            initShFrame();
//todo
        }else if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE){
            hideShFrame();
        }
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeEvent) {

    }
}