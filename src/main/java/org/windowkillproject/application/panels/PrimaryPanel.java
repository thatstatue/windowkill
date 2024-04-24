package org.windowkillproject.application.panels;

import org.windowkillproject.application.Application;
import org.windowkillproject.application.Config;
import org.windowkillproject.application.panels.Panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class PrimaryPanel extends Panel {
    public PrimaryPanel(){
        super();
        setBackground(Color.decode("#3B2635"));
        setPreferredSize(new Dimension(Config.APP_WIDTH, Config.APP_HEIGHT));
    }
    protected ArrayList <Component> initComponents() {
        ArrayList<Component> componentArrayList = new ArrayList<>();

        {
            ActionListener actionListener = e -> Application.startGame();
            JButton button = buttonMaker("Start", 330, 100, actionListener);
            componentArrayList.add(button);
        }
        {
            ActionListener actionListener = e -> Application.showSettings();
            JButton button = buttonMaker("Settings", 480, 400, actionListener);
            componentArrayList.add(button);
        }
        {
            ActionListener actionListener = e -> Application.initSTFrame();
            JButton button = buttonMaker("Skill Tree", 180, 300, actionListener);
            componentArrayList.add(button);
        }
        {
            ActionListener actionListener = e -> Application.startGame();
            JButton button = buttonMaker("Tutorial", 630, 200, actionListener);
            componentArrayList.add(button);
        }

        return componentArrayList;
    }

}
