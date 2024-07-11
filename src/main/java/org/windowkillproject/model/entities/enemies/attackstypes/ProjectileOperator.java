package org.windowkillproject.model.entities.enemies.attackstypes;

import org.windowkillproject.application.panels.game.GamePanel;

public interface ProjectileOperator {

    int getX();
    int getY();
    int getRadius();
    GamePanel getLocalPanel();
    void shoot();
}
