package org.windowkillproject.server;

import org.windowkillproject.MessageQueue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import static org.windowkillproject.Request.REGEX_SPLIT;

public class ClientHandler{
    private final Socket socket;
    private final GameServer server;
    private PrintWriter out;
    private BufferedReader in;
    private MessageQueue messageQueue = new MessageQueue();

    public static Map<Socket, ClientHandler> socketClientHandlerMap = new HashMap<>();


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
    public void start(){
        new Thread(new sendQueuedMessages(this)).start();
        new Thread(new receiveClientMessages()).start();
    }
    private class receiveClientMessages implements Runnable{
        @Override
        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null){
                    new RequestHandler(messageQueue, message).run();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private class sendQueuedMessages implements Runnable{
        private final ClientHandler clientHandler;
        sendQueuedMessages(ClientHandler clientHandler){
            this.clientHandler = clientHandler;
        }
        @Override
        public void run() {
            try {
                while (true) {
                    String message = messageQueue.dequeue();
                    sendMessage(message);//sends every request from server side queued
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {// todo should i remove client?
                server.removeClient(clientHandler);
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendMessage(String message) {
        out.println(message + REGEX_SPLIT);
    }

}
