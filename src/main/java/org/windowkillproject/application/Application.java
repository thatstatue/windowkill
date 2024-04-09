package org.windowkillproject.application;

import org.windowkillproject.application.frames.GameFrame;
import org.windowkillproject.application.frames.PrimaryFrame;
import org.windowkillproject.controller.GameController;
import org.windowkillproject.model.abilities.Shooter;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class Application implements Runnable{
    public static PrimaryFrame primaryFrame;
    public static GameFrame gameFrame;


    @Override
    public void run() {
        initPFrame();
    }
    private void initPFrame(){
        primaryFrame = new PrimaryFrame();
    }
    public static void startGame(){
        primaryFrame.setVisible(false);
        //minimize tabs
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_WINDOWS);
            robot.keyPress( KeyEvent.VK_D);
            robot.keyRelease(KeyEvent.VK_D);
            robot.keyRelease(KeyEvent.VK_WINDOWS);
        } catch (AWTException e) {
            e.printStackTrace();
        }
        initGFrame();
    }
    public static void initGFrame(){
        gameFrame = new GameFrame();
        gameFrame.addMouseListener(getMouseListener()); //todo:debug
        GameController.start();
    }
    public static void showSettings(){

    }

    private static MouseListener mouseListener;
    public static MouseListener getMouseListener() {

        if (shooter == null || shooter.getParent() == null) shooter = new Shooter(10, 30, gameFrame.getGamePanel().getEpsilon());
        if (mouseListener == null) mouseListener = shooter.getMouseListener();
        return mouseListener;
    }
    private static Shooter shooter;


}
