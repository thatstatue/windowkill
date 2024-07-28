package org.windowkillproject.server;

import java.io.*;
import java.net.*;
import java.util.*;

public class GameServer implements Runnable {
    private static final int PORT = 12345;
    private ServerSocket serverSocket;
    private List<ClientHandler> clientHandlers;

    public GameServer() {
        clientHandlers = new ArrayList<>();

    }

    public synchronized void broadcast(String message) {
        for (ClientHandler client : clientHandlers) {
            client.sendMessage(message);
        }
    }

    public synchronized void removeClient(ClientHandler clientHandler) {
        clientHandlers.remove(clientHandler);
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on port " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                clientHandlers.add(clientHandler);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
