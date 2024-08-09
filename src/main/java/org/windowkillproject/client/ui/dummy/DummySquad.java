package org.windowkillproject.client.ui.dummy;

import java.util.ArrayList;

public class DummySquad {
    private int playersCount;
    private String name;

    public int getPlayersCount() {
        return playersCount;
    }

    public DummySquad(int playersCount, String name, ArrayList<DummyOnlinePlayer> players, DummySquad opponent) {
        this.playersCount = playersCount;
        this.name = name;
        this.players = players;
        this.opponent = opponent;
    }

    public void setPlayersCount(int playersCount) {
        this.playersCount = playersCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<DummyOnlinePlayer> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<DummyOnlinePlayer> players) {
        this.players = players;
    }

    public DummySquad getOpponent() {
        return opponent;
    }

    public void setOpponent(DummySquad opponent) {
        this.opponent = opponent;
    }

    private ArrayList<DummyOnlinePlayer> players;
    private DummySquad opponent;
}
