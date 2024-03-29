package org.windowkillproject.application.frames;

import org.windowkillproject.application.Config;

import javax.swing.*;
import java.awt.*;

public class PrimaryFrame extends JFrame {
    public PrimaryFrame(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(new Dimension(Config.APP_WIDTH, Config.APP_HEIGHT));
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(false);
        setTitle(Config.APP_TITLE);
        this.setLayout(null);
        setContentPane(new PrimaryPanel());
        this.setVisible(true);
    }
    private void update() {
        this.revalidate();
        this.repaint();
    }
}
