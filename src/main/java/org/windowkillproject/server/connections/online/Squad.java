package org.windowkillproject.server.connections.online;

import java.util.ArrayList;

import static org.windowkillproject.server.connections.online.League.allPlayers;

public class Squad {
    public static ArrayList<Squad> squads;

    public static Squad getSquadFromName(String name) {
        for (Squad squad : squads) {
            if (squad.getName().equals(name)) return squad;
        }
        return null;
    }

    private String name;

    public String getName() {
        return name;
    }

    public OnlinePlayer getHost() {
        return host;
    }

    private ArrayList<OnlinePlayer> players = new ArrayList<>();
    private OnlinePlayer host;

    public Squad(OnlinePlayer host, String name) {
        this.host = host;
        this.name = name;
        players.add(host);
    }
    public Squad(String hostname, String name){
        for (OnlinePlayer player: allPlayers){
            if (player.username.equals(hostname))
                new Squad(player, name);
        }
    }

    public int getPlayersCount() {
        return players.size();
    }

    public void addPlayer(OnlinePlayer player) {
        player.squad = this;
        players.add(player);
    }

    public void removePlayer(OnlinePlayer player) {

        player.squad = null;
        players.remove(player);
        if (player.username.equals(host.username)) {
            deleteSquad(player.username);
        }
    }

    private void deleteSquad(String username) {
        for (OnlinePlayer player : players) {
            player.squad = null;
        }
        players.clear();
        squads.remove(this);
    }
}

