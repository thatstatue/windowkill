package org.windowkillproject.application.frames;

import org.windowkillproject.application.Application;
import org.windowkillproject.application.Config;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class PrimaryPanel extends JPanel {
    private final ArrayList <Component> components;
    public PrimaryPanel() {
        requestFocus();
        setLayout(null);
        setBackground(Color.decode("#3B2635"));
        setPreferredSize(new Dimension(Config.APP_WIDTH, Config.APP_HEIGHT));
        components = initComponents();
        addComponentsToPanel();
    }

    private ArrayList <Component> initComponents(){
        ArrayList <Component> componentArrayList = new ArrayList<>();

        {
            ActionListener actionListener = e -> Application.startGame();
            JButton startButton = buttonMaker("Start", 130, 130, actionListener);
            componentArrayList.add(startButton);
        }
        {
            ActionListener settingsActionListener = e -> Application.showSettings();
            JButton button = buttonMaker("Settings", 530, 430, settingsActionListener);
            componentArrayList.add(button);
        }

        return componentArrayList;
    }

    private JButton buttonMaker(String name, int x, int y, ActionListener actionListener){
        JButton jButton = new JButton(name);
        jButton.setBounds(x , y, Config.BUTTON_WIDTH, Config.BUTTON_HEIGHT);
        jButton.setFont(Config.BUTTON_FONT);
        jButton.setFocusPainted(false);
        jButton.setBackground(Config.BUTTON_BG_COLOR);
        jButton.setForeground(Config.BUTTON_FG_COLOR);
        jButton.addActionListener(actionListener);
        return jButton;
    }
    public void addComponentsToPanel(){
        for (Component component : components) {
            this.add(component);
        }
    }
}
