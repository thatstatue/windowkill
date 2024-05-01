package org.windowkillproject.controller;

import org.windowkillproject.model.Drawable;
import org.windowkillproject.model.abilities.AbilityModel;
import org.windowkillproject.model.entities.EntityModel;
import org.windowkillproject.model.entities.EpsilonModel;
import org.windowkillproject.model.entities.enemies.EnemyModel;
import org.windowkillproject.model.entities.enemies.SquarantineModel;
import org.windowkillproject.model.entities.enemies.TrigorathModel;
import org.windowkillproject.view.*;
import org.windowkillproject.view.abilities.AbilityView;
import org.windowkillproject.view.entities.enemies.EnemyView;
import org.windowkillproject.view.entities.EpsilonView;
import org.windowkillproject.view.entities.enemies.SquarantineView;
import org.windowkillproject.view.entities.enemies.TrigorathView;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.windowkillproject.model.abilities.AbilityModel.abilityModels;
import static org.windowkillproject.model.entities.EntityModel.entityModels;


public abstract class Controller {
    public static void createEntityView(String id) {
        var entityModel = findModel(id);
        if (entityModel instanceof EnemyModel) {
            if (entityModel instanceof TrigorathModel) {
                var trigorathModel = (TrigorathModel) entityModel;
                var trigorathView = new TrigorathView(id);
                trigorathView.set(
                        trigorathModel.getX(),
                        trigorathModel.getY(),
                        trigorathModel.getWidth(),
                        trigorathModel.getHeight()
                );
            } else if (entityModel instanceof SquarantineModel) {
                var squarantineModel = (SquarantineModel) entityModel;
                var squarantineView = new SquarantineView(id);
                squarantineView.set(
                        squarantineModel.getX(),
                        squarantineModel.getY(),
                        squarantineModel.getWidth(),
                        squarantineModel.getHeight()
                );
            }
        } else {
            var epsilonModel = (EpsilonModel) entityModel;
            var epsilonView = new EpsilonView(id);
            assert epsilonModel != null;
            epsilonView.set(
                    epsilonModel.getX(),
                    epsilonModel.getY(),
                    epsilonModel.getWidth(),
                    epsilonModel.getHeight()
            );
        }
    }

    public static <T extends AbilityView> void createAbilityView(Class<T> tClass, String id, int x, int y) {
        try {
            Constructor<T> constructor = tClass.getConstructor(String.class, int.class, int.class);
            constructor.newInstance(id, x, y);
        } catch (NoSuchMethodException | InstantiationException |
                 InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends Viewable> void setViewBounds(T view) {
        if (findModel(view.getId()) instanceof EntityModel) {
            EntityModel entityModel = findModel(view.getId());
            if (entityModel != null) {
                view.set(
                        entityModel.getX(),
                        entityModel.getY(),
                        entityModel.getWidth(),
                        entityModel.getHeight()
                );
                if (entityModel instanceof EnemyModel) {
                    var enemyView = (EnemyView) view;
                    enemyView.setPolygon(((EnemyModel) entityModel).getPolygon());
                }
            } else view.setEnabled(false);
        } else if (findModel(view.getId()) instanceof AbilityModel) {
            AbilityModel abilityModel = findModel(view.getId());
            if (abilityModel != null) {
                view.set(
                        abilityModel.getX(),
                        abilityModel.getY(),
                        5, 5);
            } else view.setEnabled(false);
        } else {
            view.setEnabled(false);
        }
    }

    public static <T extends Drawable> T findModel(String id) {
        for (EntityModel entityModel : entityModels) {
            if (entityModel.getId().equals(id)) return (T) entityModel;
        }
        for (int i = 0; i < abilityModels.size(); i++) {
            AbilityModel abilityModel = abilityModels.get(i);
            if (abilityModel.getId().equals(id)) return (T) abilityModel;
        }
        return null;
    }
}
