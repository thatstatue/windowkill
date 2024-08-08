package org.windowkillproject.server.model.entities.enemies.finalboss;


import org.windowkillproject.server.Config;
import org.windowkillproject.server.model.globe.GlobeModel;
import org.windowkillproject.server.model.panelmodels.InternalPanelModel;
import org.windowkillproject.server.model.panelmodels.PanelStatus;
import org.windowkillproject.server.model.abilities.ProjectileModel;
import org.windowkillproject.server.model.abilities.VertexModel;
import org.windowkillproject.server.model.entities.enemies.EnemyModel;
import org.windowkillproject.server.model.entities.enemies.attackstypes.NonRotatable;
import org.windowkillproject.server.model.entities.enemies.attackstypes.ProjectileOperator;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import static org.windowkillproject.Constants.*;
import static org.windowkillproject.server.Config.*;
import static org.windowkillproject.controller.Utils.globalRoutePoint;

public class HandModel extends EnemyModel implements ProjectileOperator, NonRotatable {
    private static ArrayList<HandModel> hands = new ArrayList<>();

    protected HandModel(String globeId, int x, int y) {
        super(globeId, null, x, y, Config.HAND_RADIUS, 100, 10, 10, 10);
        setLocalPanelModel(new InternalPanelModel(globeId,new Rectangle(x, y,
                HAND_RADIUS * 3, HAND_RADIUS * 3),PanelStatus.isometric , true
        ));
        setBgPanel((InternalPanelModel) getLocalPanelModel());
        initVertices();
        targetEpsilon = getGlobeModel().getSmileyHeadModel().getTargetEpsilon();
        //globeModel.getGlobeController().createEntityView(getId(), getX(), getY(), getWidth(), getHeight());
        hands.add(this);

    }

    public void squeeze() {
        if (hands.size() == 2) {
            setVulnerable(false);
            getLocalPanelModel().setFlexible(false);
            var main = getGlobeModel().getMainPanelModel();
            Point2D panelLeft = new Point2D.Double(main.getX()-2, main.getY()+main.getHeight()/2D);
            Point2D panelRight = new Point2D.Double(main.getX()+main.getWidth()+2, main.getY()+main.getHeight()/2D);

            if(this instanceof RightHandModel && getAnchor().distance(panelRight)> getRadius()+3 ||
                    this instanceof LeftHandModel && getAnchor().distance(panelLeft) > getRadius()+3)
                move((int) getRoutePoint().getX(), (int) getRoutePoint().getY());
//            }
        }
    }

    private long lastShot;

    @Override
    public void route() {


        getLocalPanelModel().setX((int) (getXO() - getRadius() * 1.5));
        getLocalPanelModel().setY((int) (getYO() - getRadius() * 1.5));
    }

    public void projectile() {
        if (!hands.isEmpty()) {
            setVulnerable(true);
            shoot();
            goRoundEpsilon();
        }
    }

    @Override
    public void shoot() {
        if (getGlobeModel().getElapsedTime().getTotalSeconds() - lastShot > PROJECTILE_TIMEOUT) {
            new ProjectileModel(getLocalPanelModel(), this, 4, true, targetEpsilon,
                    Color.yellow, Color.gray).shoot();
            lastShot = getGlobeModel().getElapsedTime().getTotalSeconds();
        }
    }

    private double rotationSpeed = UNIT_DEGREE / 4;


    public void setMinusRotationSpeed() {
        this.rotationSpeed = -rotationSpeed;
    }

    private void goRoundEpsilon() {
        var epsilonModel = targetEpsilon;
        double degree = Math.atan2(this.getYO() - epsilonModel.getYO(), this.getXO() - epsilonModel.getXO());
        degree += rotationSpeed;
        int finalX = epsilonModel.getXO() + (int) (WYRM_DISTANCE * 0.8 * Math.cos(degree)) - getRadius();
        int finalY = epsilonModel.getYO() + (int) (WYRM_DISTANCE * 0.8 * Math.sin(degree)) - getRadius();
        move(finalX - getX(), finalY - getY());
    }

    @Override
    public Point2D getRoutePoint() {
        int panelWidth = getBgPanel().getWidth();
        int panelHeight = getBgPanel().getHeight();
        var bounds = getGlobeModel().getMainPanelModel().getBounds();
        int goX = bounds.x - panelWidth -15;
        if (this instanceof RightHandModel)
            goX += bounds.width + panelWidth + 15;

        int goY = bounds.y + (bounds.height - panelHeight) / 2;
        return globalRoutePoint(new Point2D.Double(getX(), getY()),
                new Point2D.Double(goX, goY), MAX_ENEMY_SPEED*2);
    }

    @Override
    public void destroy() {
        super.destroy();
        hands.remove(this);
        getGlobeModel().getGameManager().deleteGamePanel(getLocalPanelModel());
    }

    private boolean vulnerable;

    @Override
    public void gotHit(int attackHp) {
        if (vulnerable) {
            super.gotHit(attackHp);
        }
    }

    @Override
    public void gotShot(int attackHP) {
        if (vulnerable) {
            super.gotShot(attackHP);
        }
    }

    public boolean isVulnerable() {
        return vulnerable;
    }

    public void setVulnerable(boolean vulnerable) {
        this.vulnerable = vulnerable;
    }


    @Override
    protected void initVertices() {
        int halfSideLength = (int) (getRadius() / Math.sqrt(2) + 14);


        getVertices().add(new VertexModel(getXO() - halfSideLength, getYO() - halfSideLength, this));
        getVertices().add(new VertexModel(getXO() + halfSideLength, getYO() - halfSideLength, this));
        getVertices().add(new VertexModel(getXO() + halfSideLength, getYO() + halfSideLength, this));
        getVertices().add(new VertexModel(getXO() - halfSideLength, getYO() + halfSideLength, this));

    }

}
