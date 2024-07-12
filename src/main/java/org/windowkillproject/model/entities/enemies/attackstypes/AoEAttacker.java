package org.windowkillproject.model.entities.enemies.attackstypes;

import org.windowkillproject.controller.ElapsedTime;
import org.windowkillproject.model.abilities.MomentModel;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public interface AoEAttacker {
    ArrayList<MomentModel> MOMENT_MODELS = new ArrayList<>();


    default void AOE(){
        MOMENT_MODELS.add(new MomentModel(getAnchor(), ElapsedTime.getTotalSeconds(), getRadius(),getAoEAttackHP()));
    };
    int getAoEAttackHP();
    Point2D getAnchor();
    int getRadius();


}