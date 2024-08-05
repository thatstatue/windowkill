package org.windowkillproject.client.ui.panels.game;

import org.windowkillproject.client.GameClient;
import org.windowkillproject.client.ui.panels.Panel;
import org.windowkillproject.client.view.entities.EntityView;

import java.awt.*;
import java.util.ArrayList;

import static java.awt.Toolkit.getDefaultToolkit;
import static org.windowkillproject.client.view.abilities.AbilityView.abilityViews;
import static org.windowkillproject.client.view.entities.EntityView.entityViews;

public class EntityPanel extends Panel {
    public EntityPanel(GameClient client){
        super(client);
        setPreferredSize(getDefaultToolkit().getScreenSize());
        setSize(getDefaultToolkit().getScreenSize());
        setLocation((getDefaultToolkit().getScreenSize().width - getWidth())/2,
                (getDefaultToolkit().getScreenSize().height - getHeight())/2);
        setOpaque(false);
    }
    @Override
    protected ArrayList<Component> initComponents() {
       return null;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
// TODO URGENT add panelBounds here to just draw them, they'll be changed through id from their paired panelmodel
        for (int i = 0; i < abilityViews.size(); i++) {
            abilityViews.get(i).paint(g);
        }
        for (int i = 0; i < entityViews.size(); i++) {
            EntityView entityView = entityViews.get(i);
            if (entityView.isHovering()) entityView.paint(g);
        }
        for (int i = 0; i < entityViews.size(); i++) {
            EntityView entityView = entityViews.get(i);
            if (!entityView.isHovering()) entityView.paint(g);
        }

    }
}
