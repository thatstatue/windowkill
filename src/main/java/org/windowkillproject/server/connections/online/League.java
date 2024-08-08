package org.windowkillproject.server.connections.online;

import org.windowkillproject.MessageQueue;
import org.windowkillproject.server.connections.Database;

import java.util.ArrayList;
import java.util.Objects;

import static org.windowkillproject.Request.*;
import static org.windowkillproject.server.connections.Database.*;

public class League {

    public static void updateLeague(String[] parts , MessageQueue messageQueue) {
        switch (parts[1]) {
            case REQ_NEW_ONLINE_PLAYER -> {
                if (isNewName(parts[2])) {
                    new OnlinePlayer(parts[2], null, PlayerState.online);
                    messageQueue.enqueue(RES_NEW_ONLINE_PLAYER + REGEX_SPLIT + true);
                } else messageQueue.enqueue(RES_NEW_ONLINE_PLAYER + REGEX_SPLIT + false);
            }
            case REQ_NEW_SQUAD ->{
                new Squad(parts[2], parts[3]);
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
            }
            case REQ_LEAVE_SQUAD -> {
                Squad squad = getSquadFromName(parts[1]);
                OnlinePlayer player = getPlayerFromUsername(parts[2]);
                if (player != null) {
                    Objects.requireNonNull(squad).removePlayer(player);
                }
            }
        }
    }
    private static boolean isNewName(String name){
        for (OnlinePlayer player : allPlayers){
            if (player.username.equals(name))
                return false;
        }
        return true;
    }
}