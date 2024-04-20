package org.windowkillproject.application.panels;

import org.windowkillproject.application.Config;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static org.windowkillproject.application.Config.OPTION_HEIGHT;
import static org.windowkillproject.application.Config.OPTION_WIDTH;

public class OptionPanel extends Panel {
    private JLabel specialtyName ;
    private JLabel xpAmount ;
    private JLabel imgDisplay ;
    private JToggleButton selectButton;

    public OptionPanel(String specialtyName, int xpAmount, BufferedImage img) {
        super();
        setBackground(Color.decode("#d3ab97"));
        setPreferredSize(new Dimension(OPTION_WIDTH, OPTION_HEIGHT));
        setFocusable(true);
        this.specialtyName.setText(specialtyName);
        this.xpAmount.setText("xp : " + xpAmount);
        this.imgDisplay.setIcon(new ImageIcon(img));
    }



    @Override
    protected ArrayList<Component> initComponents() {
        ArrayList<Component> components = new ArrayList<>();
        specialtyName = new JLabel();
        specialtyName.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        specialtyName.setForeground(Color.white);
        specialtyName.setBounds(30, 30, OPTION_WIDTH, 20);
        components.add(specialtyName);

        xpAmount = new JLabel();
        xpAmount.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        xpAmount.setForeground(Color.green);
        xpAmount.setBounds(160, 60, OPTION_WIDTH, 20);
        components.add(xpAmount);

        imgDisplay = new JLabel();
        imgDisplay.setBounds(40, 80, OPTION_WIDTH, 250);
        components.add(imgDisplay);

        selectButton = new JToggleButton("SELECT");
        selectButton.setSelected(false);
//        selectButton.addChangeListener(e -> {
//
//        });todo
        selectButton.setBounds(70, 330 , 150, 50);
        selectButton.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        selectButton.setBackground(Color.decode("#7C4F63"));
        selectButton.setForeground(Color.white);
        components.add(selectButton);
        return components;
    }
}
