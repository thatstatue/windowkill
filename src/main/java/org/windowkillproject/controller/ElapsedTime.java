package org.windowkillproject.controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.windowkillproject.application.Application.gameFrame;

public abstract class ElapsedTime {
    private static int seconds, minutes;

    public static void run(){
        Timer clock = new Timer(1000, e -> {
            pass();
            gameFrame.setClockTime(timeSetter());
        });
        clock.start();
    }
    private static String timeSetter(){
        return minutes + ":" +
                (seconds >= 10 ? String.valueOf(seconds) : "0" + seconds);
    }

    public static int secondsPassed(int minutes, int seconds){
        return (ElapsedTime.minutes - minutes)*60 + (ElapsedTime.seconds - seconds);
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
