package org.windowkillproject.client.view.entities.enemies;

import org.windowkillproject.client.view.entities.EntityView;
import org.windowkillproject.controller.Controller;
import org.windowkillproject.server.model.entities.enemies.EnemyModel;

import java.awt.*;
import java.util.Objects;


public abstract class EnemyView extends EntityView {
    private Polygon polygon;

    public EnemyView(String id) {
        super(id);
        this.polygon = ((EnemyModel) Objects.requireNonNull(Controller.findModel(id))).getPolygon();
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }

    public Polygon getPolygon() {
        return polygon;
    }


}
