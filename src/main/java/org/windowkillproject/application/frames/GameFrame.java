package org.windowkillproject.application.frames;

import org.windowkillproject.application.Config;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame{
    public GameFrame(){
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
    private void update() {
        this.revalidate();
        this.repaint();
    }
}
