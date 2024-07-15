package org.windowkillproject.model.abilities;

import org.windowkillproject.application.panels.game.GamePanel;
import org.windowkillproject.model.Drawable;
import org.windowkillproject.model.ObjectModel;
import org.windowkillproject.model.entities.EpsilonModel;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.UUID;


public abstract class AbilityModel extends ObjectModel {

    public static ArrayList<AbilityModel> abilityModels = new ArrayList<>();



    public AbilityModel(GamePanel localPanel, int x, int y) {
        super(localPanel,x,y);
        abilityModels.add(this);
    }

    public boolean isCollectedByEpsilon() {
        EpsilonModel epsilonModel = EpsilonModel.getINSTANCE();
        return (Math.abs(getX() - epsilonModel.getXO()) <= epsilonModel.getRadius()
                && Math.abs(getY() - epsilonModel.getYO()) <= epsilonModel.getRadius());
    }

    public void destroy() {
        abilityModels.remove(this);
    }


}
