package org.windowkillproject.controller;

import org.windowkillproject.client.ui.panels.game.PanelView;
import org.windowkillproject.client.ui.panels.game.MainPanelView;

import org.windowkillproject.server.model.entities.EntityModel;
import org.windowkillproject.server.model.globe.GlobeModel;
import org.windowkillproject.server.model.panelmodels.MainPanelModel;
import org.windowkillproject.server.model.panelmodels.PanelModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static org.windowkillproject.client.ui.panels.game.PanelView.panelViews;
import static org.windowkillproject.controller.Utils.isTransferableInBounds;

import static org.windowkillproject.server.model.entities.EntityModel.entityModels;

public class EmptyPanelEraser extends Timer {
    private GlobeModel globeModel;

    public EmptyPanelEraser(GlobeModel globeModel) {
        super(5000, null);
        this.globeModel = globeModel;
//        App.setGameState(new GameState(objectModels, panelViews, gamePanelsBounds, Wave.getLevel(),
//                getKilledEnemiesInWave(), getKilledEnemiesTotal()));

        ActionListener actionListener = e -> {
            for (int j = 0; j< globeModel.getPanelModels().size(); j++) {
                PanelModel panelModel = globeModel.getPanelModels().get(j);
                if (panelModel instanceof MainPanelModel)
                    continue;
                Rectangle rectangle = panelModel.getBounds();
                boolean hasEntity = false;
                for (int i = 0; i < entityModels.size(); i++) {
                    EntityModel entityModel = entityModels.get(i);
                    if (isTransferableInBounds(entityModel, rectangle, true)) {
                        hasEntity = true;
                        break;
                    }
                }
                if (!hasEntity) {
                    globeModel.getGameManager().deleteGamePanel(panelModel);
                }
            }
         //   GameSaveManager.saveGameState(App.getGameState()); //todo move savings to server-side
        };
        addActionListener(actionListener);
    }
}
