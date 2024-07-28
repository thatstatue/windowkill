package org.windowkillproject.client.ui.panels.etc;

import org.windowkillproject.Constants;
import org.windowkillproject.client.GameClient;

import org.windowkillproject.client.ui.panels.Panel;
import org.windowkillproject.client.view.ImgData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static org.windowkillproject.Constants.APP_HEIGHT;
import static org.windowkillproject.Constants.APP_WIDTH;


public class PrimaryPanel extends Panel {
    public PrimaryPanel(GameClient client) {
        super(client);
        setBackground(Color.decode("#3B2635"));
        setPreferredSize(new Dimension(APP_WIDTH, APP_HEIGHT));
    }

    protected ArrayList<Component> initComponents() {
        ArrayList<Component> componentArrayList = new ArrayList<>();


        {
            ActionListener actionListener = e -> client.getApp().startGame();
            JButton button = buttonMaker("Start", 330, 100, actionListener);
            componentArrayList.add(button);
        }
        {
            ActionListener actionListener = e -> client.getApp().showSettings();
            JButton button = buttonMaker("Settings", 480, 400, actionListener);
            componentArrayList.add(button);
        }
        {
            ActionListener actionListener = e -> client.getApp().initSTFrame();
            JButton button = buttonMaker("Skill Tree", 180, 300, actionListener);
            componentArrayList.add(button);
        }
        {
            ActionListener actionListener = e -> client.getApp().showTut();
            JButton button = buttonMaker("Tutorial", 630, 200, actionListener);
            componentArrayList.add(button);
        }

        JLabel imgDisplay = new JLabel(new ImageIcon(ImgData.getData().getBg()));
        imgDisplay.setBounds(-20,-200, 1000, 1000);
        componentArrayList.add(imgDisplay);

        return componentArrayList;
    }

}
