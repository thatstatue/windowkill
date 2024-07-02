package org.windowkillproject.application.frames;

import org.windowkillproject.application.Config;
import org.windowkillproject.application.panels.game.GamePanel;
import org.windowkillproject.application.panels.game.PanelStatus;
import org.windowkillproject.application.panels.game.MainGamePanel;

import javax.swing.*;
import java.awt.*;

import static org.windowkillproject.application.panels.game.GamePanel.gamePanels;

public class GameFrame extends JFrame {

 //   private JLayeredPane layeredPane = new JLayeredPane();
    private MainGamePanel mainGamePanel;

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
//        layeredPane.setBounds(0, 0, 1600, 800);
//        add(layeredPane, BorderLayout.CENTER);
        mainGamePanel = new MainGamePanel(PanelStatus.shrinkable);
        add(mainGamePanel);
//        System.out.println(mainGamePanel.getX()+ " "+mainGamePanel.getY() + ">>>>>>");
//        layeredPane.add(mainGamePanel, 0, 0);
    }


    @Override
    public void update(Graphics g) {
        super.update(g);
        for (GamePanel panel : gamePanels) {
            panel.revalidate();
            panel.repaint();
        }
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
    public boolean isExploding(){
        return mainGamePanel.isExploding();
    }

    public void setClockTime(String time) {
        mainGamePanel.setClockTime(time);
    }
    public int getMainPanelX(){
        return mainGamePanel.getX();
    }
    public int getMainPanelY(){
        return mainGamePanel.getY();
    }

    public void shrinkFast() {
        mainGamePanel.shrinkFast();
    }

    public void shrink() {
        mainGamePanel.shrink();
    }

    public void setXpAmount(int xpAmount) {
        mainGamePanel.setXpAmount(xpAmount);
    }
    public void stretch(int code){
        mainGamePanel.stretch(code);
    }
    public int getMainPanelHeight(){
        return mainGamePanel.getHeight();
    }
    public int getMainPanelWidth(){
        return mainGamePanel.getWidth();
    }

    public void endingScene() {
        mainGamePanel.endingScene();
    }
}
