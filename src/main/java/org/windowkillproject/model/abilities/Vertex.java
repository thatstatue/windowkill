package org.windowkillproject.model.abilities;

import org.windowkillproject.model.entities.EntityModel;

import java.awt.geom.Point2D;

public class Vertex extends AbilityModel {
    private final EntityModel parentEntityModel;
    private Point2D anchor;

    public Point2D getAnchor() {
        return anchor;
    }

    public Vertex(int x, int y, EntityModel parentEntityModel) {
        super(x, y);
        anchor = new Point2D.Float(x,y);
        this.parentEntityModel = parentEntityModel;
        //setImg(ImgData.getData().getVertex());
    }
    public void rotate(){
        double degree = Math.atan2( this.getY() - parentEntityModel.getYO(),this.getX() - parentEntityModel.getXO());
        degree += 0.084;
        setY(parentEntityModel.getYO() + (int) (parentEntityModel.getRadius() * Math.sin(degree)));
        setX(parentEntityModel.getXO() + (int)(parentEntityModel.getRadius() * Math.cos(degree)));
        anchor = new Point2D.Float(getX(), getY());
    }

}
