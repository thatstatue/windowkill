package org.windowkillproject.model.entities.enemies.normals;

import org.windowkillproject.controller.ElapsedTime;
import org.windowkillproject.controller.Utils;
import org.windowkillproject.model.abilities.ProjectileModel;
import org.windowkillproject.model.abilities.VertexModel;
import org.windowkillproject.model.entities.EpsilonModel;
import org.windowkillproject.model.entities.enemies.EnemyModel;
import org.windowkillproject.model.entities.enemies.attackstypes.Hideable;
import org.windowkillproject.model.entities.enemies.attackstypes.NonRotatable;
import org.windowkillproject.model.entities.enemies.attackstypes.ProjectileOperator;

import java.awt.*;
import java.awt.geom.Point2D;

import static org.windowkillproject.application.Config.*;
import static org.windowkillproject.controller.GameController.keepInPanel;
import static org.windowkillproject.controller.Utils.globalRoutePoint;
import static org.windowkillproject.controller.Utils.localRoutePoint;

public class NecropickModel extends EnemyModel implements ProjectileOperator, NonRotatable, Hideable {

    public NecropickModel(int x, int y) {
        super(EpsilonModel.getINSTANCE().getLocalPanel(),
                x, y, (int) (ENEMY_RADIUS * 3), 10, 0, 4, 2);
        initVertices();
        initPolygon();

        appearanceTimeStart = ElapsedTime.getTotalSeconds();
    }


    private int appearanceTimeStart, appearanceTimeEnd;

    @Override
    public void route() {
        setTheta(0);
        if (!visible) {
            if (ElapsedTime.getTotalSeconds() - appearanceTimeEnd > 4) {
                appearanceTimeStart = ElapsedTime.getTotalSeconds();
                Point2D direction = Utils.unitVector(EpsilonModel.getINSTANCE().getAnchor(), getAnchor());
                double distance = Utils.magnitude(getAnchor(), EpsilonModel.getINSTANCE().getAnchor());
                Point2D vector = Utils.weighedVector(direction, distance-EPSILON_RADIUS-getRadius()-10);
                double moveX = vector.getX();
                double moveY = vector.getY();
//                if (vector.getX() < 8) moveX += 4 * EPSILON_RADIUS;
//                if (vector.getY() < 8) moveY += 4 * EPSILON_RADIUS;
                setLocalPanel(EpsilonModel.getINSTANCE().getLocalPanel());
                move((int) moveX, (int) moveY);
            }
        } else {
            if (!EpsilonModel.getINSTANCE().getAllowedPanels().isEmpty()) {
                var panel = EpsilonModel.getINSTANCE().getAllowedPanels().get(0);
                if (panel != null) keepInPanel(this, panel);
            }
            shoot();
            if (ElapsedTime.getTotalSeconds() - appearanceTimeStart > 8) {
                visible = false;
                appearanceTimeEnd = ElapsedTime.getTotalSeconds();
            }
        }
    }

    @Override
    public Point2D getRoutePoint() {
        return globalRoutePoint(this.getAnchor(),
                EpsilonModel.getINSTANCE().getAnchor());
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
        if (ElapsedTime.getTotalSeconds() - lastShot > PROJECTILE_TIMEOUT) {
            new ProjectileModel(getLocalPanel(), this, 5, false, false, Color.gray, Color.darkGray).shoot();
            lastShot = ElapsedTime.getTotalSeconds();
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
