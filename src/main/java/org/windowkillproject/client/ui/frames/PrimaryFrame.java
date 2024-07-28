package org.windowkillproject.client.ui.frames;

import org.windowkillproject.server.Config;
import org.windowkillproject.client.ui.panels.etc.PrimaryPanel;

import javax.swing.*;
import java.awt.*;

public class PrimaryFrame extends JFrame {
    public PrimaryFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(new Dimension(Config.APP_WIDTH, Config.APP_HEIGHT));
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(false);
        setTitle(Config.APP_TITLE);
        this.setLayout(null);
        setContentPane(new PrimaryPanel());
    }
}
