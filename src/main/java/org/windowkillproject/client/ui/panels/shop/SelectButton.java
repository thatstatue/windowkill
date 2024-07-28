package org.windowkillproject.client.ui.panels.shop;

import org.windowkillproject.client.ui.App;
import org.windowkillproject.server.model.Writ;
import org.windowkillproject.server.model.entities.EpsilonModel;

import javax.swing.*;
import java.awt.*;

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
    private OptionPanel.SpecialtyName specialtyName = OptionPanel.SpecialtyName.Heal;

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
                        App.getGameFrame().setXpAmount(EpsilonModel.getINSTANCE().getXp());
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
        if(specialtyName.equals(OptionPanel.SpecialtyName.Astrape)||specialtyName.equals(OptionPanel.SpecialtyName.Cerberus)){
            if (!SkillTreePanel.ares.isPurchased()) return false;
            if (specialtyName.equals(OptionPanel.SpecialtyName.Cerberus)) {
                return SkillTreePanel.astrape.isPurchased();
            }
        }

        //defence
        if(specialtyName.equals(OptionPanel.SpecialtyName.Melampus)||specialtyName.equals(OptionPanel.SpecialtyName.Chiron)){
            if (!SkillTreePanel.aceso.isPurchased()) return false;
            if (specialtyName.equals(OptionPanel.SpecialtyName.Chiron)) {
                return SkillTreePanel.melampus.isPurchased();
            }
        }

        //morph
        if(specialtyName.equals(OptionPanel.SpecialtyName.Empusa)||specialtyName.equals(OptionPanel.SpecialtyName.Dolus)){
            if (!SkillTreePanel.proteus.isPurchased()) return false;
            if (specialtyName.equals(OptionPanel.SpecialtyName.Dolus)) {
                return SkillTreePanel.empusa.isPurchased();
            }
        }
        return true;
    }
    private void setWrit() {
        if (specialtyName != null && (specialtyName.equals(OptionPanel.SpecialtyName.Ares) || specialtyName.equals(OptionPanel.SpecialtyName.Aceso)
                || specialtyName.equals(OptionPanel.SpecialtyName.Proteus))) {
            if (on) {
                if (Writ.getChosenSkill() != null) {
                    for (int i = 0; i < 9; i++){
                        SkillTreePanel.skills[i].setOn(false);
                    }
                    if (specialtyName.equals(OptionPanel.SpecialtyName.Ares)) SkillTreePanel.ares.setOn(true);
                    if (specialtyName.equals(OptionPanel.SpecialtyName.Astrape)) SkillTreePanel.astrape.setOn(true);
                    if (specialtyName.equals(OptionPanel.SpecialtyName.Cerberus)) SkillTreePanel.cerberus.setOn(true);

                    if (specialtyName.equals(OptionPanel.SpecialtyName.Proteus)) SkillTreePanel.proteus.setOn(true);
                    if (specialtyName.equals(OptionPanel.SpecialtyName.Empusa)) SkillTreePanel.empusa.setOn(true);
                    if (specialtyName.equals(OptionPanel.SpecialtyName.Dolus)) SkillTreePanel.dolus.setOn(true);

                    if (specialtyName.equals(OptionPanel.SpecialtyName.Aceso)) SkillTreePanel.aceso.setOn(true);
                    if (specialtyName.equals(OptionPanel.SpecialtyName.Melampus)) SkillTreePanel.melampus.setOn(true);
                    if (specialtyName.equals(OptionPanel.SpecialtyName.Chiron)) SkillTreePanel.chiron.setOn(true);

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
