package org.windowkillproject.application.panels;

import org.windowkillproject.model.Writ;
import org.windowkillproject.model.entities.EpsilonModel;

import javax.swing.*;
import java.awt.*;

import static org.windowkillproject.application.Application.getGameFrame;
import static org.windowkillproject.application.panels.OptionPanel.SpecialtyName.*;

public class Button extends JButton {
    private final Color color =Color.decode("#7C4F63");

    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
        if (!purchased) setText("PURCHASE");

    }
    private boolean purchased, on;
    private int xpAmount;
    private OptionPanel.SpecialtyName specialtyName = Heal;

    public OptionPanel.SpecialtyName getSpecialtyName() {
        return specialtyName;
    }

    public void setSpecialtyName(OptionPanel.SpecialtyName specialtyName) {
        this.specialtyName = specialtyName;
    }

    public Button (String name){
        setText(name);
        setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        setBackground(color);
        setForeground(Color.white);
        setSelectButtonListener();
    }
    public void setXpAmount(int xpAmount){
        this.xpAmount= xpAmount;
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
        setText("SELECT");
        setBackground(color);
        setWrit(on);
        if (on) {
            setText("SELECTED");
            setBackground(Color.green);
        }
    }

    private void setSelectButtonListener() {
        addActionListener(e -> {
            setEnabled(false);
            if (!isPurchased()) {
                int result = showPurchasePopUP();
                if (result == JOptionPane.OK_OPTION) {
                    int epsilonXP = EpsilonModel.getINSTANCE().getXp();
                    if (xpAmount <= epsilonXP) {
                        setPurchased(true);
                        on = true;
                        setOn(true);
                        EpsilonModel.getINSTANCE().setXp(epsilonXP - xpAmount);
                        getGameFrame().setXpAmount(EpsilonModel.getINSTANCE().getXp());

                        setWrit(true);
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "you don't have enough xp");
                    }
                }
                JOptionPane.getRootFrame().dispose();
            } else {
               setOn(!on);
            }
            setEnabled(true);
        });
    }

    private void setWrit(boolean set) {
        if (specialtyName != null &&(specialtyName.equals(Ares) || specialtyName.equals(Aceso)
        || specialtyName.equals(Proteus))){
            if (set) Writ.setChosenSkill(specialtyName);
            else Writ.setChosenSkill(null);
        }
        if (specialtyName == null) System.out.println("خاکبرسر");
    }


    private int showPurchasePopUP() {
        return JOptionPane.showConfirmDialog(null,
                "if u wanna select this item u must purchase it" +
                        "\nthis item costs " + xpAmount + " xp", "attention",
                JOptionPane.OK_CANCEL_OPTION);
    }

}
