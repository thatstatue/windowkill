package org.windowkillproject.model.entities.enemies;

import org.windowkillproject.model.abilities.VertexModel;
import org.windowkillproject.model.entities.EpsilonModel;

import java.awt.*;
import java.awt.geom.Point2D;

import static org.windowkillproject.application.Config.ENEMY_RADIUS;
import static org.windowkillproject.controller.Utils.routePoint;

public class TrigorathModel extends EnemyModel {
    private final double RAD3_ON_2 = 0.866;
    public TrigorathModel(int x, int y) {
        super(x, y);
        setRadius(ENEMY_RADIUS);
        setHp(15);
        setAttackHp(10);
        setRewardCount(2);
        setRewardXps(50);
        initVertices();
        int[] xPoints = new int[3];
        int[] yPoints = new int[3];
        for (int i = 0 ; i < 3; i++){
            xPoints[i] = getVertices().get(i).getX();
            yPoints[i] = getVertices().get(i).getY();
        }
        setPolygon(new Polygon(xPoints , yPoints, 3));
    }

    @Override
    public void route() {
        move((int) getRoutePoint().getX(), (int) getRoutePoint().getY());
    }
    public Point2D getRoutePoint(){
        return routePoint(this.getAnchor(),
                EpsilonModel.getINSTANCE().getAnchor(), true);
    }

    @Override
    void initVertices() {
        getVertices().add(new VertexModel(getXO(), getYO() - getRadius(), this));
        getVertices().add(new VertexModel(getXO() - (int) (getRadius() * RAD3_ON_2), getYO() + getRadius() / 2, this));
        getVertices().add(new VertexModel(getXO() + (int) (getRadius() * RAD3_ON_2), getYO() + getRadius() / 2, this));
    }
}
