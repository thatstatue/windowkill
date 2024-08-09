package org.windowkillproject.client.ui.dummy;

public class DummyOnlinePlayer {
    public String getName() {
        return name;
    }
    public String getDetails(){
        return "  "+ name + " : "+ playerState+ " | xp = " + xp;
    }
    public void setName(String name) {
        this.name = name;
    }

    public DummyOnlinePlayer(String name, String playerState, int xp) {
        this.name = name;
        this.playerState = playerState;
        this.xp=xp;
    }

    public String getPlayerState() {
        return playerState;
    }

    public void setPlayerState(String playerState) {
        this.playerState = playerState;
    }

    private String name;

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    private String playerState;
    private int xp;
}
