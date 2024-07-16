package org.windowkillproject.model.entities.enemies.normals;

import org.windowkillproject.application.panels.game.GamePanel;
import org.windowkillproject.model.entities.Circular;
import org.windowkillproject.model.entities.EntityModel;
import org.windowkillproject.model.entities.EpsilonModel;
import org.windowkillproject.model.entities.enemies.EnemyModel;
import org.windowkillproject.model.entities.enemies.attackstypes.AoEAttacker;
import org.windowkillproject.model.entities.enemies.attackstypes.Hovering;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import static org.windowkillproject.application.Config.ARCHMIRE_RADIUS;
import static org.windowkillproject.application.Config.MAX_ENEMY_SPEED;
import static org.windowkillproject.controller.Utils.*;

public class ArchmireModel extends EnemyModel implements Hovering, AoEAttacker, Circular {
    public ArchmireModel(GamePanel localPanel, int x, int y) {
        super(localPanel, x, y,ARCHMIRE_RADIUS,30,0,5,6);
        archmireModels.add(this);
    }
    public static ArrayList<ArchmireModel> archmireModels = new ArrayList<>();
    @Override
    public void route() {
        int x= getX(); int y= getY();
        move((int) getRoutePoint().getX(), (int) getRoutePoint().getY());
        AOE();
        moveBGPanel(x, y);

    }


    @Override
    public Point2D getRoutePoint() {
        return globalRoutePoint(this.getAnchor(), EpsilonModel.getINSTANCE().getAnchor(), MAX_ENEMY_SPEED-1);
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
    public void destroy(){
        super.destroy();
        archmireModels.remove(this);
    }
}
