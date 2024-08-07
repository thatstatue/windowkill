package org.windowkillproject.client.ui.panels.etc;

import org.windowkillproject.client.GameClient;
import org.windowkillproject.client.ui.panels.Panel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static org.windowkillproject.Constants.*;

public class LeaguePanel extends Panel {
    public LeaguePanel(GameClient client) {
        super(client);
        setBackground(Color.decode("#d3ab97"));
        setPreferredSize(new Dimension(APP_WIDTH, APP_HEIGHT));
    }



    @Override
    protected ArrayList<Component> initComponents() {
        ArrayList<Component> componentArrayList = new ArrayList<>();

        JLabel name = jLabelMaker("LEAGUE", 50, 20, 200, 50);
        name.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 35));
        name.setForeground(BUTTON_BG_COLOR);
        componentArrayList.add(name);

        componentArrayList.add(buttonMaker("Menu", 790, 20, e -> client.getApp().initPFrame()));

//        componentArrayList.add(buttonMaker("LOGIN", 600, 150, e -> {
//            monoDialog.dispose();
//            monoDialog = showNamePopup();
//            monoDialog.setVisible(true);
//
//        }));


        return componentArrayList;
    }
//
//    public JDialog showNamePopup() {
//        JDialog dialog = new JDialog(client.getApp().getSettingsFrame());
//        dialog.getContentPane().setBackground(Color.decode("#7C4F63"));
//        dialog.setLocationRelativeTo(null);
//        dialog.setFocusable(true);
//        dialog.setSize(350, 220);
//        JLabel message = new JLabel("you need to set a username to log in!");
//        message.setBounds(40, 10, 350, 90);
//        message.setForeground(Color.white);
//        dialog.add(message);
//        JTextField name = new JTextField("type username");
//        dialog.add(name);
//
//        return dialog;
//    }
}
