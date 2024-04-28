package org.windowkillproject.application.listeners;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import org.windowkillproject.application.Config;
import org.windowkillproject.model.Writ;
import org.windowkillproject.model.entities.EpsilonModel;

import static org.windowkillproject.application.Application.*;
import static org.windowkillproject.application.Config.WRIT_COOL_DOWN;
import static org.windowkillproject.controller.ElapsedTime.getTotalSeconds;

public class EpsilonKeyListener implements NativeKeyListener {
    public static boolean isLeftPressed;
    public static boolean isRightPressed;
    public static boolean isUpPressed;
    public static boolean isDownPressed;
    private static boolean started;

    public static boolean isStarted() {
        return started;
    }

    private int UP_KEY =  NativeKeyEvent.VC_UP ;
    private int DOWN_KEY =  NativeKeyEvent.VC_DOWN ;
    private int LEFT_KEY =  NativeKeyEvent.VC_LEFT ;
    private int RIGHT_KEY =  NativeKeyEvent.VC_RIGHT ;

//todo listeners object
    //todo restart has bugs
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
        started = true;
    }

    public void stopListener() throws NativeHookException {
        GlobalScreen.removeNativeKeyListener(this);
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
        try {
            Thread.sleep(Config.SENSITIVITY_RATE);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        switch (keyCode){
            case NativeKeyEvent.VC_LEFT -> isLeftPressed = false;
            case NativeKeyEvent.VC_RIGHT -> isRightPressed = false;
            case NativeKeyEvent.VC_UP ->  isUpPressed = false;
            case NativeKeyEvent.VC_DOWN -> isDownPressed = false;
            case NativeKeyEvent.VC_SPACE -> initShFrame();
            case NativeKeyEvent.VC_ESCAPE -> hideShFrame();
            case NativeKeyEvent.VC_ALT -> {
                System.out.println("clicked");
                if (EpsilonModel.getINSTANCE().getXp()>= 100){
                    if (Writ.getChosenSkill()!=null){
                        long deltaT =getTotalSeconds() - Writ.getInitSeconds();
                        if (deltaT <=0 || deltaT >= WRIT_COOL_DOWN ) {
                            Writ.setInitSeconds();
                            Writ.acceptedClicksAddIncrement();
                        }
                        else System.out.println("time prob");
                    }
                    else System.out.println("writ null");
                }else System.out.println("low xp");
            }
        }

    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeEvent) {

    }
}