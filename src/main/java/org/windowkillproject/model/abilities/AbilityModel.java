package org.windowkillproject.model.abilities;

import org.windowkillproject.model.Drawable;

import java.awt.geom.Point2D;
import java.util.UUID;

import static org.windowkillproject.application.Config.EPSILON_RADIUS;

public abstract class AbilityModel implements Drawable {
    protected int x, y;
    String id;

    private Point2D anchor;

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
        anchor = new Point2D.Double(x+ EPSILON_RADIUS/2, y + EPSILON_RADIUS*1.5);
        System.out.println(anchor);
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


}
//todo: add collectable