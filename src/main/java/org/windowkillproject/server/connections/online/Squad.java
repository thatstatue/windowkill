package org.windowkillproject.server.connections.online;

import java.util.ArrayList;

import static org.windowkillproject.server.connections.Database.*;


public class Squad {



    private final String name;
    private Squad opponent;

    public Squad getOpponent() {
        return opponent;
    }

    public void setOpponent(Squad opponent) {
        this.opponent = opponent;
    }

    public String getName() {
        return name;
    }

    public OnlinePlayer getHost() {
        return host;
    }

    private ArrayList<OnlinePlayer> players = new ArrayList<>();
    private final OnlinePlayer host;
    private int vault;


    public void setPlayers(ArrayList<OnlinePlayer> players) {
        this.players = players;
    }

    public void setVault(int vault) {
        this.vault = vault;
    }

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
        this(getPlayerFromUsername(hostname), name);
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

    public int getPlayersCount(){
        return players.size();
    }
    private void deleteSquad() {
        for (OnlinePlayer player : players) {
            player.squad = null;
        }
        players.clear();
        squads.remove(this);
    }
}

