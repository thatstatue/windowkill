package org.windowkillproject.server.model.globe.multiplayer;

import org.windowkillproject.server.model.entities.EpsilonModel;
import org.windowkillproject.server.model.globe.GlobeModel;

import java.util.ArrayList;

public abstract class MultiGlobe extends GlobeModel {
    protected ArrayList<EpsilonModel> team2 = new ArrayList<>();

    public MultiGlobe(String id, EpsilonModel epsilon1, EpsilonModel epsilon2) {
        super(id, epsilon1);
        team2.add(epsilon2);
    }

    @Override
    public void performAction(String message) {
        super.performAction(message);
        for (EpsilonModel epsilonModel : team2) {
            epsilonModel.getMessageQueue().enqueue(message);
        }
    }

    @Override
    public ArrayList<EpsilonModel> getEpsilons() {
        var all = super.getEpsilons();
        all.addAll(team2);
        return all;
    }
}
