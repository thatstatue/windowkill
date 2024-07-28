package org.windowkillproject.server.model.abilities;

import org.windowkillproject.client.ui.panels.game.GamePanel;
import org.windowkillproject.server.ClientHandlerTeam;
import org.windowkillproject.server.model.ObjectModel;
import org.windowkillproject.server.model.PanelModel;
import org.windowkillproject.server.model.entities.EpsilonModel;

import java.util.ArrayList;

import static org.windowkillproject.server.model.entities.EpsilonModel.clientEpsilonModelMap;


public abstract class AbilityModel extends ObjectModel {

    public static ArrayList<AbilityModel> abilityModels = new ArrayList<>();



    public AbilityModel(ClientHandlerTeam team, PanelModel localPanel, int x, int y) {
        super(team, localPanel,x,y);
        abilityModels.add(this);
    }

    public EpsilonModel isCollectedByEpsilon() {
        var epsilon1 = clientEpsilonModelMap.get(getTeam().first());
        var epsilon2 = clientEpsilonModelMap.get(getTeam().second());
        if (isCollected(epsilon1)) return epsilon1;
        if (isCollected(epsilon2)) return epsilon2;
        return null;

    }
    private boolean isCollected(EpsilonModel epsilonModel){
        if (Math.abs(getX() - epsilonModel.getXO()) <= epsilonModel.getRadius()
                && Math.abs(getY() - epsilonModel.getYO()) <= epsilonModel.getRadius()){
            return true;
        }
        return false;
    }

    public void destroy() {
        abilityModels.remove(this);
    }


}
