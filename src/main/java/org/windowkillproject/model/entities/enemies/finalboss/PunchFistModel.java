package org.windowkillproject.model.entities.enemies.finalboss;

import org.windowkillproject.application.panels.game.GamePanel;
import org.windowkillproject.application.panels.game.InternalGamePanel;
import org.windowkillproject.application.panels.game.PanelStatus;
import org.windowkillproject.model.abilities.VertexModel;
import org.windowkillproject.model.entities.enemies.EnemyModel;

import java.awt.geom.Point2D;

import static org.windowkillproject.application.Config.HAND_RADIUS;
import static org.windowkillproject.controller.Controller.createEntityView;

public class PunchFistModel extends EnemyModel {
    private static boolean on;
    public PunchFistModel(int x, int y) {
        super(null, x, y, HAND_RADIUS, Integer.MAX_VALUE, 10, 0, 0);
        setLocalPanel(new InternalGamePanel(x, y, HAND_RADIUS*3, HAND_RADIUS*3,
                PanelStatus.isometric , true
        ));

        initVertices();
        initPolygon();
        createEntityView(getId(), getX(),getY(),getWidth(),getHeight());
        on = true;
    }

    public static boolean isOn() {
        return on;
    }

    @Override
    public void route() {

    }

    @Override
    public Point2D getRoutePoint() {
        return null;
    }

    @Override
    protected void initVertices() {
        int halfSideLength = (int) (getRadius() / Math.sqrt(2)+14);


        getVertices().add(new VertexModel(getXO() - halfSideLength, getYO() - halfSideLength, this));
        getVertices().add(new VertexModel(getXO() + halfSideLength, getYO() - halfSideLength, this));
        getVertices().add(new VertexModel(getXO() + halfSideLength, getYO() + halfSideLength, this));
        getVertices().add(new VertexModel(getXO() - halfSideLength, getYO() + halfSideLength, this));

    }
}
