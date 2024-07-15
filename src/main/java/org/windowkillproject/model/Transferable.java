package org.windowkillproject.model;

import org.windowkillproject.application.panels.game.GamePanel;

import java.util.ArrayList;

public interface Transferable {
    int getX();
    int getY();
    int getWidth();
    int getHeight();
    void addToAllowedArea(GamePanel panel);
    void setAllowedPanels(ArrayList<GamePanel> allowedPanels);
}
