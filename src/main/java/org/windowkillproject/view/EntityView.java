package org.windowkillproject.view;

import javax.swing.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class EntityView extends JLabel {
    protected BufferedImage img;
    String id;
    Point2D location=new Point2D.Double(0,0);
    double radius;
    public static ArrayList<EntityView> entityViews =new ArrayList<>();
    public EntityView(String id) {
        this.id = id;
        entityViews.add(this);
    }

    public String getId() {
        return id;
    }

    public Point2D getLoc() {
        return location;
    }

    public void setLocation(Point2D location) {
        this.location = location;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }


    public BufferedImage getImg() {
        return img;
    }

    public void setImg(BufferedImage img) {
        this.img = img;
    }
}
