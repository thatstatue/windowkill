package org.windowkillproject.application.panels;

import org.windowkillproject.application.Config;
import org.windowkillproject.view.entities.EntityView;

import java.awt.*;
import java.util.ArrayList;
import static org.windowkillproject.view.entities.EntityView.entityViews;

public class GamePanel extends Panel {

    public GamePanel() {
        super();
        setBackground(Color.black);
        setPreferredSize(new Dimension(Config.GAME_WIDTH, Config.GAME_HEIGHT));
        setFocusable(true);
        requestFocusInWindow();
    }

    @Override
    protected ArrayList<Component> initComponents() {
        return null;//todo: add elapsed time etc
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (EntityView entityView : entityViews) {
            entityView.paint(g);
        }
    }
}
