package org.windowkillproject.application.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static org.windowkillproject.application.Config.*;

public class OptionPanel extends Panel {

    private JLabel specialty;
    private JLabel xpAmount;
    private JLabel imgDisplay;


    private Button selectButton;

    public boolean isOn() {
        return selectButton.isOn();
    }

    public void setOn(boolean selected) {
        selectButton.setOn(selected);
    }

    public void setPurchased(boolean purchased) {
        selectButton.setPurchased(purchased);
    }


    public OptionPanel(SpecialtyName specialtyName, int xpAmount, BufferedImage img) {
        super();
        setBackground(Color.decode("#d3ab97"));
        setPreferredSize(new Dimension(OPTION_WIDTH, OPTION_HEIGHT));
        setFocusable(true);
        this.specialty.setText(specialtyName.getDisplayName());
        this.xpAmount.setText("xp : " + xpAmount);
        selectButton.setXpAmount(xpAmount);
        selectButton.setSpecialtyName(specialtyName);
        this.imgDisplay.setIcon(new ImageIcon(img));

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
        xpAmount.setBounds(160, 60, OPTION_WIDTH, 20);
        components.add(xpAmount);

        imgDisplay = new JLabel();
        imgDisplay.setBounds(40, 80, OPTION_WIDTH, OPTION_IMG_HEIGHT);
        components.add(imgDisplay);

        selectButton = new Button("PURCHASE");
        selectButton.setBounds(70, 330, 150, 50);

        components.add(selectButton);
        return components;
    }

    public enum SpecialtyName {
        Banish("O' Hephaestus، Banish"),
        Empower("O’ Athena، Empower"),
        Heal("O' Apollo، Heal"),
        Ares("Writ of Ares"),
        Aceso("Writ of Aceso"),
        Proteus("Writ of Proteus");
        private final String displayName;

        SpecialtyName(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

}
