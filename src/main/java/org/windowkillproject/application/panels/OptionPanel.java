package org.windowkillproject.application.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static org.windowkillproject.application.Config.OPTION_HEIGHT;
import static org.windowkillproject.application.Config.OPTION_WIDTH;

public class OptionPanel extends Panel {

    private JLabel specialtyName;
    private JLabel xpAmount;
    private JLabel imgDisplay;


    private Button selectButton;
    public boolean isOn(){
        return selectButton.isOn();
    }
    public void setOn(boolean selected){
        selectButton.setOn(selected);
    }

    public boolean isPurchased() {
        return selectButton.isPurchased();
    }

    public void setPurchased(boolean purchased) {
        selectButton.setPurchased(false);
    }


    public OptionPanel(SpecialtyName specialtyName, int xpAmount, BufferedImage img) {
        super();
        setBackground(Color.decode("#d3ab97"));
        setPreferredSize(new Dimension(OPTION_WIDTH, OPTION_HEIGHT));
        setFocusable(true);
        this.specialtyName.setText(specialtyName.getDisplayName());
        this.xpAmount.setText("xp : " + xpAmount);
        selectButton.setXpAmount(xpAmount);
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

        selectButton = new Button("PURCHASE");
        selectButton.setBounds(70, 330, 150, 50);

        components.add(selectButton);
        return components;
    }

    public enum SpecialtyName {
        Banish("O' Hephaestus، Banish"),
        Empower("O’ Athena، Empower"),
        Heal("O' Apollo، Heal");
        private final String displayName;

        SpecialtyName(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

}
