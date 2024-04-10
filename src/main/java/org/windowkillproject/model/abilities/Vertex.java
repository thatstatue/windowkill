package org.windowkillproject.model.abilities;

import org.windowkillproject.model.entities.EntityModel;

public class Vertex extends Ability {
    private final EntityModel parentEntityModel;

    public Vertex(int x, int y, EntityModel parentEntityModel) {
        super(x, y);
        this.parentEntityModel = parentEntityModel;
        //setImg(ImgData.getData().getVertex());
    }
    public void rotate(){
        double degree = Math.atan2( this.getY() - parentEntityModel.getYO(),this.getX() - parentEntityModel.getXO());
        degree += 0.084;
        setY(parentEntityModel.getYO() + (int) (parentEntityModel.getRadius() * Math.sin(degree)));
        setX(parentEntityModel.getXO() + (int)(parentEntityModel.getRadius() * Math.cos(degree)));
    }

}
