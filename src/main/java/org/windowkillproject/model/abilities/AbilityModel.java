package org.windowkillproject.model.abilities;

import org.windowkillproject.model.Drawable;
import org.windowkillproject.model.entities.EpsilonModel;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.UUID;


public abstract class AbilityModel implements Drawable {
    protected int x, y;
    protected String id;
    public static ArrayList<AbilityModel> abilityModels = new ArrayList<>();


    protected Point2D anchor;

    public Point2D getAnchor() {
        return anchor;
    }

    public void setAnchor(Point2D anchor) {
        this.anchor = anchor;
    }

    protected AbilityModel(int x, int y) {
        this.x = x;
        this.y = y;
        this.id = UUID.randomUUID().toString();
        anchor = new Point2D.Double(x,y);
        abilityModels.add(this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    public boolean isCollectedByEpsilon() {
        EpsilonModel epsilonModel = EpsilonModel.getINSTANCE();
        return (Math.abs(getX() - epsilonModel.getXO()) <= epsilonModel.getRadius()
                && Math.abs(getY() - epsilonModel.getYO()) <= epsilonModel.getRadius());
    }
    public void destroy() {
        abilityModels.remove(this);
    }


}
//todo: add collectable