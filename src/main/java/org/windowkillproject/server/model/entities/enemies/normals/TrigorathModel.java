package org.windowkillproject.server.model.entities.enemies.normals;

import org.windowkillproject.client.ui.panels.game.GamePanel;
import org.windowkillproject.server.model.abilities.VertexModel;
import org.windowkillproject.server.model.entities.EpsilonModel;
import org.windowkillproject.server.model.entities.enemies.EnemyModel;

import java.awt.geom.Point2D;

import static org.windowkillproject.server.Config.ENEMY_RADIUS;
import static org.windowkillproject.controller.Utils.localRoutePoint;

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
        return localRoutePoint(this.getAnchor(),
                EpsilonModel.getINSTANCE().getAnchor(), true);
    }

    @Override
    protected void initVertices() {
        getVertices().add(new VertexModel(getXO(), getYO() - getRadius(), this));
        getVertices().add(new VertexModel((int) (getXO() - (getRadius() * RAD3_ON_2)), getYO() + getRadius() / 2, this));
        getVertices().add(new VertexModel((int) (getXO() + (getRadius() * RAD3_ON_2)), getYO() + getRadius() / 2, this));
    }
}
