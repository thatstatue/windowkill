package org.windowkillproject.model.abilities;

import org.windowkillproject.view.abilities.CollectableView;

import java.util.ArrayList;

import static org.windowkillproject.controller.Controller.createAbilityView;
import static org.windowkillproject.controller.ElapsedTime.getTotalSeconds;

public class CollectableModel extends AbilityModel{
    public static ArrayList<CollectableModel> collectableModels = new ArrayList<>();
    public CollectableModel(int x, int y, int rewardXp) {
        super(x, y);
        this.rewardXp = rewardXp;
        collectableModels.add(this);
        createAbilityView(CollectableView.class, id, x, y);
        initSeconds = getTotalSeconds();
    }
    private int rewardXp;
    private final long initSeconds;

    public long getInitSeconds() {
        return initSeconds;
    }

    public int getRewardXp() {
        return rewardXp;
    }

    public void setRewardXp(int rewardXp) {
        this.rewardXp = rewardXp;
    }

}
