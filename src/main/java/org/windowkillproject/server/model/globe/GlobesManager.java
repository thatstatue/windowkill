package org.windowkillproject.server.model.globe;

import org.windowkillproject.MessageQueue;

import org.windowkillproject.server.model.entities.EpsilonModel;
import org.windowkillproject.server.model.globe.multiplayer.BattleMode;
import org.windowkillproject.server.model.globe.multiplayer.ColosseumGlobe;
import org.windowkillproject.server.model.globe.multiplayer.MonomachiaGlobe;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;


import static org.windowkillproject.Request.REGEX_SPLIT;
import static org.windowkillproject.Request.RES_GLOBE_ID;
import static org.windowkillproject.server.model.entities.EpsilonModel.queueEpsilonModelMap;

public class GlobesManager {
    private static Map<String, GlobeModel> globeModelMap = new HashMap<>();

    public static String newGlobe(MessageQueue queue1, MessageQueue queue2, String battleMode){

        var eps1 = queueEpsilonModelMap.get(queue1);
        if (queue1 != null && eps1 == null){
            eps1 = new EpsilonModel(queue1,null);
        }
        var eps2 = queueEpsilonModelMap.get(queue2);
        if (queue2 != null && eps2 == null){
            eps2 = new EpsilonModel(queue2,null);
        }
        String id = UUID.randomUUID().toString();
        String m = RES_GLOBE_ID+REGEX_SPLIT+id;
        System.out.println("this is the message im enqueuing" + m);
        Objects.requireNonNull(queue1).enqueue(m);
        Objects.requireNonNull(queue1).enqueue(m);

        GlobeModel globeModel;
        if (battleMode.equals(BattleMode.monomachia.name())){
            globeModel = new MonomachiaGlobe(id, eps1, eps2);
        }else if (battleMode.equals(BattleMode.colosseum.name())){
            globeModel = new ColosseumGlobe(id, eps1,eps2);
        }else globeModel = new SingleGlobe(id, eps1);
        globeModelMap.put(id, globeModel);

        eps1.setGlobeModel(globeModel);
        if (eps2!=null) eps2.setGlobeModel(globeModel);

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
