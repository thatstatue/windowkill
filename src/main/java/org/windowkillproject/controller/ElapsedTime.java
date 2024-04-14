package org.windowkillproject.controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.windowkillproject.application.Application.gameFrame;
import static org.windowkillproject.model.Wave.setBetweenWaves;

public abstract class ElapsedTime {
    private static int seconds, minutes;

    public static int getTotalSeconds() {
        return seconds + minutes*60;
    }


    public static void run(){
        Timer clock = new Timer(1000, e -> {
            pass();
            if (minutes == 0 && seconds == 10) setBetweenWaves(false);
            gameFrame.setClockTime(timeSetter());
        });
        clock.start();
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
