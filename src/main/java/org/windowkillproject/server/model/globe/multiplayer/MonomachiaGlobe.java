package org.windowkillproject.server.model.globe.multiplayer;

import org.windowkillproject.server.model.entities.EpsilonModel;

public class MonomachiaGlobe extends MultiGlobe {
    public MonomachiaGlobe(String id, EpsilonModel epsilon1, EpsilonModel epsilon2) {
        super(id, epsilon1, epsilon2);
    }
    public void summon(EpsilonModel summoner, EpsilonModel newEpsilon){
        if (getTeam1().contains(summoner)){
            getTeam1().add(newEpsilon);
        }else {
            team2.add(newEpsilon);
        }
    }
}
