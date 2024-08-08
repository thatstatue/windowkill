package org.windowkillproject.server.model.entities.enemies.normals;

import org.windowkillproject.controller.Utils;
import org.windowkillproject.server.model.abilities.ProjectileModel;
import org.windowkillproject.server.model.abilities.VertexModel;
import org.windowkillproject.server.model.entities.enemies.EnemyModel;
import org.windowkillproject.server.model.entities.enemies.attackstypes.Hideable;
import org.windowkillproject.server.model.entities.enemies.attackstypes.NonRotatable;
import org.windowkillproject.server.model.entities.enemies.attackstypes.ProjectileOperator;
import org.windowkillproject.server.model.globe.GlobeModel;

import java.awt.*;
import java.awt.geom.Point2D;

import static org.windowkillproject.Constants.EPSILON_RADIUS;
import static org.windowkillproject.Constants.PROJECTILE_TIMEOUT;
import static org.windowkillproject.server.Config.*;
import static org.windowkillproject.controller.GameManager.keepInPanel;
import static org.windowkillproject.controller.Utils.globalRoutePoint;

public class NecropickModel extends EnemyModel implements ProjectileOperator, NonRotatable, Hideable {

    public NecropickModel(String globeId, int x, int y) {
        super(globeId, null,
                x, y, (ENEMY_RADIUS * 3), 10, 0, 4, 2);
        initVertices();
        initPolygon();
        setLocalPanelModel(targetEpsilon.getLocalPanelModel());
        appearanceTimeStart = getGlobeModel().getElapsedTime().getTotalSeconds();
    }


    private int appearanceTimeStart, appearanceTimeEnd;

    @Override
    public void route() {
        setTheta(0);
        if (!visible) {
            if (getGlobeModel().getElapsedTime().getTotalSeconds() - appearanceTimeEnd > 4) {
                appearanceTimeStart = getGlobeModel().getElapsedTime().getTotalSeconds();
                Point2D direction = Utils.unitVector(targetEpsilon.getAnchor(), getAnchor());
                double distance = Utils.magnitude(getAnchor(), targetEpsilon.getAnchor());
                Point2D vector = Utils.weighedVector(direction, distance-EPSILON_RADIUS-getRadius()-10);
                double moveX = vector.getX();
                double moveY = vector.getY();
//                if (vector.getX() < 8) moveX += 4 * EPSILON_RADIUS;
//                if (vector.getY() < 8) moveY += 4 * EPSILON_RADIUS;
                setLocalPanelModel(targetEpsilon.getLocalPanelModel());
                move((int) moveX, (int) moveY);
            }
        } else {
            if (!targetEpsilon.getAllowedPanelModels().isEmpty()) {
                var panel = targetEpsilon.getAllowedPanelModels().get(0);
                if (panel != null) keepInPanel(this, panel);
            }
            shoot();
            if (getGlobeModel().getElapsedTime().getTotalSeconds() - appearanceTimeStart > 8) {
                visible = false;
                appearanceTimeEnd = getGlobeModel().getElapsedTime().getTotalSeconds();
            }
        }
    }

    @Override
    public Point2D getRoutePoint() {
        return globalRoutePoint(this.getAnchor(),
                targetEpsilon.getAnchor());
    }

    @Override
    protected void initVertices() {
        int startX = getXO();
        int startY = getYO();
        int radius = getRadius();
        int arrowHeadWidth = radius;
        int arrowHeadLength = radius / 4;
        int shaftWidth = radius / 7;
        getVertices().clear();

        getVertices().add(new VertexModel(startX + radius - arrowHeadLength * 2, startY - arrowHeadWidth / 2, this));
        getVertices().add(new VertexModel(startX + radius, startY, this));
        getVertices().add(new VertexModel(startX + radius - arrowHeadLength * 2, startY + arrowHeadWidth / 2, this));
        getVertices().add(new VertexModel(startX + radius - arrowHeadLength, startY + shaftWidth / 2, this));
        getVertices().add(new VertexModel(startX, startY + shaftWidth / 2, this));
        getVertices().add(new VertexModel(startX, startY - shaftWidth / 2, this));
        getVertices().add(new VertexModel(startX + radius - arrowHeadLength, startY - shaftWidth / 2, this));

    }

    private int lastShot;

    @Override
    public void shoot() {
        if (getGlobeModel().getElapsedTime().getTotalSeconds() - lastShot > PROJECTILE_TIMEOUT) {
            new ProjectileModel(getLocalPanelModel(), this, 5, false, null, Color.gray, Color.darkGray).shoot();
            lastShot = getGlobeModel().getElapsedTime().getTotalSeconds();
        }
    }

    private boolean visible;
    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }
}
