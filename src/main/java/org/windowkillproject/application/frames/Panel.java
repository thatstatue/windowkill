package org.windowkillproject.application.frames;

import org.windowkillproject.application.Config;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public abstract class Panel extends JPanel {
    private final ArrayList<Component> components;
    public Panel() {
        requestFocus();
        setLayout(null);
        components = initComponents();
        addComponentsToPanel();
    }

    protected abstract ArrayList <Component> initComponents();

    protected final JButton buttonMaker(String name, int x, int y, ActionListener actionListener){
        JButton jButton = new JButton(name);
        jButton.setBounds(x , y, Config.BUTTON_WIDTH, Config.BUTTON_HEIGHT);
        jButton.setFont(Config.BUTTON_FONT);
        jButton.setFocusPainted(false);
        jButton.setBackground(Config.BUTTON_BG_COLOR);
        jButton.setForeground(Config.BUTTON_FG_COLOR);
        jButton.addActionListener(actionListener);
        return jButton;
    }
    protected void addComponentsToPanel(){
        for (Component component : components) {
            this.add(component);
        }
    }
}
