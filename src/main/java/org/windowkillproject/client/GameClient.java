package org.windowkillproject.client;

import org.windowkillproject.client.ui.App;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class GameClient implements Runnable{
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    private Socket socket;

    private PrintWriter out;
    private BufferedReader in;
    private final App app;

    public App getApp() {
        return app;
    }

    public static ArrayList<GameClient> clients = new ArrayList<>();

    public GameClient() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            clients.add(this);


        } catch (IOException e) {
            e.printStackTrace();
        }
        app = new App(this);
    }

    public void sendMessage(String message) {
        out.println(message);
        System.out.println("i sent the message "+ message);

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
                    System.out.println(message );
                    app.updateGame(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
