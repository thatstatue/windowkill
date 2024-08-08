package org.windowkillproject.controller;


import javax.swing.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.windowkillproject.server.model.abilities.AbilityModel;
import org.windowkillproject.server.model.entities.EntityModel;
import org.windowkillproject.server.model.entities.EpsilonModel;
import org.windowkillproject.server.model.entities.enemies.attackstypes.Hovering;
import org.windowkillproject.server.model.globe.GlobeModel;
import org.windowkillproject.server.model.panelmodels.PanelModel;

import static org.windowkillproject.Constants.FRAME_UPDATE_TIME;
import static org.windowkillproject.Constants.MODEL_UPDATE_TIME;
import static org.windowkillproject.Request.REQ_REPAINT_GAME_FRAME;
import static org.windowkillproject.controller.GameManager.*;

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
        globeModel.performAction(REQ_REPAINT_GAME_FRAME);
        //todo uncomment for repainting
//        gamePanelsBounds.forEach((panelView, rectangle) -> {
//            panelView.revalidate();
//            getGameFrame().getLayeredPane().repaint(rectangle);
//        });
    }

    public void updateModel() {
        if (!globeModel.getWaveFactory().isBetweenWaves()) globeModel.shrinkAll();
        routeAll();
        moveProjectable();
        controlAll();
        pauseControl();
        globeModel.getGameManager().keepTransferableInBounds();
        setViewBoundsForAll();
    }

    private void routeAll() {
        for (int i = 0; i < globeModel.getEntityModels().size(); i++) {
            EntityModel entityModel = globeModel.getEntityModels().get(i);
            entityModel.rotate();
            if (!entityModel.isImpact()) {
                if (entityModel instanceof Hovering || entityModel instanceof EpsilonModel ||
                        globeModel.getElapsedTime().getTotalSeconds() - hoverAwayInitSeconds > 10) {
                    entityModel.route();
                }
            }
        }
    }

    private void moveProjectable() {
        for (int i = 0; i < globeModel.getBulletModels().size(); i++) {
            globeModel.getBulletModels().get(i).move();
        }
        for (int i = 0; i < globeModel.getProjectileModels().size(); i++) {
            globeModel.getProjectileModels().get(i).move();
        }
    }

    private void pauseControl() {
        var waveTimer = globeModel.getWaveFactory().waveTimer;
        if (globeModel.getElapsedTime().getTotalSeconds() - pauseInitSeconds < 12) {
            if (globeModel.getElapsedTime().getTotalSeconds() - pauseInitSeconds < 10) {
                if (waveTimer.isRunning()) waveTimer.stop();
            }else if (!waveTimer.isRunning()) {
                waveTimer.start();
                pauseInitSeconds = -200;
            }

        }
    }

    private void controlAll() {
        globeModel.getGameManager().areaOfEffectControl();
        globeModel.getGameManager().specialtiesControl();
        globeModel.getGameManager().writControl();
        globeModel.getGameManager().epsilonsRewardControl();
        globeModel.getGameManager().epsilonIntersectionControl();
        globeModel.getGameManager().enemyIntersectionControl();
    }

    private void setViewBoundsForAll() {
        for (EntityModel entityModel: globeModel.getEntityModels()){
            System.out.println( entityModel.getId() + " entity is requesting modif");
            try {
                globeModel.getGlobeController().setViewBounds(entityModel);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        for (AbilityModel abilityModel: globeModel.getAbilityModels()){
            System.out.println( abilityModel.getId() + " ability is requesting modif");
            try {
                globeModel.getGlobeController().setViewBounds(abilityModel);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        for (PanelModel panelModel: globeModel.getPanelModels()){
//            System.out.println( panelModel.getId() + " panel is requesting modif");
            try {
                globeModel.getGlobeController().setViewBounds(panelModel);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
