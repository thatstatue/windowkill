package org.windowkillproject.application.frames;

import org.windowkillproject.application.Config;
import org.windowkillproject.application.panels.ShopPanel;

import javax.swing.*;
import java.awt.*;

public class ShopFrame extends JFrame{

    public ShopFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(new Dimension(Config.APP_WIDTH, Config.APP_HEIGHT));
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(false);
        setTitle(Config.SHOP_TITLE);
        this.setLayout(null);
        setContentPane(new ShopPanel());
        this.setVisible(true);
    }

}
