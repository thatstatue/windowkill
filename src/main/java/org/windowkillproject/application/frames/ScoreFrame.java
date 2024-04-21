package org.windowkillproject.application.frames;

import org.windowkillproject.application.Config;
import org.windowkillproject.application.panels.ScorePanel;
import org.windowkillproject.application.panels.ShopPanel;

import javax.swing.*;
import java.awt.*;

import static org.windowkillproject.application.Config.GAME_HEIGHT;
import static org.windowkillproject.application.Config.GAME_WIDTH;

public class ScoreFrame extends JFrame {

    public ScoreFrame(JLabel[] labels) {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(new Dimension((int) (GAME_WIDTH*0.7), (int) (GAME_HEIGHT*0.7)));
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(false);
        setTitle(Config.SCORE_TITLE);
        this.setLayout(null);
        setContentPane(new ScorePanel(labels));
        this.setVisible(true);
    }

}
