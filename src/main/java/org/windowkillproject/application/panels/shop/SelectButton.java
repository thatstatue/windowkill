package org.windowkillproject.application.panels.shop;

import org.windowkillproject.model.Writ;
import org.windowkillproject.model.entities.EpsilonModel;

import javax.swing.*;
import java.awt.*;

import static org.windowkillproject.application.Application.getGameFrame;
import static org.windowkillproject.application.panels.shop.OptionPanel.SpecialtyName.*;
import static org.windowkillproject.application.panels.shop.SkillTreePanel.*;

public class SelectButton extends JButton {
    private final Color color = Color.decode("#7C4F63");

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

    public void setSpecialtyName(OptionPanel.SpecialtyName specialtyName) {
        this.specialtyName = specialtyName;
    }

    public SelectButton(String name) {
        setText(name);
        setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        setBackground(color);
        setForeground(Color.white);
        setSelectButtonListener();
    }

    public void setXpAmount(int xpAmount) {
        this.xpAmount = xpAmount;
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
        setText("PURCHASE");
        if (purchased) setText("SELECT");
        setBackground(color);
        if (on) {
            setText("SELECTED");
            setBackground(Color.green);
        }
    }

    private void setSelectButtonListener() {
        addActionListener(e -> {
            setEnabled(false);
            if (isUnlocked()){
            if (!isPurchased()) {
                int result = showPurchasePopUP();
                if (result == JOptionPane.OK_OPTION) {
                    int epsilonXP = EpsilonModel.getINSTANCE().getXp();
                    if (xpAmount <= epsilonXP) {
                        setPurchased(true);
                        setOn(true);
                        setWrit();
                        EpsilonModel.getINSTANCE().setXp(epsilonXP - xpAmount);
                        getGameFrame().setXpAmount(EpsilonModel.getINSTANCE().getXp());
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "you don't have enough xp");
                    }
                }
                JOptionPane.getRootFrame().dispose();
            } else {
                setOn(!on);
                setWrit();
            }}else{
                JOptionPane.showMessageDialog(null,
                        "you haven't unlocked this specialty yet");
            }
            setEnabled(true);
        });
    }

    private boolean isUnlocked(){
        //attack
        if(specialtyName.equals(Astrape)||specialtyName.equals(Cerberus)){
            if (!ares.isPurchased()) return false;
            if (specialtyName.equals(Cerberus)) {
                return astrape.isPurchased();
            }
        }

        //defence
        if(specialtyName.equals(Melampus)||specialtyName.equals(Chiron)){
            if (!aceso.isPurchased()) return false;
            if (specialtyName.equals(Chiron)) {
                return melampus.isPurchased();
            }
        }

        //morph
        if(specialtyName.equals(Empusa)||specialtyName.equals(Dolus)){
            if (!proteus.isPurchased()) return false;
            if (specialtyName.equals(Dolus)) {
                return empusa.isPurchased();
            }
        }
        return true;
    }
    private void setWrit() {
        if (specialtyName != null && (specialtyName.equals(Ares) || specialtyName.equals(Aceso)
                || specialtyName.equals(Proteus))) {
            if (on) {
                if (Writ.getChosenSkill() != null) {
                    for (int i = 0; i < 9; i++){
                        skills[i].setOn(false);
                    }
                    if (specialtyName.equals(Ares)) ares.setOn(true);
                    if (specialtyName.equals(Astrape)) astrape.setOn(true);
                    if (specialtyName.equals(Cerberus)) cerberus.setOn(true);

                    if (specialtyName.equals(Proteus)) proteus.setOn(true);
                    if (specialtyName.equals(Empusa)) empusa.setOn(true);
                    if (specialtyName.equals(Dolus)) dolus.setOn(true);

                    if (specialtyName.equals(Aceso)) aceso.setOn(true);
                    if (specialtyName.equals(Melampus)) melampus.setOn(true);
                    if (specialtyName.equals(Chiron)) chiron.setOn(true);

                }
                Writ.setChosenSkill(specialtyName);
            } else Writ.setChosenSkill(null);
        }
    }


    private int showPurchasePopUP() {
        return JOptionPane.showConfirmDialog(null,
                "if u wanna select this item u must purchase it" +
                        "\nthis item costs " + xpAmount + " xp", "attention",
                JOptionPane.OK_CANCEL_OPTION);
    }

}
