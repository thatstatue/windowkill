package org.windowkillproject.server.model.entities.enemies.normals;

import org.windowkillproject.server.model.abilities.VertexModel;
import org.windowkillproject.server.model.entities.enemies.EnemyModel;
import org.windowkillproject.server.model.entities.enemies.attackstypes.Dashable;
import org.windowkillproject.server.model.globe.GlobeModel;
import org.windowkillproject.server.model.panelmodels.PanelModel;

import javax.swing.*;
import java.awt.geom.Point2D;
import java.util.concurrent.atomic.AtomicInteger;

import static org.windowkillproject.Constants.FPS;
import static org.windowkillproject.server.Config.ENEMY_RADIUS;
import static org.windowkillproject.controller.GameManager.random;
import static org.windowkillproject.controller.Utils.localRoutePoint;

public class SquarantineModel extends EnemyModel implements Dashable {
    private boolean collision;

    public void setCollision(boolean collision) {
        this.collision = collision;
    }

    public SquarantineModel(String globeId, int x, int y, PanelModel localPanel) {
        super(globeId,localPanel,x, y, (int) (ENEMY_RADIUS * 0.8), 10, 6 , 1, 5);

        initVertices();
        initPolygon();
    }



    @Override
    protected void initVertices() {
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
                targetEpsilon.getAnchor(), false);
    }

    public void dash() {
        AtomicInteger count = new AtomicInteger();
        Timer dashTimer = new Timer(FPS / 2, null);
        Point2D deltaS = localRoutePoint(this.getAnchor(),
                targetEpsilon.getAnchor(), false);

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