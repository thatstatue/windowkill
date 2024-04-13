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
            setElapsedTime();
        });
        clock.start();
    }
    public static JLabel elapsedTime = new JLabel();
    private static void setElapsedTime(){
        String time = minutes + ":" +
                (seconds >= 10 ? String.valueOf(seconds) : "0" + seconds);
        elapsedTime = new JLabel(time);
        elapsedTime.setFont(new Font(Font.DIALOG, Font.BOLD, 25));
        elapsedTime.setForeground(Color.white);
        elapsedTime.setBounds(200, 150, 300, 300);
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
