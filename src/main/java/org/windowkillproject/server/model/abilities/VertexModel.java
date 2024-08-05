package org.windowkillproject.server.model.abilities;

import org.windowkillproject.server.model.entities.EntityModel;
import org.windowkillproject.server.model.entities.EpsilonModel;

import java.awt.geom.Point2D;


public class VertexModel extends AbilityModel {
    private final EntityModel parentEntityModel;
    public VertexModel( int x, int y, EntityModel parent) {
        super(parent.getGlobeModel(), parent.getLocalPanelModel(), x, y);
        this.parentEntityModel = parent;
        if (parentEntityModel instanceof EpsilonModel) globeModel.getGlobeController().createAbilityView(id, x, y);
    }

    public void rotate(double theta){
        double degree = Math.atan2( this.getY() - parentEntityModel.getYO(),this.getX() - parentEntityModel.getXO());
        degree += theta;
        setY(parentEntityModel.getYO() + (int) (parentEntityModel.getRadius() * Math.sin(degree)));
        setX(parentEntityModel.getXO() + (int)(parentEntityModel.getRadius() * Math.cos(degree)));
        anchor = new Point2D.Float(getX(), getY());
    }

}
