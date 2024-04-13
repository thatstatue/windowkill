package org.windowkillproject.application.listeners;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;
import org.windowkillproject.model.abilities.BulletModel;
import org.windowkillproject.model.entities.EpsilonModel;

import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import static org.windowkillproject.application.Application.gameFrame;
import static org.windowkillproject.model.abilities.BulletModel.bulletModels;

public class ShotgunMouseListener implements NativeMouseListener {
    public void nativeMouseClicked(NativeMouseEvent e) {

        Point2D mouseLoc = MouseInfo.getPointerInfo().getLocation();
        Point2D relativePoint = new Point2D.Double(mouseLoc.getX()-gameFrame.getX(), mouseLoc.getY()-gameFrame.getY());
        EpsilonModel epsilonModel = EpsilonModel.getINSTANCE();

        BulletModel bulletModel = new BulletModel(
                epsilonModel.getXO(), epsilonModel.getYO(), relativePoint);
        bulletModel.setShoot(true);
        bulletModel.move();
        bulletModel.move();
    }
    public void startListener() {
//        try {
//            GlobalScreen.registerNativeHook();
//        }
//        catch (NativeHookException ex) {
//            System.err.println("There was a problem registering the native hook.");
//            System.err.println(ex.getMessage());
//
//            // Terminate the program if registration failed.
//            System.exit(1);
//        }

        GlobalScreen.addNativeMouseListener(this);
    }

    public void stopListener() throws NativeHookException {
        GlobalScreen.unregisterNativeHook();
    }
}
