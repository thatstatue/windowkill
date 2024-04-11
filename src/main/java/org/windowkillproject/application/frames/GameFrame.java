package org.windowkillproject.application.frames;

import org.windowkillproject.application.Config;
import org.windowkillproject.model.entities.EpsilonModel;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.windowkillproject.controller.GameController.createWave;

public class GameFrame extends JFrame {
    boolean isStretching = false;
    public GameFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(new Dimension(Config.GAME_WIDTH, Config.GAME_HEIGHT));
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(false);
        setTitle(Config.GAME_TITLE);
        this.setLayout(null);
        setContentPane(new GamePanel());
        this.setVisible(true);
    }
    public GamePanel getGamePanel(){
        return (GamePanel) getContentPane();
    }

    public void shrink() {

        int newX = getX() + Config.FRAME_SHRINKAGE_SPEED / 2;
        int newY = getY() + Config.FRAME_SHRINKAGE_SPEED / 2;
        int newWidth = getWidth() - Config.FRAME_SHRINKAGE_SPEED;
        int newHeight = getHeight() - Config.FRAME_SHRINKAGE_SPEED;
        if (getWidth() <= Config.GAME_MIN_SIZE) {
            newWidth = Config.GAME_MIN_SIZE;
            newX = getX();
        }
        if (getHeight() <= Config.GAME_MIN_SIZE) {
            newHeight = Config.GAME_MIN_SIZE;
            newY = getY();
        }
        if (!isStretching) {
            this.setBounds(newX, newY, newWidth, newHeight);
        }
    }
    public void stretch(int code){
        AtomicInteger count = new AtomicInteger();
        Timer stretchTimer = new Timer(Config.FPS, null);
        stretchTimer.addActionListener(e -> {
            int newX = getX();
            int newY = getY();
            int newWidth = getWidth();
            int newHeight = getHeight();
            switch (code){
                case Config.BULLET_HIT_DOWN -> {
                    newY += Config.FRAME_SHRINKAGE_SPEED / 2;
                    newHeight += Config.FRAME_SHRINKAGE_SPEED * 3/2;
                }
                case Config.BULLET_HIT_LEFT -> {
                    newX -= Config.FRAME_SHRINKAGE_SPEED *3/2 ;
                    newWidth += Config.FRAME_SHRINKAGE_SPEED *  3/2;
                }
                case Config.BULLET_HIT_RIGHT -> {
                    newX += Config.FRAME_SHRINKAGE_SPEED / 2;
                    newWidth += Config.FRAME_SHRINKAGE_SPEED * 3/2;
                }
                case Config.BULLET_HIT_UP -> {
                    newY -= Config.FRAME_SHRINKAGE_SPEED *3/2;
                    newHeight += Config.FRAME_SHRINKAGE_SPEED * 3/2;
                }
            }
            if (count.get() <5) {
                isStretching = true;
                setBounds(newX, newY, newWidth, newHeight);
                count.getAndIncrement();
            }else {
                isStretching = false;
                stretchTimer.stop();
            }
        });
        stretchTimer.start();
    }
    public void shrinkFast(){
        Timer shrinkFastTimer = new Timer(Config.FPS /8, null);
        shrinkFastTimer.addActionListener(e -> {
            int newX = getX() + Config.FRAME_SHRINKAGE_SPEED / 2;
            int newY = getY() + Config.FRAME_SHRINKAGE_SPEED / 2;
            int newWidth = getWidth() - Config.FRAME_SHRINKAGE_SPEED;
            int newHeight = getHeight() - Config.FRAME_SHRINKAGE_SPEED;
            boolean stoppedX = false, stoppedY = false;
            if (getWidth() <= Config.GAME_MIN_SIZE) {
                newWidth = Config.GAME_MIN_SIZE;
                newX = getX();
                stoppedX = true;
            }
            if (getHeight() <= Config.GAME_MIN_SIZE) {
                newHeight = Config.GAME_MIN_SIZE;
                newY = getY();
                stoppedY = true;
            }
            if (!(stoppedX && stoppedY)) {
                setBounds(newX, newY, newWidth, newHeight);
                EpsilonModel epsilonModel =  EpsilonModel.getINSTANCE();
                int deltaX = newWidth/2 - epsilonModel.getXO() - epsilonModel.getRadius();
                int deltaY = newHeight/2 - epsilonModel.getYO() - epsilonModel.getRadius();
                epsilonModel.move(deltaX, deltaY);
            }else {
                createWave(1);
                shrinkFastTimer.stop();
            }
        });
        shrinkFastTimer.start();
    }
}
