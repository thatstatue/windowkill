package org.windowkillproject.view.entities;

import org.windowkillproject.application.panels.game.GamePanel;
import org.windowkillproject.view.ObjectView;

import java.awt.*;
import java.util.ArrayList;

public abstract class EntityView extends ObjectView {


    public static ArrayList<EntityView> entityViews = new ArrayList<>();

    public EntityView(String id) {
        super(id);
        entityViews.add(this);
    }
    public void set(int x, int y, int width, int height) {
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);

    }
//    @Override
//    public void paintComponent(Graphics g) {
//        super.paintComponent(g);
//    }

    }
