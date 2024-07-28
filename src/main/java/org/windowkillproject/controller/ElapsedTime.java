package org.windowkillproject.controller;

import javax.swing.*;

import static org.windowkillproject.client.ui.App.getGameFrame;
import static org.windowkillproject.server.model.Wave.setBetweenWaves;

public abstract class ElapsedTime {
    private static int seconds, minutes;
    private static boolean running;
    private static Timer clock;

    public static void setRunning(boolean running) {
        ElapsedTime.running = running;
    }

    public static boolean isRunning() {
        return running;
    }

    public static int getTotalSeconds() {
        return seconds + minutes * 60;
    }


    public static void run() {
        clock = new Timer(1000, e -> {
            pass();
            if (minutes == 0 && seconds == 10) setBetweenWaves(false);
            getGameFrame().setClockTime(timeSetter());
        });
        clock.start();
    }

    public static void pause() {
        if (clock != null) clock.stop();
    }

    public static void resume() {
        if (clock != null) {
            clock.start();
        } else {
            run();
        }
    }

    private static void reset() {
        seconds = 0;
        minutes = 0;
    }
    public static void resetTime() {
        ElapsedTime.reset();
        if (!ElapsedTime.isRunning()) {
            ElapsedTime.setRunning(true);
            ElapsedTime.run();
        } else ElapsedTime.resume();
    }

    private static String timeSetter() {
        return minutes + ":" +
                (seconds >= 10 ? String.valueOf(seconds) : "0" + seconds);
    }

    public static long secondsPassed(long time) {
        return minutes * 60L + seconds - time;
    }

    private static void pass() {
        if (seconds == 59) {
            minutes++;
            seconds = 0;
        } else {
            seconds++;
        }
    }


}
