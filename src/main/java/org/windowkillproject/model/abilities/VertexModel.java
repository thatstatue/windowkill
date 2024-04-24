package org.windowkillproject.model.abilities;

import org.windowkillproject.model.entities.EntityModel;
import org.windowkillproject.model.entities.EpsilonModel;
import org.windowkillproject.view.abilities.VertexView;

import java.awt.geom.Point2D;

import static org.windowkillproject.controller.Controller.createAbilityView;

public class VertexModel extends AbilityModel {
    private final EntityModel parentEntityModel;
    public VertexModel(int x, int y, EntityModel parentEntityModel) {
        super(x, y);
        this.parentEntityModel = parentEntityModel;
        if (parentEntityModel instanceof EpsilonModel) createAbilityView(VertexView.class, id, x, y);
        //setImg(ImgData.getData().getVertex());
    }
    public void rotate(){
        this.rotate(0/*.084*/);
    }
    public void rotate(double theta){
        double degree = Math.atan2( this.getY() - parentEntityModel.getYO(),this.getX() - parentEntityModel.getXO());
        degree += theta;
        setY(parentEntityModel.getYO() + (int) (parentEntityModel.getRadius() * Math.sin(degree)));
        setX(parentEntityModel.getXO() + (int)(parentEntityModel.getRadius() * Math.cos(degree)));
        anchor = new Point2D.Float(getX(), getY());
    }

}
