package org.windowkillproject.controller;


import javax.swing.*;

import org.windowkillproject.view.EntityView;

import static org.windowkillproject.application.Application.gameFrame;
import static org.windowkillproject.application.Config.FRAME_UPDATE_TIME;
import static org.windowkillproject.application.Config.MODEL_UPDATE_TIME;
import static org.windowkillproject.controller.Controller.calculateViewLocation;
import static org.windowkillproject.view.EntityView.entityViews;

public class Update {
    public Update() {
        new Timer((int) FRAME_UPDATE_TIME, e -> updateView()) {{
            setCoalesce(true);
        }}.start();
        new Timer((int) MODEL_UPDATE_TIME, e -> updateModel()) {{
            setCoalesce(true);
        }}.start();
    }

    public void updateView() {
        for (EntityView entityView : entityViews) {
            entityView.setLocation(calculateViewLocation(gameFrame.getGamePanel(), entityView.getId()));
        }
        gameFrame.repaint();
    }

    public void updateModel() {
        //todo
    }
}
