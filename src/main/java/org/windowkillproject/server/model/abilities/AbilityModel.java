package org.windowkillproject.server.model.abilities;

import org.windowkillproject.server.model.globe.GlobeModel;
import org.windowkillproject.server.model.ObjectModel;
import org.windowkillproject.server.model.panelmodels.PanelModel;
import org.windowkillproject.server.model.entities.EpsilonModel;


public abstract class AbilityModel extends ObjectModel {



    public AbilityModel(GlobeModel globeModel, PanelModel localPanel, int x, int y) {
        super(globeModel, localPanel,x,y);
        globeModel.abilityModels.add(this);
    }

    public boolean isCollectedByEpsilon(EpsilonModel epsilonModel) {
        return Math.abs(getX() - epsilonModel.getXO()) <= epsilonModel.getRadius()
                && Math.abs(getY() - epsilonModel.getYO()) <= epsilonModel.getRadius();
    }

    public void destroy() {
        globeModel.abilityModels.remove(this);
    }


}
