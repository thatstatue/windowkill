package org.windowkillproject.application.frames;

import org.windowkillproject.application.Config;
import org.windowkillproject.model.entities.EpsilonModel;
import org.windowkillproject.view.EntityView;
import org.windowkillproject.view.EpsilonView;

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
            epsilonModel.moveY(deltaY);
        }
        if (endX > gameFrame.getWidth()) {
            int deltaX = gameFrame.getWidth() - endX;
            epsilonModel.moveX(deltaX);
        }
    }
    public void createEnemy() {
//        TrigorathModel trigorathModel = new TrigorathModel(300, 420);
//        entities.add(trigorathModel);
//        entities.add(new TrigorathModel(30, 100));
        //todo: fix
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (EntityView entityView : entityViews) {
            entityView.paint(g);
        }
    }
}
