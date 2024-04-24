package org.windowkillproject.controller;

import javax.swing.*;

import static org.windowkillproject.application.Application.getGameFrame;
import static org.windowkillproject.model.Wave.setBetweenWaves;

public abstract class ElapsedTime {
    private static int seconds, minutes;
    private static Timer clock;
    public static int getTotalSeconds() {
        return seconds + minutes*60;
    }


    public static void run(){
        clock = new Timer(1000, e -> {
            pass();
            if (minutes == 0 && seconds == 10) setBetweenWaves(false);
            getGameFrame().setClockTime(timeSetter());
        });
        clock.start();
    }
    public static void pause(){
        if (clock!= null) clock.stop();
    }
    public static void resume(){
        if (clock!= null) clock.start();
    }

    private static String timeSetter(){
        return minutes + ":" +
                (seconds >= 10 ? String.valueOf(seconds) : "0" + seconds);
    }

    public static long secondsPassed( long time){
        return minutes* 60L +seconds - time;
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
