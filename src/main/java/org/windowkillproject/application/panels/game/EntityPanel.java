package org.windowkillproject.application.panels.game;

import org.windowkillproject.application.Config;
import org.windowkillproject.application.panels.Panel;
import org.windowkillproject.view.abilities.AbilityView;
import org.windowkillproject.view.entities.EntityView;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static java.awt.Toolkit.getDefaultToolkit;
import static org.windowkillproject.application.Application.getGameFrame;
import static org.windowkillproject.view.abilities.AbilityView.abilityViews;
import static org.windowkillproject.view.entities.EntityView.entityViews;

public class EntityPanel extends Panel {
    public EntityPanel(){
        super();
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
        for (EntityView entityView : entityViews){
            entityView.paint(g);
        }
        for (AbilityView abilityView : abilityViews){
            abilityView.paint(g);
        }

    }
}
