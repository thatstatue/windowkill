package org.windowkillproject.server.connections.online;

import org.windowkillproject.server.connections.Database;

import static org.windowkillproject.server.connections.Database.getSquadFromName;

public class OnlinePlayer {
    String username;
    Squad squad;
    public OnlinePlayer(String username){
        new OnlinePlayer(username, null, PlayerState.online);
    }
    int xp;
    PlayerState playerState;
    private String globeId;
    public OnlinePlayer(String username, Squad squad, PlayerState playerState) {
        this.username = username;
        this.squad = squad;
        this.playerState = playerState;
        Database.addPlayer(this);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Squad getSquad() {
        return squad;
    }

    public void setSquad(Squad squad) {
        this.squad = squad;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public String getGlobeId() {
        return globeId;
    }

    public void setGlobeId(String globeId) {
        this.globeId = globeId;
    }

    public void setPlayerState(PlayerState playerState) {
        this.playerState = playerState;
    }


    public OnlinePlayer(String username, String name, String playerStateName) {
        this.username = username;
        if (name!= null) squad = getSquadFromName(name);
        if (playerStateName.equals(PlayerState.online.name())){
            this.playerState = PlayerState.online;
        }else if(playerStateName.equals(PlayerState.busy.name())){
            this.playerState = PlayerState.busy;
        }else playerState = PlayerState.online;
    }

}
