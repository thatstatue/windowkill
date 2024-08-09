package org.windowkillproject.server.model.globe;

import org.windowkillproject.MessageQueue;

import org.windowkillproject.server.model.entities.EpsilonModel;
import org.windowkillproject.server.model.entities.enemies.minibosses.BlackOrbModel;
import org.windowkillproject.server.model.globe.multiplayer.BattleMode;
import org.windowkillproject.server.model.globe.multiplayer.ColosseumGlobe;
import org.windowkillproject.server.model.globe.multiplayer.MonomachiaGlobe;
import org.windowkillproject.server.model.panelmodels.MainPanelModel;

import java.util.*;


import static org.windowkillproject.Request.REGEX_SPLIT;
import static org.windowkillproject.Request.RES_GLOBE_ID;
import static org.windowkillproject.server.model.entities.EpsilonModel.queueEpsilonModelMap;

public class GlobesManager {
    public static Map<String, GlobeModel> globeModelMap = new HashMap<>();

    public static String newGlobe(MessageQueue queue1, MessageQueue queue2, String battleMode){

        String id = UUID.randomUUID().toString();

        var eps1 = queueEpsilonModelMap.get(queue1);
        if (queue1 != null && eps1 == null){
            eps1 = new EpsilonModel(queue1,id);
        }
        var eps2 = queueEpsilonModelMap.get(queue2);
        if (queue2 != null && eps2 == null){
            eps2 = new EpsilonModel(queue2,id);
        }
        String m = RES_GLOBE_ID+REGEX_SPLIT+id;
        System.out.println("this is the message im enqueuing" + m);

        if (queue1 != null) {
            queue1.setGlobeId(id);
            queue1.enqueue(m);
        }
        if (queue2 != null) {
            queue2.setGlobeId(id);
            queue2.enqueue(m);
        }

        GlobeModel globeModel;
        if (battleMode.equals(BattleMode.monomachia.name())){
            globeModel = new MonomachiaGlobe(id, eps1, eps2);
        }else if (battleMode.equals(BattleMode.colosseum.name())){
            globeModel = new ColosseumGlobe(id, eps1,eps2);
        }else globeModel = new SingleGlobe(id, eps1);
        globeModelMap.put(id, globeModel);

        globeModel.mainPanelModel = new MainPanelModel(id, id);
//        globeModel.getPanelModels().add(globeModel.mainPanelModel);

        eps1.manualAddToEntities();
        if (eps2!=null) eps2.manualAddToEntities();
        handleNextLevel(globeModel);
        globeModel.shrinkFast();

        return globeModel.getId();
    }
    private static void handleNextLevel(GlobeModel globe){
        globe.setCollectableModels(new ArrayList<>());
        globe.setAbilityModels(new ArrayList<>());
        globe.setBulletModels(new ArrayList<>());
        globe.setProjectileModels(new ArrayList<>());
        //globe.setEntityModels(new ArrayList<>());
        globe.setBlackOrbModels(new ArrayList<>());
        BlackOrbModel.setComplete(false);
        globe.setBarricadosModels(new ArrayList<>());
        globe.setArchmireModels(new ArrayList<>());
        globe.setOmenoctModels(new ArrayList<>());
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
