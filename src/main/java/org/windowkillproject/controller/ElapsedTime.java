package org.windowkillproject.controller;

import org.windowkillproject.server.model.globe.GlobeModel;

import javax.swing.*;

import static org.windowkillproject.Request.REGEX_SPLIT;
import static org.windowkillproject.Request.REQ_SET_CLOCK;
import static org.windowkillproject.server.model.globe.GlobesManager.getGlobeFromId;

public class ElapsedTime {
    private int seconds, minutes;
    private boolean running;
    private Timer clock;
    private final String globeId;

    public ElapsedTime(String globeId) {
        this.globeId=globeId;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isRunning() {
        return running;
    }

    public int getTotalSeconds() {
        return seconds + minutes * 60;
    }
    public GlobeModel getGlobeModel(){
        return getGlobeFromId(globeId);
    }

    public void run() {
        clock = new Timer(1000, e -> {
            pass();
            if (minutes == 0 && seconds == 10) getGlobeModel().getWaveFactory().setBetweenWaves(false);
            getGlobeModel().performAction(REQ_SET_CLOCK + REGEX_SPLIT +timeSetter() );

        });
        clock.start();
    }

    public void pause() {
        if (clock != null) clock.stop();
    }

    public void resume() {
        if (clock != null) {
            clock.start();
        } else {
            run();
        }
    }

    private void reset() {
        seconds = 0;
        minutes = 0;
    }
    public void resetTime() {
        reset();
        if (!isRunning()) {
            setRunning(true);
            run();
        } else resume();
    }

    private String timeSetter() {
        return minutes + ":" +
                (seconds >= 10 ? String.valueOf(seconds) : "0" + seconds);
    }

    public long secondsPassed(long time) {
        return minutes * 60L + seconds - time;
    }

    private void pass() {
        if (seconds == 59) {
            minutes++;
            seconds = 0;
        } else {
            seconds++;
        }
    }


}
