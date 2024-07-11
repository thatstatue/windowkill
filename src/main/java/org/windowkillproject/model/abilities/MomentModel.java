package org.windowkillproject.model.abilities;

import org.windowkillproject.application.panels.game.GamePanel;
import org.windowkillproject.model.ObjectModel;
import org.windowkillproject.model.abilities.AbilityModel;

import java.awt.geom.Point2D;

import static org.windowkillproject.controller.Controller.createAbilityView;

public class MomentModel extends AbilityModel {
    public Point2D getCenterOfArea() {
        return centerOfArea;
    }

    public int getTime() {
        return time;
    }

    public MomentModel(Point2D centerOfArea, int time, int radius, int attackHP) {
        super( null, (int) (centerOfArea.getX()-radius), (int) (centerOfArea.getY()-radius));
        this.centerOfArea = centerOfArea;
        this.time = time;
        this.radius = radius;
        this.attackHP = attackHP;
        createAbilityView(getId(), getX(),getY());
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
