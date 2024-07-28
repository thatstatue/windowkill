package org.windowkillproject.server.model;

import org.windowkillproject.client.ui.panels.shop.OptionPanel;
import org.windowkillproject.server.ClientHandler;
import org.windowkillproject.server.model.entities.EpsilonModel;

import java.io.Serial;
import java.io.Serializable;

import static org.windowkillproject.controller.ElapsedTime.getTotalSeconds;

public class Writ implements Serializable {

    @Serial
    private final long serialVersionUID = 1L;
    private OptionPanel.SpecialtyName chosenSkill;
    private long initSeconds = Long.MIN_VALUE;
    private int times, acceptedClicks;
    private final ClientHandler clientHandler;

    public Writ(ClientHandler clientHandler) {
        this.clientHandler= clientHandler;
    }

    public int getTimes() {
        return times;
    }
    public void timesAddIncrement() {
        times++;
    }

    public int getAcceptedClicks() {
        return acceptedClicks;
    }
    public void acceptedClicksAddIncrement() {
        acceptedClicks++;
    }


    public long getInitSeconds() {
        return initSeconds;
    }

    public void setInitSeconds() {
        if (EpsilonModel.clientEpsilonModelMap.get(clientHandler).getXp() >= 100) {
            initSeconds = getTotalSeconds();
        }
    }
    public void resetInitSeconds() {
        this.initSeconds = Long.MIN_VALUE;
    }
    public OptionPanel.SpecialtyName getChosenSkill() {
        return chosenSkill;
    }

    public void setChosenSkill(OptionPanel.SpecialtyName chosenSkill) {
        this.chosenSkill = chosenSkill;
    }
}
