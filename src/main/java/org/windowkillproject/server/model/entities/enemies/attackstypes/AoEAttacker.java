package org.windowkillproject.server.model.entities.enemies.attackstypes;

import org.windowkillproject.server.model.abilities.MomentModel;
import org.windowkillproject.server.model.globe.GlobeModel;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public interface AoEAttacker {
    ArrayList<MomentModel> MOMENT_MODELS = new ArrayList<>();


    default void AOE(){
        MOMENT_MODELS.add(new MomentModel(getGlobeModel(),getMoment(), getInitSecond(), getRadius(),getAoEAttackHP()));
    };
    int getAoEAttackHP();
    Point2D getMoment();
    int getRadius();
    int getInitSecond();
    GlobeModel getGlobeModel();


}
