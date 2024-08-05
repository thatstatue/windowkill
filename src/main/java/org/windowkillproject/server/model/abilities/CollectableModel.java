package org.windowkillproject.server.model.abilities;

import org.windowkillproject.server.model.globe.GlobeModel;
import org.windowkillproject.server.model.panelmodels.PanelModel;

import java.util.ArrayList;



public class CollectableModel extends AbilityModel{
    public static ArrayList<CollectableModel> collectableModels = new ArrayList<>();
    public CollectableModel(GlobeModel globeModel,PanelModel localPanel, int x, int y, int rewardXp) {
        super(globeModel,localPanel, x, y);
        this.rewardXp = rewardXp;
        collectableModels.add(this);
        globeModel.getGlobeController().createAbilityView( id, x, y);
        initSeconds = globeModel.getElapsedTime().getTotalSeconds();
    }
    private int rewardXp;
    private final long initSeconds;

    public long getInitSeconds() {
        return initSeconds;
    }

    public int getRewardXp() {
        return rewardXp;
    }

}
