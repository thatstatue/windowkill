package org.windowkillproject;

import org.windowkillproject.server.model.globe.GlobeModel;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.windowkillproject.server.model.globe.GlobesManager.getGlobeFromId;

public class MessageQueue {
    private String globeId;

    public String getGlobeId() {
        return globeId;
    }
    public GlobeModel getGlobeModel(){
        return getGlobeFromId(globeId);
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