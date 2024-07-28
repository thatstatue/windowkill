package org.windowkillproject.server;

import org.windowkillproject.client.GameClient;
import org.windowkillproject.server.model.Writ;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import static org.windowkillproject.Request.REGEX_SPLIT;

public class ClientHandler extends Thread {
    private final Socket socket;
    private final GameServer server;
    private PrintWriter out;
    private BufferedReader in;
    public static Map<Socket, ClientHandler> socketClientHandlerMap = new HashMap<>();
    private final Writ writ =new Writ(this);

    public Writ getWrit() {
        return writ;
    }

    public ClientHandler(Socket clientSocket, GameServer server) {
        this.socket = clientSocket;
        this.server = server;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }  catch (IOException e){
            e.printStackTrace();
        }
        socketClientHandlerMap.put(socket, this);
    }

    @Override
    public void run() {

        try {
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Received: " + message);
                new RequestHandler(this , message).run();
//                server.broadcast(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            server.removeClient(this);
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        out.println(message + REGEX_SPLIT);
    }

}
