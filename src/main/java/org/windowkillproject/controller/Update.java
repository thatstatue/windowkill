package org.windowkillproject.controller;


import javax.swing.*;

import org.windowkillproject.server.model.Wave;
import org.windowkillproject.server.model.entities.EntityModel;
import org.windowkillproject.server.model.entities.EpsilonModel;
import org.windowkillproject.server.model.entities.enemies.attackstypes.Hovering;
import org.windowkillproject.client.view.abilities.AbilityView;
import org.windowkillproject.client.view.entities.EntityView;

import static org.windowkillproject.client.ui.App.getGameFrame;
import static org.windowkillproject.server.Config.*;
import static org.windowkillproject.controller.Controller.setViewBounds;
import static org.windowkillproject.controller.ElapsedTime.getTotalSeconds;
import static org.windowkillproject.controller.GameController.*;
import static org.windowkillproject.server.model.Wave.isBetweenWaves;
import static org.windowkillproject.server.model.Wave.waveTimer;
import static org.windowkillproject.server.model.abilities.BulletModel.bulletModels;
import static org.windowkillproject.server.model.abilities.ProjectileModel.projectileModels;
import static org.windowkillproject.server.model.entities.EntityModel.entityModels;
import static org.windowkillproject.client.view.abilities.AbilityView.abilityViews;
import static org.windowkillproject.client.view.entities.EntityView.entityViews;

public class Update {
    public static Timer modelUpdateTimer;
    public static Timer frameUpdateTimer;
    public static EmptyPanelEraser emptyPanelEraser;

    public Update() {
        modelUpdateTimer = new Timer((int) MODEL_UPDATE_TIME, e -> updateModel()) {{
            setCoalesce(true);
        }};
        modelUpdateTimer.start();
        frameUpdateTimer = new Timer((int) FRAME_UPDATE_TIME, e -> updateView()) {{
            setCoalesce(true);
        }};
        frameUpdateTimer.start();

        emptyPanelEraser = new EmptyPanelEraser();
        emptyPanelEraser.start();
        new Wave();
    }

    public static void updateView() {

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
            if (!entityModel.isImpact()) {
                if (entityModel instanceof Hovering || entityModel instanceof EpsilonModel ||
                        getTotalSeconds() - hoverAwayInitSeconds > 10) {
                    entityModel.route();
                }
            }
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

        if (getTotalSeconds() - pauseInitSeconds < 12) {
            if (getTotalSeconds() - pauseInitSeconds < 10) {
                if (waveTimer.isRunning()) waveTimer.stop();
            }else if (!waveTimer.isRunning()) {
                waveTimer.start();
                pauseInitSeconds = -200;
            }

        }


        keepTransferableInBounds();
        if (Wave.isStartNewWave()) new Wave();


    }

}
