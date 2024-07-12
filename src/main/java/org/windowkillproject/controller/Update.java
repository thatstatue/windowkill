package org.windowkillproject.controller;


import javax.swing.*;

import org.windowkillproject.application.panels.game.GamePanel;
import org.windowkillproject.model.Wave;
import org.windowkillproject.model.entities.EntityModel;
import org.windowkillproject.view.abilities.AbilityView;
import org.windowkillproject.view.entities.EntityView;

import static org.windowkillproject.application.Application.getGameFrame;
import static org.windowkillproject.application.Config.*;
import static org.windowkillproject.application.panels.game.GamePanel.gamePanels;
import static org.windowkillproject.application.panels.game.GamePanel.gamePanelsBounds;
import static org.windowkillproject.controller.Controller.setViewBounds;
import static org.windowkillproject.controller.GameController.*;
import static org.windowkillproject.model.Wave.isBetweenWaves;
import static org.windowkillproject.model.abilities.BulletModel.bulletModels;
import static org.windowkillproject.model.abilities.ProjectileModel.projectileModels;
import static org.windowkillproject.model.entities.EntityModel.entityModels;
import static org.windowkillproject.model.entities.enemies.attackstypes.AoEAttacker.MOMENT_MODELS;
import static org.windowkillproject.view.abilities.AbilityView.abilityViews;
import static org.windowkillproject.view.entities.EntityView.entityViews;

public class Update {
    public static Timer modelUpdateTimer;
    public static Timer frameUpdateTimer;

    public Update() {
        modelUpdateTimer = new Timer((int) MODEL_UPDATE_TIME, e -> updateModel()) {{
            setCoalesce(true);
        }};
        modelUpdateTimer.start();
        frameUpdateTimer = new Timer((int) FRAME_UPDATE_TIME, e -> updateView()) {{
            setCoalesce(true);
        }};
        frameUpdateTimer.start();
        new Wave();
    }

    public void updateView() {

        for (int i = 0; i < abilityViews.size(); i++) {
            AbilityView abilityView = abilityViews.get(i);
            setViewBounds(abilityView);
            if (!abilityView.isEnabled()) abilityViews.remove(abilityView);
        }

        for (int i = 0; i < entityViews.size(); i++) {
            EntityView entityView = entityViews.get(i);
            setViewBounds(entityView);
            if (!entityView.isEnabled()) entityViews.remove(entityView);
        }

        getGameFrame().revalidate();
        //todo uncomment for repainting
//        gamePanelsBounds.forEach((gamePanel, rectangle) -> {
//            gamePanel.revalidate();
//            getGameFrame().getLayeredPane().repaint(rectangle);
//        });
        getGameFrame().repaint();
    }

    public void updateModel() {
        if (!isBetweenWaves()) getGameFrame().shrink();
        for (int i = 0; i < entityModels.size(); i++) {
            EntityModel entityModel = entityModels.get(i);
            entityModel.rotate();
            if (!entityModel.isImpact()) entityModel.route();
        }
        for (int i = 0; i < bulletModels.size(); i++) {
            bulletModels.get(i).move();
        }
        for (int i = 0; i < projectileModels.size(); i++) {
            projectileModels.get(i).move();
        }

        areaOfEffectControl();
        specialtiesControl();
        writControl();
        epsilonRewardControl();
        epsilonIntersectionControl();
        enemyIntersectionControl();

        keepEpsilonInBounds();
//        keepEpsilonInBounds();
//        hideEntitiesOutsideBounds();
        if (Wave.isStartNewWave()) new Wave();


    }

}
