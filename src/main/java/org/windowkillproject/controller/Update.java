package org.windowkillproject.controller;


import javax.swing.*;

import org.windowkillproject.model.abilities.BulletModel;
import org.windowkillproject.model.entities.EntityModel;
import org.windowkillproject.model.entities.enemies.EnemyModel;
import org.windowkillproject.view.BulletView;
import org.windowkillproject.view.EntityView;

import static org.windowkillproject.application.Application.gameFrame;
import static org.windowkillproject.application.Config.FRAME_UPDATE_TIME;
import static org.windowkillproject.application.Config.MODEL_UPDATE_TIME;
import static org.windowkillproject.controller.Controller.setViewBounds;
import static org.windowkillproject.controller.GameController.enemyIntersectionControl;
import static org.windowkillproject.controller.GameController.epsilonIntersectionControl;
import static org.windowkillproject.model.abilities.BulletModel.bulletModels;
import static org.windowkillproject.model.entities.EntityModel.entityModels;
import static org.windowkillproject.view.BulletView.bulletViews;
import static org.windowkillproject.view.EntityView.entityViews;

public class Update {
    public Update() {
        gameFrame.shrinkFast();
        new Timer((int) FRAME_UPDATE_TIME, e -> updateView()) {{
            setCoalesce(true);
        }}.start();
        new Timer((int) MODEL_UPDATE_TIME, e -> updateModel()) {{
            setCoalesce(true);
        }}.start();
    }

    public void updateView() {
        for (int i = 0 ; i < entityViews.size(); i++ ) {
            EntityView entityView = entityViews.get(i);
            setViewBounds(entityView);
            if (!entityView.isEnabled()) entityViews.remove(entityView);
        }
        for (int i = 0; i < bulletViews.size(); i++) {
            BulletView bulletView = bulletViews.get(i);
            setViewBounds(bulletView);
            if (!bulletView.isEnabled()) bulletViews.remove(bulletView);
        }
        gameFrame.repaint();
    }

    public void updateModel() {
        gameFrame.shrink();
        var gamePanel = gameFrame.getGamePanel();
        for (EntityModel entityModel : entityModels) {
            entityModel.rotate();
            if (entityModel instanceof EnemyModel) {
                EnemyModel enemyModel = (EnemyModel) entityModel;
                enemyModel.route();
            }
        }
        for (int i = 0; i < bulletModels.size(); i++) {
            bulletModels.get(i).move();
        }
        gamePanel.keepEpsilonInBounds();

        enemyIntersectionControl();
        epsilonIntersectionControl();

        for (EntityModel entityModel : entityModels) {
            //  entityModel.setLocation(calculateViewLocation(gameFrame.getGamePanel(), entityView.getId()));
        }
    }
}
