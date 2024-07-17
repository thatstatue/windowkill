package org.windowkillproject.application.listeners;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;
import org.windowkillproject.model.Wave;
import org.windowkillproject.model.abilities.BulletModel;
import org.windowkillproject.model.entities.EpsilonModel;
import org.windowkillproject.model.entities.enemies.finalboss.SmileyHeadModel;

import java.awt.*;
import java.awt.geom.Point2D;

import static org.windowkillproject.application.Application.getGameFrame;
import static org.windowkillproject.controller.ElapsedTime.getTotalSeconds;
import static org.windowkillproject.controller.Utils.unitVector;
import static org.windowkillproject.controller.Utils.weighedVector;

public class ShotgunMouseListener implements NativeMouseListener {
    public static long empowerInitSeconds = Long.MAX_VALUE;

    public void nativeMouseClicked(NativeMouseEvent e) {
        if (getGameFrame().isVisible()&& !SmileyHeadModel.isAppearing()) {
            Point2D mouseLoc = MouseInfo.getPointerInfo().getLocation();

            EpsilonModel epsilonModel = EpsilonModel.getINSTANCE();

            BulletModel bulletModel = new BulletModel(
                    epsilonModel.getXO(), epsilonModel.getYO(), mouseLoc);
            bulletModel.shoot();
            long deltaT = getTotalSeconds() - empowerInitSeconds;
            if (deltaT > 0 && deltaT <= 10) { //doesn't allow too many bullets
                empowerBullets(epsilonModel, mouseLoc);
            }

        }
    }

    private static void empowerBullets(EpsilonModel epsilonModel, Point2D relativePoint) {
        Point2D point = weighedVector(unitVector(epsilonModel.getAnchor(), relativePoint), 10);
        var extraBullet1 = new BulletModel(
                (int) (epsilonModel.getXO() + point.getX()),
                (int) (epsilonModel.getYO() + point.getY()), relativePoint);
        point = weighedVector(point, 2);
        var extraBullet2 = new BulletModel(
                (int) (epsilonModel.getXO() + point.getX()),
                (int) (epsilonModel.getYO() + point.getY()), relativePoint);
        extraBullet1.shoot();
        extraBullet2.shoot();
    }

    private static boolean started;

    public static boolean isStarted() {
        return started;
    }

    public void startListener() {
        GlobalScreen.addNativeMouseListener(this);
        started = true;
    }

}
