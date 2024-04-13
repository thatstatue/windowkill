package org.windowkillproject.application;

import org.windowkillproject.application.frames.GameFrame;
import org.windowkillproject.application.frames.PrimaryFrame;
import org.windowkillproject.application.listeners.EpsilonKeyListener;
import org.windowkillproject.application.listeners.ShotgunMouseListener;

import org.windowkillproject.controller.Update;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Application implements Runnable {
    public static PrimaryFrame primaryFrame;
    public static GameFrame gameFrame;


    @Override
    public void run() {
        initPFrame();
    }

    private void initPFrame() {
        primaryFrame = new PrimaryFrame();
    }

    public static void startGame() {
        primaryFrame.setVisible(false);
        //minimize tabs //todo : un-comment
//        try {
//            Robot robot = new Robot();
//            robot.keyPress(KeyEvent.VK_WINDOWS);
//            robot.keyPress(KeyEvent.VK_D);
//            robot.keyRelease(KeyEvent.VK_D);
//            robot.keyRelease(KeyEvent.VK_WINDOWS);
//        } catch (AWTException e) {
//            e.printStackTrace();
//        }
        initGFrame();
    }

    public static void initGFrame() {
        gameFrame = new GameFrame();
       // gameFrame.addMouseListener(new ShotgunMouseListener()); //todo: debug
       //
        new EpsilonKeyListener().startListener();
        new ShotgunMouseListener().startListener();
        gameFrame.shrinkFast();
        new Update();

    }

    public static void showSettings() {
//todo
    }


}
