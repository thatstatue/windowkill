package org.windowkillproject.model.abilities;

import java.util.ArrayList;

import static org.windowkillproject.controller.Controller.createCollectableView;

public class CollectableModel extends AbilityModel{
    public static ArrayList<CollectableModel> collectableModels = new ArrayList<>();
    public CollectableModel(int x, int y) {
        super(x, y);
        collectableModels.add(this);
        createCollectableView(id, x, y);
    }
    private int rewardHp;


    public int getRewardHp() {
        return rewardHp;
    }

    public void setRewardHp(int rewardHp) {
        this.rewardHp = rewardHp;
    }

}
