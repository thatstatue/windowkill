package org.windowkillproject.client.ui.panels.shop;

import org.windowkillproject.SpecialityName;
import org.windowkillproject.client.GameClient;

import javax.swing.*;
import java.awt.*;

import static org.windowkillproject.Request.*;

public class SelectButton extends JButton {
    private final Color color = Color.decode("#7C4F63");

    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
        if (!purchased) setText("PURCHASE");

    }
    private GameClient client;
    private boolean purchased, on;
    private int xpAmount;


    private SpecialityName specialtyName = SpecialityName.Heal;

    public void setSpecialtyName(SpecialityName specialtyName) {
        this.specialtyName = specialtyName;
    }

    public SelectButton(String name, GameClient client) {
        this.client = client;
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
                    var epsilonXp = client.getApp().getGameFrame().getXpAmount();
                    if (xpAmount <= epsilonXp) {
                        setPurchased(true);
                        setOn(true);
                        setWrit();
                        client.sendMessage(RES_SET_EPSILON_XP + REGEX_SPLIT + (epsilonXp - xpAmount));
                        setXpAmount(epsilonXp - xpAmount);
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
        if(specialtyName.equals(SpecialityName.Astrape)||specialtyName.equals(SpecialityName.Cerberus)){
            if (!SkillTreePanel.ares.isPurchased()) return false;
            if (specialtyName.equals(SpecialityName.Cerberus)) {
                return SkillTreePanel.astrape.isPurchased();
            }
        }

        //defence
        if(specialtyName.equals(SpecialityName.Melampus)||specialtyName.equals(SpecialityName.Chiron)){
            if (!SkillTreePanel.aceso.isPurchased()) return false;
            if (specialtyName.equals(SpecialityName.Chiron)) {
                return SkillTreePanel.melampus.isPurchased();
            }
        }

        //morph
        if(specialtyName.equals(SpecialityName.Empusa)||specialtyName.equals(SpecialityName.Dolus)){
            if (!SkillTreePanel.proteus.isPurchased()) return false;
            if (specialtyName.equals(SpecialityName.Dolus)) {
                return SkillTreePanel.empusa.isPurchased();
            }
        }
        return true;
    }
    private boolean writChosen;

    public void setWritChosen(boolean writChosen) {
        this.writChosen = writChosen;
    }

    private void setWrit() {
        client.sendMessage(REQ_WRIT_CHOSEN );
        if (specialtyName != null && (!specialtyName.equals(SpecialityName.Heal))) {
            if (on) {
                if (writChosen) {
                    for (int i = 0; i < 9; i++){
                        SkillTreePanel.skills[i].setOn(false);
                    }
                    if (specialtyName.equals(SpecialityName.Ares)) SkillTreePanel.ares.setOn(true);
                    if (specialtyName.equals(SpecialityName.Astrape)) SkillTreePanel.astrape.setOn(true);
                    if (specialtyName.equals(SpecialityName.Cerberus)) SkillTreePanel.cerberus.setOn(true);

                    if (specialtyName.equals(SpecialityName.Proteus)) SkillTreePanel.proteus.setOn(true);
                    if (specialtyName.equals(SpecialityName.Empusa)) SkillTreePanel.empusa.setOn(true);
                    if (specialtyName.equals(SpecialityName.Dolus)) SkillTreePanel.dolus.setOn(true);

                    if (specialtyName.equals(SpecialityName.Aceso)) SkillTreePanel.aceso.setOn(true);
                    if (specialtyName.equals(SpecialityName.Melampus)) SkillTreePanel.melampus.setOn(true);
                    if (specialtyName.equals(SpecialityName.Chiron)) SkillTreePanel.chiron.setOn(true);

                }
                client.sendMessage(RES_WRIT_CHOSEN + REGEX_SPLIT +specialtyName);
            } else {
                client.sendMessage(RES_WRIT_CHOSEN + REGEX_SPLIT + null);
                writChosen = false;
            }
        }
    }


    private int showPurchasePopUP() {
        return JOptionPane.showConfirmDialog(null,
                "if u wanna select this item u must purchase it" +
                        "\nthis item costs " + xpAmount + " xp", "attention",
                JOptionPane.OK_CANCEL_OPTION);
    }

}
