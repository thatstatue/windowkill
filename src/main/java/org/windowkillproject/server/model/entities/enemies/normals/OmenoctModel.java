package org.windowkillproject.server.model.entities.enemies.normals;


import org.windowkillproject.controller.Utils;
import org.windowkillproject.server.model.abilities.BulletModel;
import org.windowkillproject.server.model.abilities.ProjectileModel;
import org.windowkillproject.server.model.abilities.VertexModel;
import org.windowkillproject.server.model.entities.enemies.EnemyModel;
import org.windowkillproject.server.model.entities.enemies.attackstypes.ProjectileOperator;
import org.windowkillproject.server.model.globe.GlobeModel;
import org.windowkillproject.server.model.panelmodels.PanelModel;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import static java.lang.Math.*;
import static org.windowkillproject.Constants.*;
import static org.windowkillproject.server.Config.*;
//todo in ctor set an epsilon in the game that this enemy will target

public class OmenoctModel extends EnemyModel implements ProjectileOperator {
    public OmenoctModel(String globeId, int x, int y, PanelModel localPanel) {
        super(globeId, localPanel, x, y, (int) (ENEMY_RADIUS * 1.2), 20, 6, 8, 4);
        initVertices();
        initPolygon();
        getGlobeModel().getOmenoctModels().add(this);
    }
    private long lastShot;
    private int edgeCode;
    @Override
    public void destroy(){
        super.destroy();
        getGlobeModel().getOmenoctModels().remove(this);
    }

    @Override
    public void route() {
        int x= getX(); int y= getY();

        if (targetEpsilon.getLocalPanelModel()!=null) {
            Point2D routePoint = goToNearestEdge();
            if (routePoint.getX() == 0 && routePoint.getY() == 0) {
                shoot();
            } else {
                move((int) routePoint.getX(), (int) routePoint.getY());
            }
        } else {
            move((int) getRoutePoint().getX(), (int) getRoutePoint().getY());
        }
        moveBGPanel(x, y);
    }


    @Override
    public void shoot() {
        if (getGlobeModel().getElapsedTime().getTotalSeconds() - lastShot > PROJECTILE_TIMEOUT) {
            new ProjectileModel(getLocalPanelModel(),this, 4, true, targetEpsilon, Color.red, Color.white).shoot();
            lastShot = getGlobeModel().getElapsedTime().getTotalSeconds();
        }
    }

    public void hitWall(int code) {
        if (code == edgeCode) gotShot(BulletModel.getAttackHp());
    }

    private Point2D goToNearestEdge() {
        Rectangle rectangle = targetEpsilon.getLocalPanelModel().getBounds();
        int distanceFromLeft = getXO() - rectangle.x ;
        int distanceFromUp = getYO() - rectangle.y ;
        int distanceFromRight = rectangle.x + rectangle.width - getXO();
        int distanceFromDown = rectangle.y + rectangle.height - getYO();

        boolean isInLeftBound = distanceFromLeft > 0;
        boolean isInRightBound = distanceFromRight > 0;
        boolean isInUpBound = distanceFromUp> 0;
        boolean isInDownBound = distanceFromDown > 0;

        boolean isToRight = distanceFromRight < distanceFromLeft;
        boolean isToDown = distanceFromDown < distanceFromUp;

        int x = 0;
        int y = 0;
        if (!isInLeftBound) x = MIN_ENEMY_SPEED;
        if (!isInRightBound) x = -MIN_ENEMY_SPEED;
        if (!isInUpBound) y = MIN_ENEMY_SPEED;
        if (!isInDownBound) y = -MIN_ENEMY_SPEED;
        if (!(min(abs(distanceFromLeft), abs(distanceFromRight)) < 5
                || min(abs(distanceFromUp), abs(distanceFromDown)) < 5)) {

            if (isInLeftBound && isInRightBound) {
                if (isToRight) x = MIN_ENEMY_SPEED;
                else x = -MIN_ENEMY_SPEED;
            }
            if (isInUpBound && isInDownBound) {
                if (isToDown) y = MIN_ENEMY_SPEED;
                else y = -MIN_ENEMY_SPEED;
            }
        } else {
            setTheta(0);
        }
        if (x == 0) {
            edgeCode = LEFT_CODE;
            if (isToRight) edgeCode = RIGHT_CODE;
        } else if (y == 0) {
            edgeCode = UP_CODE;
            if (isToDown) edgeCode = DOWN_CODE;
        }
        return new Point2D.Double(x, y);
    }

    @Override
    public Point2D getRoutePoint() {
        return Utils.globalRoutePoint(this.getAnchor(),
                targetEpsilon.getAnchor());
    }

    @Override
    protected void initVertices() {
        getVertices().add(new VertexModel(getXO(), getYO() - getRadius(), this));
        getVertices().add(new VertexModel(getXO() - getRadius(), getYO(), this));
        getVertices().add(new VertexModel(getXO(), getYO() + getRadius(), this));
        getVertices().add(new VertexModel(getXO() + getRadius(), getYO(), this));
        rotate(Math.PI / 4);
        getVertices().add(new VertexModel(getXO(), getYO() - getRadius(), this));
        getVertices().add(new VertexModel(getXO() - getRadius(), getYO(), this));
        getVertices().add(new VertexModel(getXO(), getYO() + getRadius(), this));
        getVertices().add(new VertexModel(getXO() + getRadius(), getYO(), this));

        setVertices(getVerticesInOrder());
    }

    private ArrayList<VertexModel> getVerticesInOrder() {
        ArrayList<VertexModel> verticesInOrder = new ArrayList<>();
        verticesInOrder.add(getVertices().get(0));
        verticesInOrder.add(getVertices().get(4));
        verticesInOrder.add(getVertices().get(1));
        verticesInOrder.add(getVertices().get(5));
        verticesInOrder.add(getVertices().get(2));
        verticesInOrder.add(getVertices().get(6));
        verticesInOrder.add(getVertices().get(3));
        verticesInOrder.add(getVertices().get(7));
        return verticesInOrder;
    }
}
