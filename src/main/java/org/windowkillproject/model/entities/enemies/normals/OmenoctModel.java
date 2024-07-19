package org.windowkillproject.model.entities.enemies.normals;


import org.windowkillproject.application.panels.game.GamePanel;
import org.windowkillproject.controller.ElapsedTime;
import org.windowkillproject.controller.Utils;
import org.windowkillproject.model.abilities.BulletModel;
import org.windowkillproject.model.abilities.ProjectileModel;
import org.windowkillproject.model.abilities.VertexModel;
import org.windowkillproject.model.entities.EpsilonModel;
import org.windowkillproject.model.entities.enemies.EnemyModel;
import org.windowkillproject.model.entities.enemies.attackstypes.ProjectileOperator;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import static java.lang.Math.*;
import static org.windowkillproject.application.Config.*;
import static org.windowkillproject.application.panels.game.GamePanel.gamePanelsBounds;

public class OmenoctModel extends EnemyModel implements ProjectileOperator {
    public OmenoctModel(int x, int y, GamePanel localPanel) {
        super(localPanel, x, y, (int) (ENEMY_RADIUS * 1.2), 20, 6, 8, 4);
        initVertices();
        initPolygon();
        omenoctModels.add(this);
    }
public static ArrayList<OmenoctModel> omenoctModels = new ArrayList<>();
    private long lastShot;
    private int edgeCode;
    @Override
    public void destroy(){
        super.destroy();
        omenoctModels.remove(this);
    }

    @Override
    public void route() {
        int x= getX(); int y= getY();

        if (EpsilonModel.getINSTANCE().getLocalPanel()!=null) {
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
        if (ElapsedTime.getTotalSeconds() - lastShot > PROJECTILE_TIMEOUT) {
            new ProjectileModel(getLocalPanel(),this, 4, true, true, Color.red, Color.white).shoot();
            lastShot = ElapsedTime.getTotalSeconds();
        }
    }

    public void hitWall(int code) {
        if (code == edgeCode) gotShot(BulletModel.getAttackHp());
    }

    private Point2D goToNearestEdge() {
        Rectangle rectangle = gamePanelsBounds.get(EpsilonModel.getINSTANCE().getLocalPanel());
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
                EpsilonModel.getINSTANCE().getAnchor());
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