package org.windowkillproject.controller;


import javax.swing.*;

import org.windowkillproject.server.model.entities.EntityModel;
import org.windowkillproject.server.model.entities.EpsilonModel;
import org.windowkillproject.server.model.entities.enemies.attackstypes.Hovering;
import org.windowkillproject.client.view.abilities.AbilityView;
import org.windowkillproject.client.view.entities.EntityView;
import org.windowkillproject.server.model.globe.GlobeModel;

import static org.windowkillproject.Constants.FRAME_UPDATE_TIME;
import static org.windowkillproject.Constants.MODEL_UPDATE_TIME;
import static org.windowkillproject.Request.REQ_REPAINT_GAME_FRAME;
import static org.windowkillproject.client.ui.panels.game.PanelView.panelViews;
import static org.windowkillproject.controller.GameManager.*;
import static org.windowkillproject.server.model.abilities.BulletModel.bulletModels;
import static org.windowkillproject.server.model.abilities.ProjectileModel.projectileModels;
import static org.windowkillproject.server.model.entities.EntityModel.entityModels;
import static org.windowkillproject.client.view.abilities.AbilityView.abilityViews;
import static org.windowkillproject.client.view.entities.EntityView.entityViews;

public class GameLoop {
    public Timer modelUpdateTimer;
    public Timer frameUpdateTimer;
    public EmptyPanelEraser emptyPanelEraser;
    private GlobeModel globeModel;

    public GameLoop(GlobeModel globeModel) {
        this.globeModel = globeModel;
        modelUpdateTimer = new Timer((int) MODEL_UPDATE_TIME, e -> updateModel()) {{
            setCoalesce(true);
        }};
        frameUpdateTimer = new Timer((int) FRAME_UPDATE_TIME, e -> updateView()) {{
            setCoalesce(true);
        }};
        emptyPanelEraser = new EmptyPanelEraser(globeModel);
    }
    public void start(){
        modelUpdateTimer.start();
        frameUpdateTimer.start();
        globeModel.getWaveFactory().start();
        emptyPanelEraser.start();
    }
    public void stop(){
        modelUpdateTimer.stop();
        frameUpdateTimer.stop();
        globeModel.getWaveFactory().stop();
        emptyPanelEraser.start();
    }
    public void updateView() {
        for (int i =0 ; i< panelViews.size(); i++){ //todo how does it work
            var panelView = panelViews.get(i);
            globeModel.getGlobeController().setViewBounds(panelView);
            panelView.revalidate();
            panelView.repaint();
        }

        for (int i = 0; i < abilityViews.size(); i++) {
            AbilityView abilityView = abilityViews.get(i);
            globeModel.getGlobeController().setViewBounds(abilityView);
            if (!abilityView.isEnabled()) abilityViews.remove(abilityView);
        }

        for (int i = 0; i < entityViews.size(); i++) {
            EntityView entityView = entityViews.get(i);
            globeModel.getGlobeController().setViewBounds(entityView);
            if (!entityView.isEnabled()) entityViews.remove(entityView);
        }

        globeModel.performAction(REQ_REPAINT_GAME_FRAME);
        //todo uncomment for repainting
//        gamePanelsBounds.forEach((panelView, rectangle) -> {
//            panelView.revalidate();
//            getGameFrame().getLayeredPane().repaint(rectangle);
//        });
    }

    public void updateModel() {
        if (!globeModel.getWaveFactory().isBetweenWaves()) globeModel.shrinkAll();
        for (int i = 0; i < entityModels.size(); i++) {
            EntityModel entityModel = entityModels.get(i);
            entityModel.rotate();
            if (!entityModel.isImpact()) {
                if (entityModel instanceof Hovering || entityModel instanceof EpsilonModel ||
                        globeModel.getElapsedTime().getTotalSeconds() - hoverAwayInitSeconds > 10) {
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


        globeModel.getGameManager().areaOfEffectControl();
        globeModel.getGameManager().specialtiesControl();
        globeModel.getGameManager().writControl();
        globeModel.getGameManager().epsilonsRewardControl();
        globeModel.getGameManager().epsilonIntersectionControl();
        globeModel.getGameManager().enemyIntersectionControl();

        var waveTimer = globeModel.getWaveFactory().waveTimer;
        if (globeModel.getElapsedTime().getTotalSeconds() - pauseInitSeconds < 12) {
            if (globeModel.getElapsedTime().getTotalSeconds() - pauseInitSeconds < 10) {
                if (waveTimer.isRunning()) waveTimer.stop();
            }else if (!waveTimer.isRunning()) {
                waveTimer.start();
                pauseInitSeconds = -200;
            }

        }
        globeModel.getGameManager().keepTransferableInBounds();

    }

}
