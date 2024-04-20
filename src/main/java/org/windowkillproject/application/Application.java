package org.windowkillproject.application;

import org.windowkillproject.application.frames.GameFrame;
import org.windowkillproject.application.frames.PrimaryFrame;
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

    public static void initGFrame() {
        gameFrame = new GameFrame();
        new EpsilonKeyListener().startListener();
        new ShotgunMouseListener().startListener();
        gameFrame.shrinkFast();
        new Update();

    }
    public static void initShFrame() {
        modelUpdateTimer.stop();
        frameUpdateTimer.stop();
        ElapsedTime.pause();
        if (gameFrame != null) gameFrame.setVisible(false);
        if (shopFrame == null) shopFrame = new ShopFrame();

    }

    public static void showSettings() {
//todo
    }


}
