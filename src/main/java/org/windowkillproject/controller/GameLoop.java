package org.windowkillproject.controller;


import javax.swing.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.windowkillproject.server.model.abilities.AbilityModel;
import org.windowkillproject.server.model.entities.EntityModel;
import org.windowkillproject.server.model.entities.EpsilonModel;
import org.windowkillproject.server.model.entities.enemies.attackstypes.Hovering;
import org.windowkillproject.server.model.globe.GlobeModel;
import org.windowkillproject.server.model.panelmodels.PanelModel;

import java.util.ArrayList;

import static org.windowkillproject.Constants.FRAME_UPDATE_TIME;
import static org.windowkillproject.Constants.MODEL_UPDATE_TIME;
import static org.windowkillproject.Request.REQ_REPAINT_GAME_FRAME;
import static org.windowkillproject.controller.GameManager.*;
import static org.windowkillproject.server.model.globe.GlobesManager.getGlobeFromId;

public class GameLoop {
    public Timer modelUpdateTimer;
    public Timer frameUpdateTimer;
    public EmptyPanelEraser emptyPanelEraser;
    private String globeId;

    public GameLoop(String globeId) {
        this.globeId = globeId;
        modelUpdateTimer = new Timer((int) MODEL_UPDATE_TIME, e -> updateModel()) {{
            setCoalesce(true);
        }};
        frameUpdateTimer = new Timer((int) FRAME_UPDATE_TIME, e -> updateView()) {{
            setCoalesce(true);
        }};
        emptyPanelEraser = new EmptyPanelEraser(globeId);

    }
    public GlobeModel getGlobeModel(){
        return getGlobeFromId(globeId);
    }
    public void start(){
        modelUpdateTimer.start();
        frameUpdateTimer.start();
        getGlobeModel().getWaveFactory().start();
        emptyPanelEraser.start();
    }
    public void stop(){
        modelUpdateTimer.stop();
        frameUpdateTimer.stop();
        getGlobeModel().getWaveFactory().stop();
        emptyPanelEraser.start();
    }
    public void updateView() {
        getGlobeModel().performAction(REQ_REPAINT_GAME_FRAME);
        //todo uncomment for repainting
//        gamePanelsBounds.forEach((panelView, rectangle) -> {
//            panelView.revalidate();
//            getGameFrame().getLayeredPane().repaint(rectangle);
//        });
    }

    public void updateModel() {
        if (!getGlobeModel().getWaveFactory().isBetweenWaves()) getGlobeModel().shrinkAll();
        routeAll();
        moveProjectable();
        controlAll();
        pauseControl();
        getGlobeModel().getGameManager().keepTransferableInBounds();
        setViewBoundsForAll();
    }

    private void routeAll() {
        for (int i = 0; i < getGlobeModel().getEntityModels().size(); i++) {
            EntityModel entityModel = getGlobeModel().getEntityModels().get(i);
            entityModel.rotate();
            if (!entityModel.isImpact()) {
                if (entityModel instanceof Hovering || entityModel instanceof EpsilonModel ||
                        getGlobeModel().getElapsedTime().getTotalSeconds() - hoverAwayInitSeconds > 10) {
                    entityModel.route();
                }
            }
        }
    }

    private void moveProjectable() {
        var globeModel = getGlobeModel();
        for (int i = 0; i < globeModel.getBulletModels().size(); i++) {
            globeModel.getBulletModels().get(i).move();
        }
        for (int i = 0; i < globeModel.getProjectileModels().size(); i++) {
            globeModel.getProjectileModels().get(i).move();
        }
    }

    private void pauseControl() {
        var globeModel = getGlobeModel();
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
        var globeModel = getGlobeModel();
        globeModel.getGameManager().areaOfEffectControl();
        globeModel.getGameManager().specialtiesControl();
        globeModel.getGameManager().writControl();
        globeModel.getGameManager().epsilonsRewardControl();
        globeModel.getGameManager().epsilonIntersectionControl();
        globeModel.getGameManager().enemyIntersectionControl();
    }

    private void setViewBoundsForAll() {
        var globeModel = getGlobeModel();
        ArrayList<EntityModel> entityModels = globeModel.getEntityModels();
        for (int i = 0; i < entityModels.size(); i++) {
            EntityModel entityModel = entityModels.get(i);
            try {
                globeModel.getGlobeController().setViewBounds(entityModel);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        ArrayList<AbilityModel> abilityModels = globeModel.getAbilityModels();
        for (int i = 0; i < abilityModels.size(); i++) {
            AbilityModel abilityModel = abilityModels.get(i);
            try {
                globeModel.getGlobeController().setViewBounds(abilityModel);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        ArrayList<PanelModel> panelModels = globeModel.getPanelModels();
        for (int i = 0; i < panelModels.size(); i++) {
            PanelModel panelModel = panelModels.get(i);
//            System.out.println( panelModel.getId() + " panel is requesting modif");
            try {
                globeModel.getGlobeController().setViewBounds(panelModel);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
