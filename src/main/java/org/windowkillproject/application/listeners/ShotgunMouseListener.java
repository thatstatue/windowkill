package org.windowkillproject.application.listeners;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;
import org.windowkillproject.model.abilities.BulletModel;
import org.windowkillproject.model.entities.EpsilonModel;

import java.awt.*;
import java.awt.geom.Point2D;

import static org.windowkillproject.application.Application.getGameFrame;
import static org.windowkillproject.controller.ElapsedTime.getTotalSeconds;
import static org.windowkillproject.controller.Utils.unitVector;
import static org.windowkillproject.controller.Utils.weighedVector;

public class ShotgunMouseListener implements NativeMouseListener {
    public static long empowerInitSeconds = Long.MAX_VALUE;
    public void nativeMouseClicked(NativeMouseEvent e) {

        Point2D mouseLoc = MouseInfo.getPointerInfo().getLocation();
        Point2D relativePoint = new Point2D.Double(mouseLoc.getX()-getGameFrame().getX(), mouseLoc.getY()-getGameFrame().getY());
        EpsilonModel epsilonModel = EpsilonModel.getINSTANCE();

        BulletModel bulletModel = new BulletModel(
                epsilonModel.getXO(), epsilonModel.getYO(), relativePoint);
        long deltaT =getTotalSeconds() - empowerInitSeconds ;
        if ( deltaT>0 && deltaT<= 10){
            Point2D point = weighedVector(unitVector(epsilonModel.getAnchor(),relativePoint),10);
            var extraBullet1 = new BulletModel(
                    (int) (epsilonModel.getXO()+point.getX()),
                    (int) (epsilonModel.getYO()+point.getY()), relativePoint);
            point = weighedVector(point,2);
            var extraBullet2 = new BulletModel(
                    (int) (epsilonModel.getXO()+point.getX()),
                    (int) (epsilonModel.getYO()+point.getY()), relativePoint);
            extraBullet1.shot();
            extraBullet2.shot();
        }
        bulletModel.shot();
    }

    public void startListener() {
        GlobalScreen.addNativeMouseListener(this);
    }

    public void stopListener() throws NativeHookException {
        GlobalScreen.unregisterNativeHook();
    }
}
