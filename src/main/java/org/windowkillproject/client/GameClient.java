package org.windowkillproject.client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.windowkillproject.client.ui.panels.league.Alert;
import org.windowkillproject.client.ui.App;
import org.windowkillproject.server.connections.online.PlayerState;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.windowkillproject.server.connections.online.PlayerState.offline;
import static org.windowkillproject.server.connections.online.PlayerState.online;

public class GameClient implements Runnable {
    public String getUsername() {
        return username;
    }
    public static Map<String,GameClient> usernameMap = new HashMap<>();

    private Alert alert = Alert.noAlerts;
    private static final String SERVER_ADDRESS = "localhost";

    public Alert getAlert() {
        return alert;
    }

    public void setAlert(Alert alert) {
        this.alert = alert;
    }

    private static final int SERVER_PORT = 12345;
    private Socket socket;

    private PrintWriter out;

    public void setPlayerState(PlayerState playerState) {
        this.playerState = playerState;
    }

    private BufferedReader in;
    @JsonIgnore
    private final transient App app;
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
        usernameMap.put(username, this);

    }
    public void sendMessage(String message) {
        out.println(message);
//        System.out.println("i sent the message " + message);

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
                    app.updateGame(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
