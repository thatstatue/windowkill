package org.windowkillproject.application.frames;

import org.windowkillproject.application.Config;
import org.windowkillproject.application.panels.game.EntityPanel;
import org.windowkillproject.application.panels.game.GamePanel;
import org.windowkillproject.application.panels.game.PanelStatus;
import org.windowkillproject.application.panels.game.MainGamePanel;
import org.windowkillproject.view.abilities.AbilityView;
import org.windowkillproject.view.entities.EntityView;
import org.windowkillproject.view.entities.EpsilonView;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

import static org.windowkillproject.application.Config.CENTER_X;
import static org.windowkillproject.application.Config.CENTER_Y;
import static org.windowkillproject.application.panels.game.GamePanel.gamePanels;
import static org.windowkillproject.view.abilities.AbilityView.abilityViews;
import static org.windowkillproject.view.entities.EntityView.entityViews;

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

    public void setMainGamePanel(MainGamePanel mainGamePanel) {
        this.mainGamePanel = mainGamePanel;
    }

    public GameFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setUndecorated(true);
        setAlwaysOnTop(true);
        setTitle(Config.GAME_TITLE);
        this.setLayout(null);
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setBackground(new Color(0, 0, 0, 0));
        setLocationRelativeTo(null);
//        add(layeredPane, BorderLayout.CENTER);
        mainGamePanel = MainGamePanel.getInstance();
        entityPanel = new EntityPanel();


        layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, CENTER_X * 2, CENTER_Y * 2);
        layeredPane.setLayout(null);
        layeredPane.add(mainGamePanel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(entityPanel, JLayeredPane.PALETTE_LAYER);
        setContentPane(layeredPane);
//        add(mainGamePanel);
//        System.out.println(mainGamePanel.getX()+ " "+mainGamePanel.getY() + ">>>>>>");
//        layeredPane.add(mainGamePanel, 0, 0);
    }


//    @Override
//    public void paintComponents(Graphics g) {
//        super.paintComponents(g);
//
////        for (GamePanel panel : gamePanels) {
////            panel.revalidate();
////            panel.repaint();
////        }
//
//
////        for (EntityView entityView : entityViews){
////            entityView.repaint();
////        }
////        for (AbilityView abilityView : abilityViews){
////            abilityView.repaint();
////        }
////        repaint();
//
////        Objects.requireNonNull(EpsilonView.getInstance()).repaint();
//    }

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

    public int getMainPanelX() {
        return mainGamePanel.getX();
    }

    public int getMainPanelY() {
        return mainGamePanel.getY();
    }

    public void shrinkFast() {
        mainGamePanel.shrinkFast();
    }

    public void shrink() {
        for (GamePanel gamePanel : gamePanels) {
//            if (gamePanel.getPanelStatus().equals(PanelStatus.shrinkable)) {
                gamePanel.shrink();
//            }
        }
    }

    public void setXpAmount(int xpAmount) {
        mainGamePanel.setXpAmount(xpAmount);
    }

    public void stretch(GamePanel gamePanel, int code) {
//        if (gamePanel.getPanelStatus().equals(PanelStatus.shrinkable)) {
            gamePanel.stretch(code);
//        }

    }

    public int getMainPanelHeight() {
        return mainGamePanel.getHeight();
    }

    public int getMainPanelWidth() {
        return mainGamePanel.getWidth();
    }

    public void endingScene() {
        mainGamePanel.endingScene();
    }
}
