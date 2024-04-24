package org.windowkillproject.model.abilities;

import org.windowkillproject.application.Config;
import org.windowkillproject.model.entities.EntityModel;
import org.windowkillproject.model.entities.EpsilonModel;
import org.windowkillproject.model.entities.enemies.EnemyModel;
import org.windowkillproject.view.abilities.BulletView;

import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.util.ArrayList;


import static org.windowkillproject.application.Application.getGameFrame;
import static org.windowkillproject.application.Config.BULLET_ATTACK_HP;
import static org.windowkillproject.application.Config.EPSILON_RADIUS;
import static org.windowkillproject.controller.Controller.createAbilityView;
import static org.windowkillproject.controller.GameController.impact;
import static org.windowkillproject.controller.Utils.unitVector;
import static org.windowkillproject.controller.Utils.weighedVector;
import static org.windowkillproject.model.entities.EntityModel.entityModels;

public class BulletModel extends AbilityModel {
    private static int attackHp = BULLET_ATTACK_HP;
    private final Point2D mousePoint;

    public static void setAttackHp(int attackHp) {
        BulletModel.attackHp = attackHp;
    }

    public static ArrayList<BulletModel> bulletModels = new ArrayList<>();

    public static int getAttackHp() {
        return attackHp;
    }

    public Point2D getMousePoint() {
        return mousePoint;
    }
    public void shot(){
        setShoot(true);
        move();
        move();
        move();
    }

    public void move() {


        if (isShoot()) {
            Point2D delta = unitVector( getMousePoint(), this.getAnchor());
            delta = weighedVector(delta, Config.EPSILON_SPEED* 2.5);
            //System.out.println(point2D.getX());
            /*
            setX((int) (getX() + point2D.getX()));
            setY((int) (getY() +point2D.getY()));
             */
            setX((int) (getX() + delta.getX()));
            setY((int) (getY() + delta.getY()));

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
            if (getX() < 0 || getX() > getGameFrame().getWidth() ||
                    getY() < 0 || getY() > getGameFrame().getHeight()) {
                if (getX() < 0) {
                    getGameFrame().stretch(Config.BULLET_HIT_LEFT);
                }
                if (getX() > getGameFrame().getWidth()) {
                    getGameFrame().stretch(Config.BULLET_HIT_RIGHT);
                }
                if (getY() < 0) {
                    getGameFrame().stretch(Config.BULLET_HIT_UP);
                }
                if (getY() > getGameFrame().getHeight()) {
                    getGameFrame().stretch(Config.BULLET_HIT_DOWN);
                }
                explode();
            }
        }
    }

    public BulletModel(int x, int y, Point2D mousePoint) {
        super(x, y);
        anchor = new Point2D.Double(x+ EPSILON_RADIUS/2, y + EPSILON_RADIUS*1.5);
        isShoot = false;
        bulletModels.add(this);
        this.mousePoint = mousePoint;
        createAbilityView(BulletView.class, id, x, y);
    }

    private boolean isShoot;


    public boolean isShoot() {
        return isShoot;
    }

    private void explode() {
        impact(this);
        bulletModels.remove(this);
        destroy();
    }

    public void setShoot(boolean shoot) {
        isShoot = shoot;
    }
}
