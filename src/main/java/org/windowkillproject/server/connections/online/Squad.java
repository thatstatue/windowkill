package org.windowkillproject.server.connections.online;

import java.util.ArrayList;

import static org.windowkillproject.server.connections.Database.allPlayers;
import static org.windowkillproject.server.connections.Database.squads;


public class Squad {



    private String name;

    public String getName() {
        return name;
    }

    public OnlinePlayer getHost() {
        return host;
    }

    private ArrayList<OnlinePlayer> players = new ArrayList<>();
    private OnlinePlayer host;
    private int vault;

    public int getVault() {
        return vault;
    }

    public void addToVault(int add) {
        vault += add;
    }

    public Squad(OnlinePlayer host, String name) {
        this.host = host;
        this.name = name;
        players.add(host);
    }
    public Squad(String hostname, String name){
        for (OnlinePlayer player: allPlayers){
            if (player.username.equals(hostname)) {
                new Squad(player, name);
                break;
            }
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
            deleteSquad();
        }
    }

    private void deleteSquad() {
        for (OnlinePlayer player : players) {
            player.squad = null;
        }
        players.clear();
        squads.remove(this);
    }
}

