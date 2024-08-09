package org.windowkillproject.server.connections;

import org.windowkillproject.MessageQueue;
import org.windowkillproject.server.connections.online.OnlinePlayer;
import org.windowkillproject.server.connections.online.PlayerState;
import org.windowkillproject.server.connections.online.Squad;
import org.windowkillproject.server.model.globe.GlobeModel;
import org.windowkillproject.server.model.globe.GlobesManager;
import org.windowkillproject.server.model.globe.multiplayer.BattleMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.windowkillproject.Request.*;
import static org.windowkillproject.controller.GameManager.random;
import static org.windowkillproject.server.connections.Database.*;

public class League {
    private static boolean squadBattle;
    private static Map<String, MessageQueue> opponentOwnQueue = new HashMap<>();
    public static boolean isSquadBattle() {
        return squadBattle;
    }

    public static void setSquadBattle(boolean squadBattle) {
        League.squadBattle = squadBattle;
    }

    public static void initSquadBattle(){
        if (!squadBattle){
            int size = squads.size();
            if (size>1){
                squadBattle=true;
                boolean[] areIn = new boolean[size];
                for (int i = 0 ; i < squads.size()/2; i++){
                    int t;
                    do {
                        t = random.nextInt(size);
                    }while (areIn[t]);
                    var squad1 = squads.get(t);
                    areIn[t] = true;
                    do {
                        t = random.nextInt(size);
                    }while (areIn[t]);
                    var squad2 = squads.get(t);
                    squad1.setOpponent(squad2);
                    squad2.setOpponent(squad1);
                }
            }

        }
    }
    public static void updateLeague(String[] parts , MessageQueue messageQueue) {
        switch (parts[1]) {
            case REQ_NEW_ONLINE_PLAYER -> {
                if (isNewName(parts[2])) {
                    new OnlinePlayer(parts[2], null, PlayerState.online);
                    messageQueue.enqueue(RES_NEW_ONLINE_PLAYER + REGEX_SPLIT + true);
                } else messageQueue.enqueue(RES_NEW_ONLINE_PLAYER + REGEX_SPLIT + false);
                sendSquadsToUpdate(messageQueue);
            }
            case REQ_NEW_SQUAD ->{
                new Squad(parts[2], parts[3]);
                sendSquadsToUpdate(messageQueue);
                initSquadBattle();
                if (squadBattle) messageQueue.enqueue(BROADCAST_REDIRECT+ REGEX_SPLIT+
                        null + REGEX_SPLIT+ REQ_NOTIFY_START_BATTLE + REGEX_SPLIT+ null);
            }
            case REQ_ADD_TO_VAULT -> {
                Squad squad = getSquadFromName(parts[1]);
                Objects.requireNonNull(squad).addToVault(Integer.parseInt(parts[2]));
            }case REQ_JOIN_SQUAD -> {
                Squad squad = getSquadFromName(parts[1]);
                OnlinePlayer player = getPlayerFromUsername(parts[2]);
                if (player != null) {
                    Objects.requireNonNull(squad).addPlayer(player);
                }
                sendSquadsToUpdate(messageQueue);
            }
            case REQ_LEAVE_SQUAD -> {
                Squad squad = getSquadFromName(parts[1]);
                OnlinePlayer player = getPlayerFromUsername(parts[2]);
                if (player != null) {
                    Objects.requireNonNull(squad).removePlayer(player);
                }
                sendSquadsToUpdate(messageQueue);
            }
            case REQ_SQUADS_LIST -> {
                sendSquadsToUpdate(messageQueue);
            }case REQ_NEW_GAME_MONOMACHIA -> {
                String from = parts[2];
                String to = parts[3];
                opponentOwnQueue.put(to , messageQueue);
                messageQueue.enqueue(BROADCAST_REDIRECT + REGEX_SPLIT+ null+
                       REQ_NEW_GAME_MONOMACHIA+ REGEX_SPLIT+ to + REGEX_SPLIT + from );
            }case RES_NEW_GAME_MONOMACHIA -> {
                if (Boolean.parseBoolean(parts[4])){
                    var id = GlobesManager.newGlobe(messageQueue,
                            opponentOwnQueue.get(parts[2]), BattleMode.monomachia.name());
//        messageQueue.setGlobeId(id);
                    GlobeModel globeModel = GlobesManager.getGlobeFromId(id);
                    globeModel.performAction(REQ_BEGIN_GAME);
                }
            }

        }
    }

    private static void sendSquadsToUpdate(MessageQueue messageQueue) {
        String squadNames = RES_SQUAD_NAMES;
        String occupants = RES_OCCUPANTS;
        for (Squad squad: squads){
            squadNames += REGEX_SPLIT+ squad.getName();
            occupants += REGEX_SPLIT+ squad.getPlayersCount();
        }
        messageQueue.enqueue(squadNames);
        messageQueue.enqueue(occupants);
    }

    private static boolean isNewName(String name){
        for (OnlinePlayer player : allPlayers){
            if (player.getUsername().equals(name))
                return false;
        }
        return true;
    }
}