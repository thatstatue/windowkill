package org.windowkillproject.client.ui.frames;

import org.windowkillproject.client.GameClient;
import org.windowkillproject.client.ui.panels.etc.PrimaryPanel;

import javax.swing.*;
import java.awt.*;

import static org.windowkillproject.Constants.*;

public class PrimaryFrame extends JFrame {
    public PrimaryFrame(GameClient client) {
        super();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(new Dimension(APP_WIDTH, APP_HEIGHT));
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(false);
        setTitle(APP_TITLE);
        this.setLayout(null);
        setContentPane(new PrimaryPanel(client));
    }
}
