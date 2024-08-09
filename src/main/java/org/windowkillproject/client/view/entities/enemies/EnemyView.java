package org.windowkillproject.client.view.entities.enemies;

import org.windowkillproject.client.view.entities.EntityView;

import java.awt.*;

public abstract class EnemyView extends EntityView {
    private Polygon polygon;

    public EnemyView(String globeId, String id, Polygon polygon) {
        super(globeId, id );
        this.polygon = polygon;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }

    public Polygon getPolygon() {
        return polygon;
    }


}
