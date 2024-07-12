package org.windowkillproject.model.entities.enemies;

import org.windowkillproject.application.Config;
import org.windowkillproject.application.panels.game.GamePanel;
import org.windowkillproject.model.abilities.VertexModel;
import org.windowkillproject.model.entities.EpsilonModel;
import org.windowkillproject.model.entities.enemies.attackstypes.Dashable;

import javax.swing.*;
import java.awt.geom.Point2D;
import java.util.concurrent.atomic.AtomicInteger;

import static org.windowkillproject.application.Config.ENEMY_RADIUS;
import static org.windowkillproject.controller.GameController.random;
import static org.windowkillproject.controller.Utils.localRoutePoint;

public class SquarantineModel extends EnemyModel implements Dashable {
    private boolean collision;

    public void setCollision(boolean collision) {
        this.collision = collision;
    }

    public SquarantineModel(int x, int y, GamePanel localPanel) {
        super(localPanel,x, y, (int) (ENEMY_RADIUS * 0.8), 10, 6 , 1, 5);

        initVertices();
        initPolygon();
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
        return localRoutePoint(this.getAnchor(),
                EpsilonModel.getINSTANCE().getAnchor(), false);
    }

    public void dash() {
        AtomicInteger count = new AtomicInteger();
        Timer dashTimer = new Timer(Config.FPS / 2, null);
        Point2D deltaS = localRoutePoint(this.getAnchor(),
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