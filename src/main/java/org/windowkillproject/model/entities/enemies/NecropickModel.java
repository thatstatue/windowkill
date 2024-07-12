package org.windowkillproject.model.entities.enemies;

import org.windowkillproject.controller.ElapsedTime;
import org.windowkillproject.controller.Utils;
import org.windowkillproject.model.abilities.ProjectileModel;
import org.windowkillproject.model.abilities.VertexModel;
import org.windowkillproject.model.entities.EpsilonModel;
import org.windowkillproject.model.entities.enemies.attackstypes.NonRotatable;
import org.windowkillproject.model.entities.enemies.attackstypes.ProjectileOperator;

import java.awt.*;
import java.awt.geom.Point2D;

import static org.windowkillproject.application.Config.*;
import static org.windowkillproject.controller.Utils.globalRoutePoint;
import static org.windowkillproject.controller.Utils.localRoutePoint;

public class NecropickModel extends EnemyModel implements ProjectileOperator, NonRotatable {

    public NecropickModel(int x, int y) {
        super(EpsilonModel.getINSTANCE().getLocalPanel(),
                x, y, (int) (ENEMY_RADIUS * 3), 10, 0, 4, 2);
        initVertices();
        initPolygon();

        appearanceTimeStart = ElapsedTime.getTotalSeconds();
    }

    private boolean isSubtle = false;

    public boolean isSubtle() {
        return isSubtle;
    }

    public void setSubtle(boolean subtle) {
        isSubtle = subtle;
    }

    private int appearanceTimeStart, appearanceTimeEnd;

    @Override
    public void route() {
        setTheta(0);
        if (isSubtle) {
            if (ElapsedTime.getTotalSeconds() - appearanceTimeEnd > 4) {
                isSubtle = false;
                appearanceTimeStart = ElapsedTime.getTotalSeconds();
//                move(10000, 10000);
                Point2D direction = Utils.unitVector(EpsilonModel.getINSTANCE().getAnchor(), getAnchor());
                double distance = Utils.magnitude(getAnchor(), EpsilonModel.getINSTANCE().getAnchor());
                Point2D vector = Utils.weighedVector(direction, distance);
                double moveX = vector.getX();
                double moveY = vector.getY();
                if (vector.getX() < 8) moveX += 10 * EPSILON_RADIUS;
                if (vector.getY() < 8) moveY += 10 * EPSILON_RADIUS;
                setLocalPanel(EpsilonModel.getINSTANCE().getLocalPanel());
                move((int) moveX, (int) moveY);
                System.out.println("just outta subtle");
            }
            System.out.println("im subtle");
        } else {
            System.out.println("im not subtle");
            shoot();
            if (ElapsedTime.getTotalSeconds() - appearanceTimeStart > 8) {
                isSubtle = true;
                appearanceTimeEnd = ElapsedTime.getTotalSeconds();
//                move(-10000, -10000);
                System.out.println("im subtle again");
            }
        }

    }

    @Override
    public Point2D getRoutePoint() {
        return globalRoutePoint(this.getAnchor(),
                EpsilonModel.getINSTANCE().getAnchor());
    }

    @Override
    void initVertices() {
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
}
