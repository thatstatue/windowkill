package org.windowkillproject.client.ui.listeners;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;
import org.windowkillproject.client.GameClient;
import org.windowkillproject.server.model.abilities.BulletModel;
import org.windowkillproject.server.model.entities.enemies.finalboss.SmileyHeadModel;

import java.awt.*;
import java.awt.geom.Point2D;

import static org.windowkillproject.Request.REQ_EPSILON_ANCHOR;
import static org.windowkillproject.controller.ElapsedTime.getTotalSeconds;
import static org.windowkillproject.controller.Utils.unitVector;
import static org.windowkillproject.controller.Utils.weighedVector;

public class ShotgunMouseListener implements NativeMouseListener {
    public static long empowerInitSeconds = Long.MAX_VALUE;
    public static boolean slaughter;
    private final GameClient client;

    public ShotgunMouseListener(GameClient client) {
        super();
        this.client = client;
    }
    private Point2D epsilonAnchor = new Point2D.Float(0,0);

    public void setEpsilonAnchor(Point2D epsilonAnchor) {
        this.epsilonAnchor = epsilonAnchor;
    }

    public void nativeMouseClicked(NativeMouseEvent e) {
        if (client.getApp().getGameFrame().isVisible()&& !SmileyHeadModel.isAppearing()) {
            Point2D mouseLoc = MouseInfo.getPointerInfo().getLocation();

            client.sendMessage(REQ_EPSILON_ANCHOR);

            BulletModel bulletModel = new BulletModel(
                    (int) epsilonAnchor.getX(), (int) epsilonAnchor.getY(), mouseLoc);
            bulletModel.shoot();
            long deltaT = getTotalSeconds() - empowerInitSeconds;
            if (deltaT > 0 && deltaT <= 10) { //doesn't allow too many bullets
                empowerBullets(epsilonAnchor, mouseLoc);
            }
            if (slaughter) {
                bulletModel.setSlaughter(true);
                slaughter = false;
            }
        }
    }

    private static void empowerBullets(Point2D epsilonAnchor, Point2D relativePoint) {
        Point2D point = weighedVector(unitVector(epsilonAnchor, relativePoint), 10);
        var extraBullet1 = new BulletModel(
                (int) (epsilonAnchor.getX() + point.getX()),
                (int) (epsilonAnchor.getY() + point.getY()), relativePoint);
        point = weighedVector(point, 2);
        var extraBullet2 = new BulletModel(
                (int) (epsilonAnchor.getX() + point.getX()),
                (int) (epsilonAnchor.getY() + point.getY()), relativePoint);
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
