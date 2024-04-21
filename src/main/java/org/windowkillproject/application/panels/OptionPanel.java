package org.windowkillproject.application.panels;

import org.windowkillproject.application.Config;
import org.windowkillproject.model.entities.EpsilonModel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static org.windowkillproject.application.Application.shopFrame;
import static org.windowkillproject.application.Config.OPTION_HEIGHT;
import static org.windowkillproject.application.Config.OPTION_WIDTH;
public class OptionPanel extends Panel {

    private JLabel specialtyName ;
    private JLabel xpAmount ;
    private JLabel imgDisplay ;

    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    private JToggleButton selectButton = new JToggleButton("PURC  HASE");
    private boolean purchased, chosen;

    public OptionPanel(SpecialtyName specialtyName, int xpAmount, BufferedImage img) {
        super();
        setBackground(Color.decode("#d3ab97"));
        setPreferredSize(new Dimension(OPTION_WIDTH, OPTION_HEIGHT));
        setFocusable(true);
        this.specialtyName.setText(specialtyName.getDisplayName());
        this.xpAmount.setText("xp : " + xpAmount);
        this.imgDisplay.setIcon(new ImageIcon(img));
//        setSelectButtonListener();

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

        selectButton = new JToggleButton("PURCHASE");
        selectButton.setSelected(false);
        selectButton.setBounds(70, 330 , 150, 50);
        selectButton.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        selectButton.setBackground(Color.decode("#7C4F63"));
        selectButton.setForeground(Color.white);
        components.add(selectButton);
        return components;
    }

    public void setSelectButtonListener() {
        selectButton.addChangeListener(e -> {
            if (!isPurchased()) {
//                int result = showPurchasePopUP();
//                if (result == JOptionPane.OK_OPTION) {
                    int itemXp =Integer.parseInt(xpAmount.getText().substring(5));
                    int epsilonXP = EpsilonModel.getINSTANCE().getXp();
                    if (itemXp <= epsilonXP){
                        setPurchased(true);
                        EpsilonModel.getINSTANCE().setXp(epsilonXP - itemXp);
                    }else {
                        System.out.println("ure poor xd");// todo
                    }
//                }
//                JOptionPane.getRootFrame().dispose();
            }else {
                if (chosen){
                    selectButton.setText("SElECT");
                }else selectButton.setText("SELECTED");
            chosen =!chosen;
            }
        });
    }


    private int showPurchasePopUP(){
        return JOptionPane.showConfirmDialog(null,
                "if u wanna select this item u must purchase it" +
                        "\nthis item costs "+xpAmount.getText()+ " xp" , "attention",
                JOptionPane.OK_CANCEL_OPTION);
    }
    public enum SpecialtyName{
        Banish("O' Hephaestus، Banish") ,
        Empower("O’ Athena، Empower"),
        Heal("O' Apollo، Heal");
        private final String displayName;
        SpecialtyName(String displayName){
            this.displayName = displayName;
        }
        public String getDisplayName(){
            return displayName;
        }
    }

}
