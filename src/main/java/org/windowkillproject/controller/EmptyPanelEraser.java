package org.windowkillproject.controller;

import org.windowkillproject.application.panels.game.GamePanel;
import org.windowkillproject.application.panels.game.MainGamePanel;
import org.windowkillproject.model.entities.EntityModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import static org.windowkillproject.application.panels.game.GamePanel.gamePanels;
import static org.windowkillproject.application.panels.game.GamePanel.gamePanelsBounds;
import static org.windowkillproject.controller.Controller.deleteGamePanel;
import static org.windowkillproject.controller.Utils.isTransferableInBounds;
import static org.windowkillproject.model.entities.EntityModel.entityModels;

public class EmptyPanelEraser extends Timer {

    public EmptyPanelEraser() {
        super(5000, null);
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
        };
        addActionListener(actionListener);
    }
}
