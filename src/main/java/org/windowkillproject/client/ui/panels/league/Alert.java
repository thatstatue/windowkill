package org.windowkillproject.client.ui.panels.league;

public enum Alert {

    startBattle("NOTICE: the squad battle has begun"),
    winBattle("CONGRATS: you won the squad battle"),
    loseBattle("FAIL: you lost the squad battle"),
    noAlerts("");
    private final String message;

    Alert(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
