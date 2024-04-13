package org.windowkillproject.application.listeners;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import org.windowkillproject.application.Config;
import org.windowkillproject.model.entities.EpsilonModel;

import static org.windowkillproject.application.Application.gameFrame;

public class EpsilonKeyListener implements NativeKeyListener {
    static boolean isLeftPressed, isRightPressed, isUpPressed, isDownPressed;
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
        switch (e.getKeyCode()) {
            case NativeKeyEvent.VC_LEFT -> isLeftPressed = true;
            case NativeKeyEvent.VC_RIGHT -> isRightPressed = true;
            case NativeKeyEvent.VC_UP -> isUpPressed = true;
            case NativeKeyEvent.VC_DOWN -> isDownPressed = true;
            default -> System.out.print("");//todo: update, width height rate
        }
        EpsilonModel eM = EpsilonModel.getINSTANCE();

        int endX = eM.getWidth() + eM.getX() + eM.getRadius();
        int endY = eM.getHeight() + eM.getY() + 3* eM.getRadius();
        if (!((isLeftPressed && isRightPressed) || (isDownPressed && isUpPressed))) {
            if (isUpPressed && eM.getY() - Config.EPSILON_SPEED >= 0)
                eM.move(0,-Config.EPSILON_SPEED);
            else if (isDownPressed && endY + Config.EPSILON_SPEED <= gameFrame.getHeight())
                eM.move(0,Config.EPSILON_SPEED);
            if (isLeftPressed && eM.getX() - Config.EPSILON_SPEED >= 0)
                eM.move(-Config.EPSILON_SPEED,0);
            else if (isRightPressed && endX + Config.EPSILON_SPEED <= gameFrame.getWidth())
                eM.move(Config.EPSILON_SPEED,0);

        }
    }

    @Override
     public void nativeKeyReleased(NativeKeyEvent e) {
        switch (e.getKeyCode()) {
            case NativeKeyEvent.VC_LEFT -> isLeftPressed = false;
            case NativeKeyEvent.VC_RIGHT -> isRightPressed = false;
            case NativeKeyEvent.VC_UP -> isUpPressed = false;
            case NativeKeyEvent.VC_DOWN -> isDownPressed = false;
            default -> System.out.print("");//todo: update
        }
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeEvent) {
        if (nativeEvent.getKeyCode() == NativeKeyEvent.VC_SPACE){
//todo
        }
    }
}