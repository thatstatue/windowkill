package org.windowkillproject.application.frames;

import org.windowkillproject.application.Config;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
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
        this.setBounds(newX, newY, newWidth, newHeight);
    }
    public void stretch(int code){
        int newX = getX();
        int newY = getY();
        int newWidth = getWidth();
        int newHeight = getHeight();
        switch (code){
            case Config.BULLET_HIT_DOWN -> {
                newY += Config.FRAME_SHRINKAGE_SPEED / 2;
                newHeight += Config.FRAME_SHRINKAGE_SPEED * 5;
            }
            case Config.BULLET_HIT_LEFT -> {
                newX -= Config.FRAME_SHRINKAGE_SPEED ;
                newWidth += Config.FRAME_SHRINKAGE_SPEED *  5;
            }
            case Config.BULLET_HIT_RIGHT -> {
                newX += Config.FRAME_SHRINKAGE_SPEED / 2;
                newWidth += Config.FRAME_SHRINKAGE_SPEED * 5;
            }
            case Config.BULLET_HIT_UP -> {
                newY -= Config.FRAME_SHRINKAGE_SPEED ;
                newHeight += Config.FRAME_SHRINKAGE_SPEED * 5;
            }
        }
        this.setBounds(newX, newY, newWidth, newHeight);

    }
}
