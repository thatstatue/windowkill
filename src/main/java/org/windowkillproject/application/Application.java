package org.windowkillproject.application;

import org.windowkillproject.application.frames.GameFrame;
import org.windowkillproject.application.frames.PrimaryFrame;
import org.windowkillproject.application.frames.ScoreFrame;
import org.windowkillproject.application.frames.ShopFrame;
import org.windowkillproject.application.listeners.EpsilonKeyListener;
import org.windowkillproject.application.listeners.ShotgunMouseListener;

import org.windowkillproject.controller.ElapsedTime;
import org.windowkillproject.controller.Update;

import java.awt.*;
import java.awt.event.KeyEvent;

import static org.windowkillproject.controller.Update.frameUpdateTimer;
import static org.windowkillproject.controller.Update.modelUpdateTimer;

public class Application implements Runnable {
    public static PrimaryFrame primaryFrame;
    public static GameFrame gameFrame;
    public static ShopFrame shopFrame;
    public static ScoreFrame scoreFrame;




    @Override
    public void run() {
        initPFrame();
    }

    public static void initPFrame() {
        if (scoreFrame!=null) scoreFrame.dispose();
        if (gameFrame!=null) gameFrame.dispose();
        if (shopFrame!=null) shopFrame.dispose();

        primaryFrame = new PrimaryFrame();
    }
    public static void initScoreFrame(){
        if (gameFrame!=null) {
            gameFrame.setVisible(false);
            scoreFrame= new ScoreFrame(gameFrame.getLabels());
            pauseUpdate();
            gameFrame.dispose();
        }
    }


    public static void startGame() {
        primaryFrame.setVisible(false);
        shopFrame = new ShopFrame();
        shopFrame.setVisible(false);
        //minimize tabs //todo : un-comment
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_WINDOWS);
            robot.keyPress(KeyEvent.VK_D);
            robot.keyRelease(KeyEvent.VK_D);
            robot.keyRelease(KeyEvent.VK_WINDOWS);
        } catch (AWTException e) {
            e.printStackTrace();
        }
        initGFrame();
    }

    private static void initGFrame() {
        gameFrame = new GameFrame();
        new EpsilonKeyListener().startListener();
        new ShotgunMouseListener().startListener();
        gameFrame.shrinkFast();
        new Update();

    }
    public static void initShFrame() {
        pauseUpdate();
        if (gameFrame != null) gameFrame.setVisible(false);
        if (shopFrame == null) shopFrame = new ShopFrame();
        else shopFrame.setVisible(true);

    }
    public static void pauseUpdate(){
        modelUpdateTimer.stop();
        frameUpdateTimer.stop();
        ElapsedTime.pause();
    }
    public static void hideShFrame(){
        if (shopFrame != null) shopFrame.setVisible(false);
        if (gameFrame != null) gameFrame.setVisible(true);
        ElapsedTime.resume();
        modelUpdateTimer.start();
        frameUpdateTimer.start();

    }

    public static void showSettings() {
//todo
    }


}
