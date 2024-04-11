package org.windowkillproject.model.abilities;

import org.windowkillproject.application.Config;
import org.windowkillproject.model.entities.EntityModel;
import org.windowkillproject.model.entities.enemies.EnemyModel;

import java.awt.geom.Area;
import java.util.ArrayList;


import static org.windowkillproject.application.Application.gameFrame;
import static org.windowkillproject.controller.Controller.createBulletView;
import static org.windowkillproject.model.entities.EntityModel.entityModels;

public class BulletModel extends AbilityModel {
    private int attackHp = 5;
    private final int delta = 10;

    public void setTheta(double theta) {
        this.theta = theta;
    }

    private double theta;

    public static ArrayList<BulletModel> bulletModels = new ArrayList<>();

    public int getAttackHp() {
        return attackHp;
    }

    public void move() {


        if (isShoot()) {
            setX(getX() + (int) (delta * Math.cos(theta)));
            setY(getY() + (int) (delta * Math.sin(theta)));

            //enemies getting shot
            for (EntityModel entityModel : entityModels) {
                if (entityModel instanceof EnemyModel) {
                    EnemyModel enemyModel = (EnemyModel) entityModel;
                    Area enemyA = new Area(enemyModel.getPolygon());
                    if (enemyA.contains(this.getX(), this.getY())) {
                        enemyModel.gotShoot(this);
                        explode();
                        break;
                    }
                }
            }

            //frame getting shot
            if (getX() < 0 || getX() > gameFrame.getWidth() ||
                    getY() < 0 || getY() > gameFrame.getHeight()) {
                if (getX() < 0) {
                    gameFrame.stretch(Config.BULLET_HIT_LEFT);
                }
                if (getX() > gameFrame.getWidth()) {
                    gameFrame.stretch(Config.BULLET_HIT_RIGHT);
                }
                if (getY() < 0) {
                    gameFrame.stretch(Config.BULLET_HIT_UP);
                }
                if (getY() > gameFrame.getHeight()) {
                    gameFrame.stretch(Config.BULLET_HIT_DOWN);
                }
                explode();
            }
        }
    }

    public BulletModel(int x, int y) {
        super(x, y);
        isShoot = false;
        bulletModels.add(this);
        createBulletView(id, x, y);
    }

    private boolean isShoot;


    public boolean isShoot() {
        return isShoot;
    }

    public void explode() {
        bulletModels.remove(this);
    }

    public void setShoot(boolean shoot) {
        isShoot = shoot;
    }
}
