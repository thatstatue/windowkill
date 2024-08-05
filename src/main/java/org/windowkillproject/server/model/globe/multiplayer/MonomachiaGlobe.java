package org.windowkillproject.server.model.globe.multiplayer;

import org.windowkillproject.server.model.entities.EpsilonModel;

public class MonomachiaGlobe extends MultiGlobe {
    public MonomachiaGlobe(EpsilonModel epsilon1, EpsilonModel epsilon2) {
        super(epsilon1, epsilon2);

    }
    public void summon(EpsilonModel summoner, EpsilonModel newEpsilon){
        if (team1.contains(summoner)){
            team1.add(newEpsilon);
        }else {
            team2.add(newEpsilon);
        }
    }
}
