package org.windowkillproject.server.model.entities.enemies.attackstypes;

import org.windowkillproject.server.model.globe.GlobeModel;

public interface ProjectileOperator {

    int getX();
    int getY();
    int getRadius();
    GlobeModel getGlobeModel();
    String getGlobeId();
    void shoot();
}
