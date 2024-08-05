package org.windowkillproject.client.ui.frames;

import org.windowkillproject.client.GameClient;
import org.windowkillproject.client.ui.panels.etc.ScorePanel;

import javax.swing.*;
import java.awt.*;

import static org.windowkillproject.Constants.*;

public class ScoreFrame extends JFrame {

    public ScoreFrame(GameClient client, JLabel[] labels, boolean won) {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(new Dimension((int) (GAME_WIDTH * 0.7), (int) (GAME_HEIGHT * 0.7)));
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(false);
        setTitle(SCORE_TITLE);
        this.setLayout(null);
        setContentPane(new ScorePanel(client, labels, won));
        this.setVisible(true);
    }

}
