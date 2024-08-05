package org.windowkillproject.server.model.abilities;

import org.windowkillproject.server.model.globe.GlobeModel;

import java.awt.geom.Point2D;


public class MomentModel extends AbilityModel {
    public Point2D getCenterOfArea() {
        return centerOfArea;
    }

    public int getTime() {
        return time;
    }

    public MomentModel(GlobeModel globeModel,Point2D centerOfArea, int time, int radius, int attackHP) {
        super( globeModel,null, (int) (centerOfArea.getX()-radius), (int) (centerOfArea.getY()-radius));
        this.centerOfArea = centerOfArea;
        this.time = time;
        this.radius = radius;
        this.attackHP = attackHP;
        globeModel.getGlobeController().createAbilityView(getId(), getX(),getY());
    }

    public int getRadius() {
        return radius;
    }

    public int getAttackHP() {
        return attackHP;
    }

    private final Point2D centerOfArea;
    private final int time;
    private final int radius;
    private final int attackHP;
}
