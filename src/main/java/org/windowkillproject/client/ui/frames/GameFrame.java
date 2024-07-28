package org.windowkillproject.client.ui.frames;

import org.windowkillproject.client.GameClient;
import org.windowkillproject.client.ui.panels.game.MainGamePanel;
import org.windowkillproject.client.ui.panels.game.EntityPanel;
import org.windowkillproject.client.ui.panels.game.GamePanel;


import javax.swing.*;
import java.awt.*;

import static org.windowkillproject.Constants.CENTER_X;
import static org.windowkillproject.Constants.CENTER_Y;
import static org.windowkillproject.Constants.GAME_TITLE;
import static org.windowkillproject.client.ui.panels.game.GamePanel.gamePanels;
import static org.windowkillproject.client.ui.panels.game.GamePanel.gamePanelsBounds;


public class GameFrame extends JFrame {

    private JLayeredPane layeredPane = new JLayeredPane();
    private MainGamePanel mainGamePanel;
    private EntityPanel entityPanel;

    @Override
    public JLayeredPane getLayeredPane() {
        return layeredPane;
    }

    public MainGamePanel getMainGamePanel() {
        return mainGamePanel;
    }

    public GameFrame(GameClient client) {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setUndecorated(true);
        setAlwaysOnTop(true);
        setTitle(GAME_TITLE);
        this.setLayout(null);
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setBackground(new Color(0, 0, 0, 0));
        setLocationRelativeTo(null);
//        add(layeredPane, BorderLayout.CENTER);
        mainGamePanel = mainGamePanel.newInstance(client);
        entityPanel = new EntityPanel(client);


        layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, CENTER_X * 2, CENTER_Y * 2);
        layeredPane.setLayout(null);
        layeredPane.add(mainGamePanel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(entityPanel, JLayeredPane.PALETTE_LAYER);
        setContentPane(layeredPane);
    }

    public void setWaveLevel(int level) {
        mainGamePanel.setWaveLevel(level);
    }

    public void initLabels() {
        mainGamePanel.initLabels();
    }

    public JLabel[] getLabels() {
        return mainGamePanel.getLabels();
    }

    public void setHpAmount(int hpAmount) {
        mainGamePanel.setHpAmount(hpAmount);
    }

    public boolean isExploding() {
        return mainGamePanel.isExploding();
    }

    public void setClockTime(String time) {
        mainGamePanel.setClockTime(time);
    }

    public void shrinkFast() {
        mainGamePanel.shrinkFast();
    }

    public void shrink() {
        for (GamePanel gamePanel : gamePanels) {
                gamePanel.shrink();
        }
    }

    public void setXpAmount(int xpAmount) {
        mainGamePanel.setXpAmount(xpAmount);
    }

    public void stretch(GamePanel gamePanel, int code) {
            gamePanel.stretch(code);

    }

    public int getMainPanelHeight() {
        return gamePanelsBounds.get(mainGamePanel).height;
    }

    public int getMainPanelWidth() {
        return gamePanelsBounds.get(mainGamePanel).width;
    }

    public void endingScene() {
        mainGamePanel.endingScene();
    }
}
