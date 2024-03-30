package org.windowkillproject.application;

import org.windowkillproject.application.frames.GameFrame;
import org.windowkillproject.application.frames.GamePanel;
import org.windowkillproject.application.frames.PrimaryFrame;
import org.windowkillproject.controller.GameController;

import java.awt.*;
import java.awt.event.KeyEvent;
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
    public static void initGFrame(){
        gameFrame = new GameFrame();

        ArrayList<GamePanel> gps = new ArrayList<>();
        gps.add((GamePanel) gameFrame.getContentPane());
        GameController.init(gps);

        GameController.start();
    }
    public static void showSettings(){

    }
    public static void startGame(){
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_WINDOWS);
            robot.keyPress( KeyEvent.VK_D);
            robot.keyRelease(KeyEvent.VK_D);
            robot.keyRelease(KeyEvent.VK_WINDOWS);
        } catch (AWTException e) {
            e.printStackTrace();
        }
        primaryFrame.setVisible(false);
        initGFrame();
    }


}
