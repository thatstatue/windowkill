package org.windowkillproject.server.model;

import org.windowkillproject.MessageQueue;
import org.windowkillproject.SpecialityName;
import org.windowkillproject.server.model.abilities.BulletModel;


import java.io.Serial;
import java.io.Serializable;

import static org.windowkillproject.Constants.*;
import static org.windowkillproject.Constants.EPSILON_RADIUS;

import static org.windowkillproject.server.model.entities.EpsilonModel.queueEpsilonModelMap;

public class Writ implements Serializable {

    private SpecialityName chosenSkill;
    private long initSeconds = Long.MIN_VALUE;
    private int times, acceptedClicks;
    private final MessageQueue messageQueue;

    public Writ(MessageQueue messageQueue) {
        this.messageQueue= messageQueue;
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
        var epsilon = queueEpsilonModelMap.get(messageQueue);
        if (epsilon.getXp() >= 100) {
            initSeconds = epsilon.globeModel.getElapsedTime().getTotalSeconds();
        }
    }
    public void resetInitSeconds() {
        this.initSeconds = Long.MIN_VALUE;
    }
    public SpecialityName getChosenSkill() {
        return chosenSkill;
    }

    public void setChosenSkill(SpecialityName chosenSkill) {
        this.chosenSkill = chosenSkill;
    }
    public void check(){
        var epsilon = queueEpsilonModelMap.get(messageQueue);
        long now = epsilon.globeModel.getElapsedTime().getTotalSeconds();
        if (getInitSeconds() > 0 && now - getInitSeconds() <= WRIT_DURATION) {
            switch (getChosenSkill()) {
                case Ares -> {
                    BulletModel.setAttackHp(BULLET_ATTACK_HP + 2);
                }
                case Astrape -> {
                    epsilon.setAstrapper(true);
                }
                case Cerberus -> {
                    //
                }
                case Aceso -> {
                    if (now - getInitSeconds() >= getTimes() && getTimes() < 10) {
                        epsilon.setHp(epsilon.getHp() + 1);
                        timesAddIncrement();
                    }
                }
                case Melampus -> {
                    epsilon.setMelame(true);
                }
                case Chiron -> {
                    epsilon.setChironner(true);
                }

                case Proteus -> {
                    if (getTimes() < getAcceptedClicks()) {
                        epsilon.spawnVertex();
                        timesAddIncrement();
                    }
                }
                case Empusa -> {
                    epsilon.setRadius((int) (EPSILON_RADIUS * 0.9));
                }
                case Dolus -> {
                }
            }
        } else {
            BulletModel.setAttackHp(BULLET_ATTACK_HP);
            epsilon.setAstrapper(false);
            epsilon.setMelame(false);
            epsilon.setChironner(false);
            if (epsilon.getRadius() < EPSILON_RADIUS)
                epsilon.setRadius(EPSILON_RADIUS);
        }
    }
}
