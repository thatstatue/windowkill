package org.windowkillproject.model.entities.enemies;

import org.windowkillproject.application.panels.game.GamePanel;
import org.windowkillproject.model.abilities.VertexModel;
import org.windowkillproject.model.entities.EpsilonModel;

import java.awt.*;
import java.awt.geom.Point2D;

import static org.windowkillproject.application.Config.ENEMY_RADIUS;
import static org.windowkillproject.controller.Utils.routePoint;

public class TrigorathModel extends EnemyModel {
    private final double RAD3_ON_2 = 0.866;

    public TrigorathModel(int x, int y, GamePanel localPanel) {
        super(localPanel, x, y, ENEMY_RADIUS , 15, 10, 2, 5);

        initVertices();
        initPolygon();
    }


    @Override
    public void route() {
        move((int) getRoutePoint().getX(), (int) getRoutePoint().getY());
    }

    public Point2D getRoutePoint() {
        return routePoint(this.getAnchor(),
                EpsilonModel.getINSTANCE().getAnchor(), true);
    }

    @Override
    void initVertices() {
        getVertices().add(new VertexModel(getXO(), getYO() - getRadius(), this));
        getVertices().add(new VertexModel((int) (getXO() - (getRadius() * RAD3_ON_2)), getYO() + getRadius() / 2, this));
        getVertices().add(new VertexModel((int) (getXO() + (getRadius() * RAD3_ON_2)), getYO() + getRadius() / 2, this));
    }
}
