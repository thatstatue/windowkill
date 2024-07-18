package org.windowkillproject.model.entities.enemies.normals;

import org.windowkillproject.application.panels.game.InternalGamePanel;
import org.windowkillproject.application.panels.game.PanelStatus;
import org.windowkillproject.controller.ElapsedTime;
import org.windowkillproject.model.abilities.ProjectileModel;
import org.windowkillproject.model.abilities.VertexModel;
import org.windowkillproject.model.entities.Circular;
import org.windowkillproject.model.entities.EpsilonModel;
import org.windowkillproject.model.entities.enemies.EnemyModel;
import org.windowkillproject.model.entities.enemies.attackstypes.NonRotatable;
import org.windowkillproject.model.entities.enemies.attackstypes.ProjectileOperator;
import org.windowkillproject.model.entities.enemies.attackstypes.Unmovable;

import java.awt.*;
import java.awt.geom.Point2D;

import static org.windowkillproject.application.Config.*;
import static org.windowkillproject.controller.Controller.createEntityView;
import static org.windowkillproject.controller.Controller.deleteGamePanel;
import static org.windowkillproject.controller.Utils.getSign;
import static org.windowkillproject.controller.Utils.globalRoutePoint;

public class WyrmModel extends EnemyModel implements ProjectileOperator, Unmovable, NonRotatable, Circular {
    private static int count;

    public static int getCount() {
        return count;
    }

    public WyrmModel(int x, int y) {
        super(null, x, y, WYRM_RADIUS, 12 , 0, 2, 8);
         setLocalPanel(new InternalGamePanel(x, y, WYRM_RADIUS*3, WYRM_RADIUS*3,
                 PanelStatus.isometric , true
                 ));
         initVertices();
         initPolygon();
         createEntityView(getId(), getX(),getY(),getWidth(),getHeight());
         count++;
     }

    private long lastShot;

    @Override
    public void route() {
        if (getAnchor().distance(EpsilonModel.getINSTANCE().getAnchor())< WYRM_DISTANCE) {
            shoot();
            goRoundEpsilon();
        } else {
            move((int) getRoutePoint().getX(), (int) getRoutePoint().getY());
        }
        getLocalPanel().setLocation((int) (getXO()-getRadius()*1.5), (int) (getYO()-getRadius()*1.5));
    }

    @Override
    public void shoot() {
        if (ElapsedTime.getTotalSeconds() - lastShot > PROJECTILE_TIMEOUT) {
            new ProjectileModel(getLocalPanel(),this, 4, true, true, Color.magenta, Color.magenta).shoot();
            lastShot = ElapsedTime.getTotalSeconds();
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
        var epsilonModel = EpsilonModel.getINSTANCE();
        double degree = Math.atan2( this.getYO() - epsilonModel.getYO(),this.getXO() - epsilonModel.getXO());
        degree += rotationSpeed;
        int finalX =epsilonModel.getXO() + (int)(WYRM_DISTANCE*0.8 * Math.cos(degree)) - getRadius();
        int finalY = epsilonModel.getYO() + (int) (WYRM_DISTANCE*0.8 * Math.sin(degree)) - getRadius();
        move(finalX - getX(), finalY - getY());
    }

    @Override
    public Point2D getRoutePoint() {
        return globalRoutePoint(this.getAnchor(),
                EpsilonModel.getINSTANCE().getAnchor(), MIN_ENEMY_SPEED+0.5);
    }
    @Override
    public void destroy() {
        super.destroy();
        deleteGamePanel(getLocalPanel());
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
