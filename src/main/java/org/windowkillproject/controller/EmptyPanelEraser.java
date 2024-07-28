package org.windowkillproject.controller;

import org.windowkillproject.client.ui.App;
import org.windowkillproject.client.ui.panels.game.GamePanel;
import org.windowkillproject.client.ui.panels.game.MainGamePanel;
import org.windowkillproject.controller.data.GameSaveManager;
import org.windowkillproject.controller.data.GameState;
import org.windowkillproject.server.model.Wave;
import org.windowkillproject.server.model.entities.EntityModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static org.windowkillproject.client.ui.panels.game.GamePanel.gamePanels;
import static org.windowkillproject.client.ui.panels.game.GamePanel.gamePanelsBounds;
import static org.windowkillproject.controller.Controller.deleteGamePanel;
import static org.windowkillproject.controller.Utils.isTransferableInBounds;
import static org.windowkillproject.server.model.ObjectModel.objectModels;
import static org.windowkillproject.server.model.entities.EntityModel.entityModels;
import static org.windowkillproject.server.model.entities.enemies.EnemyModel.getKilledEnemiesInWave;
import static org.windowkillproject.server.model.entities.enemies.EnemyModel.getKilledEnemiesTotal;

public class EmptyPanelEraser extends Timer {

    public EmptyPanelEraser() {
        super(5000, null);
        App.setGameState(new GameState(objectModels, gamePanels, gamePanelsBounds, Wave.getLevel(),
                getKilledEnemiesInWave(), getKilledEnemiesTotal()));

        ActionListener actionListener = e -> {
            for (int j = 0 ; j< gamePanels.size(); j++) {
                GamePanel gamePanel = gamePanels.get(j);
                if (gamePanel instanceof MainGamePanel)
                    continue;
                Rectangle rectangle = gamePanelsBounds.get(gamePanel);
                boolean hasEntity = false;
                for (int i = 0; i < entityModels.size(); i++) {
                    EntityModel entityModel = entityModels.get(i);
                    if (isTransferableInBounds(entityModel, rectangle, true)) {
                        hasEntity = true;
                        break;
                    }
                }
                if (!hasEntity) {
                    deleteGamePanel(gamePanel);
                }
            }
            GameSaveManager.saveGameState(App.getGameState());
        };
        addActionListener(actionListener);
    }
}
