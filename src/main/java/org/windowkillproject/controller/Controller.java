package org.windowkillproject.controller;

import org.windowkillproject.model.Drawable;
import org.windowkillproject.model.abilities.AbilityModel;
import org.windowkillproject.model.abilities.BulletModel;
import org.windowkillproject.model.entities.EntityModel;
import org.windowkillproject.model.entities.EpsilonModel;
import org.windowkillproject.model.entities.enemies.EnemyModel;
import org.windowkillproject.model.entities.enemies.TrigorathModel;
import org.windowkillproject.view.*;

import static org.windowkillproject.model.abilities.BulletModel.bulletModels;
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
            }//todo add square
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

    public static void createBulletView(String id, int x, int y) {
        new BulletView(id, x, y);
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
            }
        } else if (findModel(view.getId()) instanceof AbilityModel) {
            AbilityModel abilityModel = findModel(view.getId());
            if (abilityModel != null) {
                view.set(
                        abilityModel.getX(),
                        abilityModel.getY(),
                        5, 5);
            }
        } else {
            view.setEnabled(false);
        }
    }

    public static <T extends Drawable> T findModel(String id) {
        for (EntityModel entityModel : entityModels) {
            if (entityModel.getId().equals(id)) return (T) entityModel;
        }
        for (BulletModel bulletModel : bulletModels) {
            if (bulletModel.getId().equals(id)) return (T) bulletModel;
        }
        return null;
    }
}