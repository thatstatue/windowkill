package org.windowkillproject.model;

import org.windowkillproject.application.panels.OptionPanel;

import static org.windowkillproject.controller.ElapsedTime.getTotalSeconds;

public abstract class Writ {
    private static OptionPanel.SpecialtyName chosenSkill;
    private static long initSeconds = Long.MIN_VALUE;
    private static int times;

    public static int getTimes() {
        return times;
    }
    public static void timesAddIncrement() {
        times++;
    }

    public static long getInitSeconds() {
        return initSeconds;
    }

    public static void setInitSeconds() {
        Writ.initSeconds = getTotalSeconds();
    }

    public static OptionPanel.SpecialtyName getChosenSkill() {
        return chosenSkill;
    }

    public static void setChosenSkill(OptionPanel.SpecialtyName chosenSkill) {
        Writ.chosenSkill = chosenSkill;
        System.out.println("here look "+ getChosenSkill().getDisplayName());
    }
}
