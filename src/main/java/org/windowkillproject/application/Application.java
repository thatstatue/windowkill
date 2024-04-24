package org.windowkillproject.application;

import org.windowkillproject.application.frames.*;
import org.windowkillproject.application.listeners.EpsilonKeyListener;
import org.windowkillproject.application.listeners.ShotgunMouseListener;

import org.windowkillproject.controller.ElapsedTime;
import org.windowkillproject.controller.Update;

import java.awt.*;
import java.awt.event.KeyEvent;

import static org.windowkillproject.controller.Update.frameUpdateTimer;
import static org.windowkillproject.controller.Update.modelUpdateTimer;

public class Application implements Runnable {
    private static PrimaryFrame primaryFrame;
    private static GameFrame gameFrame;
    private static ShopFrame shopFrame;
    public static ScoreFrame scoreFrame;
    private static SkillTreeFrame skillTreeFrame;

    public static PrimaryFrame getPrimaryFrame(){
        if (primaryFrame == null) primaryFrame = new PrimaryFrame();
        return primaryFrame;
    }
    public static GameFrame getGameFrame(){
        if (gameFrame == null) gameFrame = new GameFrame();
        return gameFrame;
    }
    public static ShopFrame getShopFrame(){
        if (shopFrame == null) shopFrame = new ShopFrame();
        return shopFrame;
    }
    public static SkillTreeFrame getSkillTreeFrame(){
        if (skillTreeFrame == null) skillTreeFrame = new SkillTreeFrame();
        return skillTreeFrame;
    }



    @Override
    public void run() {
        initPFrame();
    }

    public static void initPFrame() {
        if (scoreFrame!=null) scoreFrame.dispose();
        gameFrame = new GameFrame();
        shopFrame = new ShopFrame();
        getSkillTreeFrame().setVisible(false);
        getPrimaryFrame().setVisible(true);


    }
    public static void initScoreFrame(){
        getGameFrame().setVisible(false);
        scoreFrame= new ScoreFrame(gameFrame.getLabels());
        pauseUpdate();
        gameFrame = new GameFrame();
    }


    public static void startGame() {
        getPrimaryFrame().setVisible(false);
        getShopFrame().setVisible(false);
        //minimize tabs
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
        getGameFrame().setVisible(true);
        new EpsilonKeyListener().startListener();
        new ShotgunMouseListener().startListener();
        getGameFrame().shrinkFast();
        new Update();

    }
    public static void initShFrame() {
        pauseUpdate();
        getGameFrame().setVisible(false);
        getShopFrame().setVisible(true);

    }
    public static void initSTFrame() {
        getPrimaryFrame().setVisible(false);
        getSkillTreeFrame().setVisible(true);

    }
    public static void pauseUpdate(){
        modelUpdateTimer.stop();
        frameUpdateTimer.stop();
        ElapsedTime.pause();
    }
    public static void hideShFrame(){
        getShopFrame().setVisible(false);
        getGameFrame().setVisible(true);
        ElapsedTime.resume();
        modelUpdateTimer.start();
        frameUpdateTimer.start();

    }

    public static void showSettings() {
//todo
    }


}
