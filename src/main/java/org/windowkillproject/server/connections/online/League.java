package org.windowkillproject.server.connections.online;

import org.windowkillproject.MessageQueue;

import java.util.ArrayList;

import static org.windowkillproject.Request.*;

public class League {
    public static ArrayList<OnlinePlayer> allPlayers;

    public static void updateLeague(String[] parts , MessageQueue messageQueue) {
        switch (parts[1]) {
            case REQ_NEW_ONLINE_PLAYER -> {
                if (isNewName(parts[2])) {
                    allPlayers.add(new OnlinePlayer(parts[2]));
                    messageQueue.enqueue(RES_NEW_ONLINE_PLAYER + REGEX_SPLIT + true);
                } else messageQueue.enqueue(RES_NEW_ONLINE_PLAYER + REGEX_SPLIT + false);
            }
            case REQ_NEW_SQUAD ->{
                new Squad(parts[2], parts[3]);
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