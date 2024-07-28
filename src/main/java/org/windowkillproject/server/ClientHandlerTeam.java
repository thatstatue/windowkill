package org.windowkillproject.server;

import org.windowkillproject.server.ClientHandler;

public record ClientHandlerTeam(ClientHandler first, ClientHandler second) {
    public void sendMessage(String message){
        first.sendMessage(message);
        second.sendMessage(message);
    }
}
