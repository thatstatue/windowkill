package org.windowkillproject.model.entities.enemies;

import org.windowkillproject.application.panels.game.InternalGamePanel;
import org.windowkillproject.application.panels.game.PanelStatus;
import org.windowkillproject.controller.ElapsedTime;
import org.windowkillproject.model.abilities.ProjectileModel;
import org.windowkillproject.model.abilities.VertexModel;
import org.windowkillproject.model.entities.EpsilonModel;

import java.awt.*;
import java.awt.geom.Point2D;

import static java.lang.Math.*;
import static org.windowkillproject.application.Application.getGameFrame;
import static org.windowkillproject.application.Config.*;
import static org.windowkillproject.application.panels.game.GamePanel.gamePanels;
import static org.windowkillproject.controller.Controller.createEntityView;
import static org.windowkillproject.controller.Utils.routePoint;

public class WyrmModel extends EnemyModel implements ProjectileOperator, Unmovable, NonRotatable {
     public WyrmModel(int x, int y) {
        super(null, x, y, WYRM_RADIUS, 12 , 0, 2, 8);

//         int kitWidth =Toolkit.getDefaultToolkit().getScreenSize().width;
//         int kitHeight =Toolkit.getDefaultToolkit().getScreenSize().height;
//         int panelX = random.nextInt( kitWidth/ 2)+ kitWidth/4;
//         int panelY = random.nextInt( kitHeight/ 2)+ kitHeight/4;
//         panelX = x; panelY = y;
         setLocalPanel(new InternalGamePanel(PanelStatus.isometric,
                 WYRM_RADIUS*3, WYRM_RADIUS*3, x, y
                 ));
//         getGameFrame().add(getLocalPanel());
//         System.out.println(getLocalPanel());
//         move(getLocalPanel().getX()-getX()+getLocalPanel().getWidth()/2,
//                 getLocalPanel().getY()-getY()+getLocalPanel().getHeight()/2);
         initVertices();
         initPolygon();
         createEntityView(getId(), getX(),getY(),getWidth(),getHeight());

     }

    private long lastShot;

    @Override
    public void route() {
        if (getAnchor().distance(EpsilonModel.getINSTANCE().getAnchor())< WYRM_DISTANCE) {
            shoot();
            goRoundEpsilon();
//            Point2D routePoint = goToNearestEdge();
//            if (routePoint.getX() == 0 && routePoint.getY() == 0) {
//                shoot();
//            } else {
//                move((int) routePoint.getX(), (int) routePoint.getY());
//            }
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

    private void goRoundEpsilon() {
        var epsilonModel = EpsilonModel.getINSTANCE();
//        double rotationRadius = getAnchor().distance(epsilonModel.getAnchor());
        double degree = Math.atan2( this.getYO() - epsilonModel.getYO(),this.getXO() - epsilonModel.getXO());
        degree += rotationSpeed;
        int finalX =epsilonModel.getXO() + (int)(WYRM_DISTANCE*0.8 * Math.cos(degree)) - getRadius();
        int finalY = epsilonModel.getYO() + (int) (WYRM_DISTANCE*0.8 * Math.sin(degree)) - getRadius();
        move(finalX - getX(), finalY - getY());
    }

    @Override
    public Point2D getRoutePoint() {
        return routePoint(this.getAnchor(),
                EpsilonModel.getINSTANCE().getAnchor(), false);
    }
    @Override
    public void destroy() {
        super.destroy();
        getLocalPanel().setEnabled(false);
        gamePanels.remove(getLocalPanel());
        getGameFrame().getLayeredPane().remove(getLocalPanel());
    }


    @Override
    void initVertices() {

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
