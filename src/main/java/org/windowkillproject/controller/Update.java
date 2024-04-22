package org.windowkillproject.controller;


import javax.swing.*;

import org.windowkillproject.model.Wave;
import org.windowkillproject.model.entities.EntityModel;
import org.windowkillproject.model.entities.enemies.EnemyModel;
import org.windowkillproject.view.abilities.AbilityView;
import org.windowkillproject.view.entities.EntityView;

import static org.windowkillproject.application.Application.gameFrame;
import static org.windowkillproject.application.Config.FRAME_UPDATE_TIME;
import static org.windowkillproject.application.Config.MODEL_UPDATE_TIME;
import static org.windowkillproject.controller.Controller.setViewBounds;
import static org.windowkillproject.controller.GameController.*;
import static org.windowkillproject.model.Wave.isBetweenWaves;
import static org.windowkillproject.model.Wave.spawnWave;
import static org.windowkillproject.model.abilities.BulletModel.bulletModels;
import static org.windowkillproject.model.entities.EntityModel.entityModels;
import static org.windowkillproject.view.abilities.AbilityView.abilityViews;
import static org.windowkillproject.view.entities.EntityView.entityViews;

public class Update {
    public static Timer modelUpdateTimer;
    public static Timer frameUpdateTimer;
    public Update() {
        spawnWave();
        modelUpdateTimer = new Timer((int) MODEL_UPDATE_TIME, e -> updateModel()) {{
            setCoalesce(true);
        }};
        modelUpdateTimer.start();
        frameUpdateTimer = new Timer((int) FRAME_UPDATE_TIME, e -> updateView()) {{
            setCoalesce(true);
        }};
        frameUpdateTimer.start();
    }

    public void updateView() {
        for (int i = 0; i < entityViews.size(); i++) {
            EntityView entityView = entityViews.get(i);
            setViewBounds(entityView);
            if (!entityView.isEnabled()) entityViews.remove(entityView);
        }
        for (int i = 0; i < abilityViews.size(); i++) {
            AbilityView abilityView = abilityViews.get(i);
            setViewBounds(abilityView);
            if (!abilityView.isEnabled()) abilityViews.remove(abilityView);
        }
        gameFrame.repaint();
    }

    public void updateModel() {
        if (!isBetweenWaves()) gameFrame.shrink();
        for (EntityModel entityModel : entityModels) {
            entityModel.rotate();
            if (!entityModel.isImpact()) entityModel.route();

        }
        for (int i = 0; i < bulletModels.size(); i++) {
            bulletModels.get(i).move();
        }
        keepEpsilonInBounds();
        specialtiesControl();
        epsilonRewardControl();
        enemyIntersectionControl();
        epsilonIntersectionControl();

    }
}
