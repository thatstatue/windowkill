package org.windowkillproject.server.model.entities.enemies.attackstypes;

import org.windowkillproject.server.ClientHandler;
import org.windowkillproject.server.ClientHandlerTeam;
import org.windowkillproject.server.model.PanelModel;

public interface ProjectileOperator {

    int getX();
    int getY();
    int getRadius();
    PanelModel getLocalPanel();
    ClientHandler getClientHandler();
    ClientHandlerTeam getTeam();
    void shoot();
}
