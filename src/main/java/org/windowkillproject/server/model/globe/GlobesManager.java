package org.windowkillproject.server.model.globe;

import org.windowkillproject.MessageQueue;

import org.windowkillproject.server.model.globe.multiplayer.BattleMode;
import org.windowkillproject.server.model.globe.multiplayer.ColosseumGlobe;
import org.windowkillproject.server.model.globe.multiplayer.MonomachiaGlobe;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.windowkillproject.server.model.entities.EpsilonModel.newINSTANCE;
import static org.windowkillproject.server.model.entities.EpsilonModel.queueEpsilonModelMap;

public class GlobesManager {
    private static Map<String, GlobeModel> globeModelMap = new HashMap<>();

    public static String newGlobe(MessageQueue queue1, MessageQueue queue2, String battleMode){

        var eps1 = queueEpsilonModelMap.get(queue1);
        if (queue1 != null && eps1 == null){
            eps1 = newINSTANCE(queue1,null);
        }
        var eps2 = queueEpsilonModelMap.get(queue2);
        if (queue2 != null && eps2 == null){
            eps2 = newINSTANCE(queue2,null);
        }
        GlobeModel globeModel;

        if (battleMode.equals(BattleMode.monomachia.name())){
            globeModel = new MonomachiaGlobe(eps1, eps2);
            eps2.setGlobeModel(globeModel);
            globeModel.entityModels.add(eps2);
        }else if (battleMode.equals(BattleMode.colosseum.name())){
            globeModel = new ColosseumGlobe(eps1,eps2);
            eps2.setGlobeModel(globeModel);
            globeModel.entityModels.add(eps2);
        }else globeModel = new SingleGlobe(eps1);

        eps1.setGlobeModel(globeModel);
        globeModelMap.put(globeModel.getId(), globeModel);
        return globeModel.getId();
    }

    public static GlobeModel getGlobeFromId(String id) {
        return globeModelMap.get(id);
    }




    public static void summon(String globeId, MessageQueue summonerQueue, MessageQueue newQueue){
        var summoner = queueEpsilonModelMap.get(summonerQueue);
        var newEpsilon = queueEpsilonModelMap.get(newQueue);
        var globe = globeModelMap.get(globeId);
        if (globe instanceof MonomachiaGlobe) {
            ((MonomachiaGlobe)globe).summon(summoner, newEpsilon);
        }
    }
}
