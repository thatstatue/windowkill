package org.windowkillproject.client.ui.frames;

import org.windowkillproject.client.GameClient;
import org.windowkillproject.client.ui.panels.game.MainPanelView;
import org.windowkillproject.client.ui.panels.game.EntityPanel;


import javax.swing.*;
import java.awt.*;

import static org.windowkillproject.Constants.CENTER_X;
import static org.windowkillproject.Constants.CENTER_Y;
import static org.windowkillproject.Constants.GAME_TITLE;
import static org.windowkillproject.client.ui.panels.game.PanelView.panelViews;

public class GameFrame extends JFrame {

    private JLayeredPane layeredPane;
    private MainPanelView mainPanelView;
    private EntityPanel entityPanel;

    @Override
    public JLayeredPane getLayeredPane() {
        return layeredPane;
    }

    public MainPanelView getMainPanelView() {
        return mainPanelView;
    }

    public void setMainPanelView(MainPanelView mainPanelView) {
        if (this.mainPanelView!= null){
            panelViews.remove(this.mainPanelView);
        }
        this.mainPanelView = mainPanelView;
    }

    public GameFrame(String id, GameClient client) {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setUndecorated(true);
        setAlwaysOnTop(true);
        setTitle(GAME_TITLE);
        this.setLayout(null);
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setBackground(new Color(0, 0, 0, 0));
        setLocationRelativeTo(null);

        mainPanelView = new MainPanelView(id ,client);
        entityPanel = new EntityPanel(client);

        initLayeredPane();
    }

    private void initLayeredPane() {
        layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, CENTER_X * 2, CENTER_Y * 2);
        layeredPane.setLayout(null);
        layeredPane.add(mainPanelView, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(entityPanel, JLayeredPane.PALETTE_LAYER);
        setContentPane(layeredPane);
    }

    public void setWaveLevel(int level) {
        mainPanelView.setWaveLevel(level);
    }

    public void initLabels() {
        mainPanelView.initLabels();
    }

    public JLabel[] getLabels() {
        return mainPanelView.getLabels();
    }


    public void setXpAmount(int xpAmount) {
        mainPanelView.setXpAmount(xpAmount);
    }
    public int getXpAmount(){
        return mainPanelView.getXpAmount();
    }


}
