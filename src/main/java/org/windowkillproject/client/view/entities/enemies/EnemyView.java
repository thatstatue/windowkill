package org.windowkillproject.client.view.entities.enemies;

import org.windowkillproject.client.view.entities.EntityView;

import java.awt.*;

public abstract class EnemyView extends EntityView {
    private Polygon polygon;

    public EnemyView(String id, Polygon polygon) {
        super(id);
        this.polygon = polygon;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }

    public Polygon getPolygon() {
        return polygon;
    }


}
