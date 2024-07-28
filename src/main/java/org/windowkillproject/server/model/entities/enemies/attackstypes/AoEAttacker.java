package org.windowkillproject.server.model.entities.enemies.attackstypes;

import org.windowkillproject.controller.ElapsedTime;
import org.windowkillproject.server.model.abilities.MomentModel;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public interface AoEAttacker {
    ArrayList<MomentModel> MOMENT_MODELS = new ArrayList<>();


    default void AOE(){
        MOMENT_MODELS.add(new MomentModel(getMoment(), ElapsedTime.getTotalSeconds(), getRadius(),getAoEAttackHP()));
    };
    int getAoEAttackHP();
    Point2D getMoment();
    int getRadius();


}
