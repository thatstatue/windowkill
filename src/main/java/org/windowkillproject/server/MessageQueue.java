package org.windowkillproject.server;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MessageQueue {
    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();

    public void enqueue(String message) {
        queue.add(message);
    }

    public String dequeue() throws InterruptedException {
        return queue.take();
    }
}