package org.windowkillproject.server.model.abilities;

import org.windowkillproject.server.model.globe.GlobeModel;
import org.windowkillproject.server.model.ObjectModel;
import org.windowkillproject.server.model.panelmodels.PanelModel;
import org.windowkillproject.server.model.entities.EpsilonModel;


public abstract class AbilityModel extends ObjectModel {



    public AbilityModel(String globeId, PanelModel localPanel, int x, int y) {
        super(globeId, localPanel,x,y);
        if (globeId != null) addToGlobe(this);

    }

    private void addToGlobe(AbilityModel abilityModel) {
        getGlobeModel().getAbilityModels().add(abilityModel);
        getGlobeModel().getGlobeController().createAbilityView(id, x, y);
    }

    public boolean isCollectedByEpsilon(EpsilonModel epsilonModel) {
        return Math.abs(getX() - epsilonModel.getXO()) <= epsilonModel.getRadius()
                && Math.abs(getY() - epsilonModel.getYO()) <= epsilonModel.getRadius();
    }

    public void destroy() {
        getGlobeModel().getAbilityModels().remove(this);
    }


}
