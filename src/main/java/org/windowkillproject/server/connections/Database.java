package org.windowkillproject.server.connections;

import org.windowkillproject.server.connections.online.OnlinePlayer;
import org.windowkillproject.server.connections.online.Squad;

import java.util.ArrayList;

public class Database {
    //private static ArrayList<String> allClientIds = new ArrayList<>();
    public static ArrayList<OnlinePlayer> allPlayers = new ArrayList<>();
    public static ArrayList<Squad> squads = new ArrayList<>();

    public static void addPlayer(OnlinePlayer player){
        allPlayers.add(player);
    }
    public static ArrayList<OnlinePlayer> getPlayersFromGlobeId(String globeId){
        ArrayList<OnlinePlayer> ans = new ArrayList<>();
        for (var player : allPlayers){
            if(player.getGlobeId().equals(globeId))
                ans.add(player);
        }
        return ans;
    }
    public static Squad getSquadFromName(String name) {
        for (Squad squad : squads) {
            if (squad.getName().equals(name)) return squad;
        }
        return null;
    }
    public static OnlinePlayer getPlayerFromUsername(String username){
        for (var player: allPlayers){
            if(player.getUsername().equals(username)) return player;
        }
        return null;
    }
}
