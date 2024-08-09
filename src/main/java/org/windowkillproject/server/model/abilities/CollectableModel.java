package org.windowkillproject.server.model.abilities;

import org.windowkillproject.server.model.panelmodels.PanelModel;



public class CollectableModel extends AbilityModel{
    public CollectableModel(String globeId,PanelModel localPanel, int x, int y, int rewardXp) {
        super(globeId,localPanel, x, y);
        this.rewardXp = rewardXp;
        getGlobeModel().getCollectableModels().add(this);
        getGlobeModel().getGlobeController().createAbilityView( id, x, y);
        initSeconds = getGlobeModel().getElapsedTime().getTotalSeconds();
    }
    private int rewardXp;
    private final long initSeconds;

    public long getInitSeconds() {
        return initSeconds;
    }

    public int getRewardXp() {
        return rewardXp;
    }
    @Override
    public void destroy(){
        super.destroy();
        getGlobeModel().getCollectableModels().remove(this);
    }

}
