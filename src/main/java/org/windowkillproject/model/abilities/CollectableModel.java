package org.windowkillproject.model.abilities;

import org.windowkillproject.application.panels.game.GamePanel;

import java.util.ArrayList;

import static org.windowkillproject.controller.Controller.createAbilityView;
import static org.windowkillproject.controller.ElapsedTime.getTotalSeconds;

public class CollectableModel extends AbilityModel{
    public static ArrayList<CollectableModel> collectableModels = new ArrayList<>();
    public CollectableModel(GamePanel localPanel, int x, int y, int rewardXp) {
        super(localPanel, x, y);
        this.rewardXp = rewardXp;
        collectableModels.add(this);
        createAbilityView( id, x, y);
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

}
