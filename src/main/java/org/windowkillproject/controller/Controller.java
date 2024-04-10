package org.windowkillproject.controller;

import org.windowkillproject.model.entities.EntityModel;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Objects;

public abstract class Controller {
    private static final HashMap<String, EntityModel> entityModelsMap = new HashMap<>();
    public static void createEntityView(String id){

        entityModelsMap.put(id, findModel(id));
//        Class ccc = Objects.requireNonNull(findModel(id)).getClass();
//        new ccc(id);
    }
    public static Point2D calculateViewLocation(Component component, String id){
        EntityModel entityModel= findModel(id);
        Point corner=new Point(component.getX(),component.getY());
        assert entityModel != null;
        // return relativeLocation(entityModel.getAnchor(),corner);
        return null; //todo
    }
    public static double getViewRadius(String id){
        return Objects.requireNonNull(findModel(id)).getRadius();
    }
    public static EntityModel findModel(String id){
        for (EntityModel entityModel: EntityModel.entityModels){
            if (entityModel.getId().equals(id)) return entityModel;
        }
        return null;
    }
}
