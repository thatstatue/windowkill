package org.windowkillproject.client.ui.panels.shop;

import org.windowkillproject.SpecialityName;
import org.windowkillproject.client.GameClient;
import org.windowkillproject.client.ui.panels.Panel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static org.windowkillproject.Constants.OPTION_HEIGHT;
import static org.windowkillproject.Constants.OPTION_WIDTH;

public class OptionPanel extends Panel {

    private JLabel specialty;
    private JLabel xpAmount;
    private JLabel imgDisplay;


    private SelectButton selectButton;

    public boolean isOn() {
        return selectButton.isOn();
    }

    public void setOn(boolean selected) {
        selectButton.setOn(selected);
    }

    public void setPurchased(boolean purchased) {
        selectButton.setPurchased(purchased);
    }
    public boolean isPurchased() {
         return selectButton.isPurchased();
    }



    public OptionPanel(SpecialityName specialtyName, int xpAmount, BufferedImage img, GameClient client) {
        super(client);
        setBackground(Color.decode("#d3ab97"));
        setPreferredSize(new Dimension(OPTION_WIDTH, OPTION_HEIGHT));
        setFocusable(true);
        this.specialty.setText(specialtyName.getDisplayName());
        this.xpAmount.setText("xp : " + xpAmount);
        selectButton.setXpAmount(xpAmount);
        selectButton.setSpecialtyName(specialtyName);
//        this.imgDisplay.setIcon(new ImageIcon(img));

    }
    public void setWritChosen(boolean specialityName){
        selectButton.setWritChosen(specialityName);
    }
    @Override
    protected ArrayList<Component> initComponents() {
        ArrayList<Component> components = new ArrayList<>();
        specialty = new JLabel();
        specialty.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        specialty.setForeground(Color.white);
        specialty.setBounds(30, 30, OPTION_WIDTH, 20);
        components.add(specialty);

        xpAmount = new JLabel();
        xpAmount.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        xpAmount.setForeground(Color.green);
        xpAmount.setBounds(160, 50, OPTION_WIDTH, 20);
        components.add(xpAmount);
//
//        imgDisplay = new JLabel();
//        imgDisplay.setBounds(40, 80, OPTION_WIDTH, OPTION_IMG_HEIGHT);
//        components.add(imgDisplay);
        selectButton = new SelectButton("PURCHASE", client);
        selectButton.setBounds(70, 80, 150, 50);

        components.add(selectButton);
        return components;
    }



}
