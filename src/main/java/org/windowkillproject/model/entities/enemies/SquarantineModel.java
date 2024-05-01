package org.windowkillproject.model.entities.enemies;

import org.windowkillproject.application.Config;
import org.windowkillproject.model.abilities.VertexModel;
import org.windowkillproject.model.entities.EpsilonModel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.concurrent.atomic.AtomicInteger;

import static org.windowkillproject.application.Config.ENEMY_RADIUS;
import static org.windowkillproject.controller.GameController.random;
import static org.windowkillproject.controller.Utils.routePoint;

public class SquarantineModel extends EnemyModel {
    private boolean collision;

    public void setCollision(boolean collision) {
        this.collision = collision;
    }

    public SquarantineModel(int x, int y) {
        super(x, y);
        setRadius((int) (ENEMY_RADIUS * 0.8));
        setHp(10);
        setAttackHp(6);
        setRewardCount(1);
        setRewardXps(5);
        initVertices();
        int[] xPoints = new int[4];
        int[] yPoints = new int[4];
        for (int i = 0; i < 4; i++) {
            xPoints[i] = getVertices().get(i).getX();
            yPoints[i] = getVertices().get(i).getY();
        }
        setPolygon(new Polygon(xPoints, yPoints, 4));
    }

    @Override
    void initVertices() {
        getVertices().add(new VertexModel(getXO(), getYO() - getRadius(), this));
        getVertices().add(new VertexModel(getXO() - getRadius(), getYO(), this));
        getVertices().add(new VertexModel(getXO(), getYO() + getRadius(), this));
        getVertices().add(new VertexModel(getXO() + getRadius(), getYO(), this));
    }

    @Override
    public void route() {

        move((int) getRoutePoint().getX(), (int) getRoutePoint().getY());
        if (random.nextInt(7) == 3) dash();
    }

    public Point2D getRoutePoint() {
        return routePoint(this.getAnchor(),
                EpsilonModel.getINSTANCE().getAnchor(), false);
    }

    private void dash() {
        AtomicInteger count = new AtomicInteger();
        Timer dashTimer = new Timer(Config.FPS / 2, null);
        Point2D deltaS = routePoint(this.getAnchor(),
                EpsilonModel.getINSTANCE().getAnchor(), false);

        dashTimer.addActionListener(e -> {
            if (count.get() >= 10 || collision) {
                collision = false;
                dashTimer.stop();
            } else {
                move((int) deltaS.getX(), (int) deltaS.getY() * 2);
                count.getAndIncrement();
            }
        });
        dashTimer.start();
    }
}