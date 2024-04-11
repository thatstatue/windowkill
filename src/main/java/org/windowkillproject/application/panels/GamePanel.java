package org.windowkillproject.application.frames;

import org.windowkillproject.application.Config;
import org.windowkillproject.model.entities.EpsilonModel;
import org.windowkillproject.model.entities.enemies.TrigorathModel;
import org.windowkillproject.view.EntityView;

import java.awt.*;
import java.util.ArrayList;
import static org.windowkillproject.application.Application.gameFrame;
import static org.windowkillproject.view.EntityView.entityViews;

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

    public void keepEpsilonInBounds(){
        var epsilonModel = EpsilonModel.getINSTANCE();
        int endX = epsilonModel.getWidth() + epsilonModel.getX() + 5+ epsilonModel.getRadius();
        int endY = epsilonModel.getHeight() + epsilonModel.getY() + 3* epsilonModel.getRadius();
        if (endY > gameFrame.getHeight()) {
            int deltaY = gameFrame.getHeight() - endY;
            epsilonModel.move(0, deltaY);
        }
        if (endX > gameFrame.getWidth()) {
            int deltaX = gameFrame.getWidth() - endX;
            epsilonModel.move(deltaX,0);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (EntityView entityView : entityViews) {
            entityView.paint(g);
        }
    }
}
