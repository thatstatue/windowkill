package org.windowkillproject.server.model.entities.enemies.normals;

import org.windowkillproject.server.model.abilities.VertexModel;
import org.windowkillproject.server.model.entities.enemies.EnemyModel;
import org.windowkillproject.server.model.globe.GlobeModel;
import org.windowkillproject.server.model.panelmodels.PanelModel;

import java.awt.geom.Point2D;

import static org.windowkillproject.server.Config.ENEMY_RADIUS;
import static org.windowkillproject.controller.Utils.localRoutePoint;

public class TrigorathModel extends EnemyModel {
    private final double RAD3_ON_2 = 0.866;

    public TrigorathModel(GlobeModel globeModel, int x, int y, PanelModel localPanel) {
        super(globeModel, localPanel, x, y, ENEMY_RADIUS , 15, 10, 2, 5);

        initVertices();
        initPolygon();
    }


    @Override
    public void route() {
        move((int) getRoutePoint().getX(), (int) getRoutePoint().getY());
    }

    public Point2D getRoutePoint() {
        return localRoutePoint(this.getAnchor(),
                targetEpsilon.getAnchor(), true);
    }

    @Override
    protected void initVertices() {
        getVertices().add(new VertexModel(getXO(), getYO() - getRadius(), this));
        getVertices().add(new VertexModel((int) (getXO() - (getRadius() * RAD3_ON_2)), getYO() + getRadius() / 2, this));
        getVertices().add(new VertexModel((int) (getXO() + (getRadius() * RAD3_ON_2)), getYO() + getRadius() / 2, this));
    }
}
