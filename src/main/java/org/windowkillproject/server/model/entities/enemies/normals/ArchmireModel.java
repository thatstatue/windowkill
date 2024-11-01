package org.windowkillproject.server.model.entities.enemies.normals;

import org.windowkillproject.server.model.entities.Circular;
import org.windowkillproject.server.model.entities.EntityModel;
import org.windowkillproject.server.model.entities.enemies.EnemyModel;
import org.windowkillproject.server.model.entities.enemies.attackstypes.AoEAttacker;
import org.windowkillproject.server.model.entities.enemies.attackstypes.Hovering;
import org.windowkillproject.server.model.globe.GlobeModel;
import org.windowkillproject.server.model.panelmodels.PanelModel;

import java.awt.geom.Point2D;

import static org.windowkillproject.server.Config.ARCHMIRE_RADIUS;
import static org.windowkillproject.server.Config.MAX_ENEMY_SPEED;
import static org.windowkillproject.controller.Utils.*;

public class ArchmireModel extends EnemyModel implements Hovering, AoEAttacker, Circular {
    public ArchmireModel(String globeId,PanelModel localPanel, int x, int y) {
        super(globeId, localPanel, x, y,ARCHMIRE_RADIUS,30,0,5,6);
        getGlobeModel().getArchmireModels().add(this);
    }
    @Override
    public void route() {
        int x= getX(); int y= getY();
        move((int) getRoutePoint().getX(), (int) getRoutePoint().getY());
        AOE();
        moveBGPanel(x, y);

    }


    @Override
    public Point2D getRoutePoint() {
        return globalRoutePoint(this.getAnchor(),targetEpsilon.getAnchor(), MAX_ENEMY_SPEED-1);
    }

    @Override
    protected void initVertices() {

    }

    @Override
    public void AOE() {
        AoEAttacker.super.AOE();

    }
    public boolean drown(EntityModel entityModel){
        int drownAttackHP = 10;
        if (isTransferableInBounds(entityModel, getAnchor(), getRadius(), false)){
            entityModel.gotHit(drownAttackHP);
            return true;
        }
        return false;
    }

    @Override
    public int getAoEAttackHP() {
        return 2;
    }

    @Override
    public Point2D getMoment() {
        return getAnchor();
    }

    @Override
    public int getInitSecond() {
        return getGlobeModel().getElapsedTime().getTotalSeconds();
    }

    @Override
    public void destroy(){
        super.destroy();
        getGlobeModel().getArchmireModels().remove(this);
    }
}
