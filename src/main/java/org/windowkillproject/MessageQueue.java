package org.windowkillproject;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MessageQueue {
    private String globeId;

    public String getGlobeId() {
        return globeId;
    }

    public void setGlobeId(String globeId) {
        this.globeId = globeId;
    }

    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();

    public void enqueue(String message) {
        queue.add(message);
    }

    public String dequeue() throws InterruptedException {
        return queue.take();
    }
}