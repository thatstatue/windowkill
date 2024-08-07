package org.windowkillproject.client;

import org.windowkillproject.client.ui.App;
import org.windowkillproject.server.connections.online.PlayerState;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

import static org.windowkillproject.server.connections.online.PlayerState.offline;
import static org.windowkillproject.server.connections.online.PlayerState.online;

public class GameClient implements Runnable {
    public String getUsername() {
        return username;
    }

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    private Socket socket;

    private PrintWriter out;
    private BufferedReader in;
    private final App app;
    private PlayerState playerState;
    private String username;

    public App getApp() {
        return app;
    }

    public static ArrayList<GameClient> clients = new ArrayList<>();

    public PlayerState getPlayerState() {
        return playerState;
    }

    public GameClient() {
        boolean res = true;
        playerState = online;
        while (res) {
            try {
                socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                clients.add(this);
                res = false;

            } catch (IOException e) {
                if (JOptionPane.OK_OPTION != JOptionPane.showConfirmDialog(null,
                        "couldn't connect to server, wanna reconnect?")) {
                    res = false;
                    playerState = offline;
                }
            }
        }
        app = new App(this);
    }
    public void setUsername(String username){
        this.username = username;
    }
    public void sendMessage(String message) {
        out.println(message);
        System.out.println("i sent the message " + message);

    }


    @Override
    public void run() {
        app.run();

        new Thread(new ReceiveMessages()).start();

    }

    private class ReceiveMessages implements Runnable {
        @Override
        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println(message);
                    app.updateGame(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
