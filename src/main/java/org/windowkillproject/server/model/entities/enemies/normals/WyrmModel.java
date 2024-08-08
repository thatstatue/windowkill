package org.windowkillproject.server.model.entities.enemies.normals;

import org.windowkillproject.server.model.globe.GlobeModel;
import org.windowkillproject.server.model.panelmodels.InternalPanelModel;
import org.windowkillproject.server.model.panelmodels.PanelStatus;
import org.windowkillproject.server.model.abilities.ProjectileModel;
import org.windowkillproject.server.model.abilities.VertexModel;
import org.windowkillproject.server.model.entities.Circular;
import org.windowkillproject.server.model.entities.enemies.EnemyModel;
import org.windowkillproject.server.model.entities.enemies.attackstypes.NonRotatable;
import org.windowkillproject.server.model.entities.enemies.attackstypes.ProjectileOperator;
import org.windowkillproject.server.model.entities.enemies.attackstypes.Unmovable;

import java.awt.*;
import java.awt.geom.Point2D;

import static org.windowkillproject.Constants.*;
import static org.windowkillproject.controller.Utils.getSign;
import static org.windowkillproject.controller.Utils.globalRoutePoint;
import static org.windowkillproject.server.Config.WYRM_RADIUS;

public class WyrmModel extends EnemyModel implements ProjectileOperator, Unmovable, NonRotatable, Circular {
    private static int count;

    public static int getCount() {
        return count;
    }

    public WyrmModel(String globeId, int x, int y) {
        super(globeId,null, x, y, WYRM_RADIUS, 12 , 0, 2, 8);
         setLocalPanelModel(new InternalPanelModel(globeId,new Rectangle(x, y, WYRM_RADIUS*3, WYRM_RADIUS*3),
                 PanelStatus.isometric , true
                 ));
         initVertices();
         initPolygon();
//        globeModel.getGlobeController().createEntityView(getId(), getX(),getY(),getWidth(),getHeight());
         count++;
     }

    private long lastShot;

    @Override
    public void route() {
        if (getAnchor().distance(targetEpsilon.getAnchor())< WYRM_DISTANCE) {
            shoot();
            goRoundEpsilon();
        } else {
            move((int) getRoutePoint().getX(), (int) getRoutePoint().getY());
        }
        getLocalPanelModel().setX((int) (getXO()-getRadius()*1.5));
        getLocalPanelModel().setY((int) (getYO()-getRadius()*1.5));
    }

    @Override
    public void shoot() {
        if (getGlobeModel().getElapsedTime().getTotalSeconds() - lastShot > PROJECTILE_TIMEOUT) { //todo epsilonmodel which
            new ProjectileModel(getLocalPanelModel(),this, 4, true, null, Color.magenta, Color.magenta).shoot();
            lastShot = getGlobeModel().getElapsedTime().getTotalSeconds();
        }
    }
    private double rotationSpeed = UNIT_DEGREE/4;


    public void setMinusRotationSpeed() {
        this.rotationSpeed = -rotationSpeed;
    }
    public int getRotationSign() {
        return getSign((int) rotationSpeed);
    }


    private void goRoundEpsilon() {
        var epsilonModel = targetEpsilon;
        double degree = Math.atan2( this.getYO() - epsilonModel.getYO(),this.getXO() - epsilonModel.getXO());
        degree += rotationSpeed;
        int finalX =epsilonModel.getXO() + (int)(WYRM_DISTANCE*0.8 * Math.cos(degree)) - getRadius();
        int finalY = epsilonModel.getYO() + (int) (WYRM_DISTANCE*0.8 * Math.sin(degree)) - getRadius();
        move(finalX - getX(), finalY - getY());
    }

    @Override
    public Point2D getRoutePoint() {
        return globalRoutePoint(this.getAnchor(),
                targetEpsilon.getAnchor(), MIN_ENEMY_SPEED+0.5);
    }
    @Override
    public void destroy() {
        super.destroy();
        getGlobeModel().getGameManager().deleteGamePanel(getLocalPanelModel());
        count--;
    }


    @Override
    protected void initVertices() {

        int width = 110;
        int height = 20;
        int startX = getX()+ height - 5;
        int startY = getY() +WYRM_RADIUS - height -1;
        getVertices().add(new VertexModel(startX , startY ,this));
        getVertices().add(new VertexModel(startX+width, startY,this));
        getVertices().add(new VertexModel(startX+width+height, startY+height,this));
        getVertices().add(new VertexModel(startX+width , startY+2*height,this));
        getVertices().add(new VertexModel(startX, startY+2*height ,this));
        getVertices().add(new VertexModel(startX-height, startY+height,this));
    }

}
