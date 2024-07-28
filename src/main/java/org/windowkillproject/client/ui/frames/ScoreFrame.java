package org.windowkillproject.client.ui.frames;

import org.windowkillproject.server.Config;
import org.windowkillproject.client.ui.panels.etc.ScorePanel;

import javax.swing.*;
import java.awt.*;

import static org.windowkillproject.server.Config.GAME_HEIGHT;
import static org.windowkillproject.server.Config.GAME_WIDTH;

public class ScoreFrame extends JFrame {

    public ScoreFrame(JLabel[] labels, boolean won) {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(new Dimension((int) (GAME_WIDTH * 0.7), (int) (GAME_HEIGHT * 0.7)));
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(false);
        setTitle(Config.SCORE_TITLE);
        this.setLayout(null);
        setContentPane(new ScorePanel(labels, won));
        this.setVisible(true);
    }

}
