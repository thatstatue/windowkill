package org.windowkillproject.server.model;

import org.windowkillproject.client.ui.panels.game.GamePanel;

import java.util.ArrayList;

public interface Transferable {
    int getX();
    int getY();
    int getWidth();
    int getHeight();
    void addToAllowedArea(GamePanel panel);
    void setLocalPanel(GamePanel gamePanel);
    GamePanel getLocalPanel();
    void setAllowedPanels(ArrayList<GamePanel> allowedPanels);
}
