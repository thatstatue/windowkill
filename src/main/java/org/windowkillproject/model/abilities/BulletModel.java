package org.windowkillproject.model.abilities;

import org.windowkillproject.application.Config;
import org.windowkillproject.application.panels.game.GamePanel;
import org.windowkillproject.model.entities.EntityModel;
import org.windowkillproject.model.entities.enemies.EnemyModel;
import org.windowkillproject.model.entities.enemies.OmenoctModel;
import org.windowkillproject.view.abilities.BulletView;

import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Objects;


import static org.windowkillproject.application.Application.getGameFrame;
import static org.windowkillproject.application.Config.*;
import static org.windowkillproject.application.SoundPlayer.playBulletSound;
import static org.windowkillproject.controller.Controller.createAbilityView;
import static org.windowkillproject.controller.GameController.impact;
import static org.windowkillproject.controller.Utils.unitVector;
import static org.windowkillproject.controller.Utils.weighedVector;
import static org.windowkillproject.model.entities.EntityModel.entityModels;

public class BulletModel extends AbilityModel implements Projectable{
    private static int attackHp = BULLET_ATTACK_HP;
    private final Point2D mousePoint;
    private GamePanel localPanel;

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
@Override
    public void shoot() {
        setShoot(true);
        move();
        move();
        move();
    }
@Override
    public void move() {
        if (isShoot()) {
            Point2D delta = unitVector(getMousePoint(), this.getAnchor());
            delta = weighedVector(delta, Config.BULLET_SPEED);
            setX((int) (getX() + delta.getX()));
            setY((int) (getY() + delta.getY()));

            //enemies getting shot
            isEnemyShot();

            //frame getting shot
            isFrameShot();
        }
    }

    private void isFrameShot() {
        boolean shotRight = getX() > getGameFrame().getMainPanelWidth();
        boolean shotLeft = getX() < 0;
        boolean shotUp = getY() < 0 ;
        boolean shotDown = getY() > getGameFrame().getMainPanelHeight();

        if ( shotLeft|| shotRight ||shotUp || shotDown) {
            if (shotLeft) hit(LEFT_CODE);
            if (shotRight) hit(RIGHT_CODE);
            if (shotUp) hit(UP_CODE);
            if (shotDown) hit(DOWN_CODE);
            explode();
        }
    }

    private void hit(int code) {
        getGameFrame().stretch(code);
        hitWall(code);
    }

    private void hitWall(int code){
    for (int i = 0; i < entityModels.size(); i++){
        EntityModel entityModel = entityModels.get(i);
        if (entityModel instanceof OmenoctModel){
            var omenoctModel = (OmenoctModel) entityModel;
            omenoctModel.hitWall(code);
        }
    }
}
    private void isEnemyShot() {
        for (EntityModel entityModel : entityModels) {
            if (entityModel instanceof EnemyModel) {
                EnemyModel enemyModel = (EnemyModel) entityModel;
                //not hitting vertices
                boolean notHitVs = true;
                for (VertexModel vertexModel : enemyModel.getVertices()) {
                    if (Objects.equals(vertexModel.anchor, new Point2D.Double(getX(), getY()))) {
                        explode();
                        notHitVs = false;
                        break;
                    }
                }
                //hitting enemy
                if (notHitVs && ((EnemyModel) entityModel).getPolygon()!=null) {
                    Area enemyA = new Area(enemyModel.getPolygon());
                    if (enemyA.contains(this.getX(), this.getY())) {
                        enemyModel.gotShoot();
                        explode();
                        break;
                    }
                }
            }
        }
    }

    public BulletModel(int x, int y, Point2D mousePoint) {
        super(x, y);
        anchor = new Point2D.Double(x + EPSILON_RADIUS / 2, y + EPSILON_RADIUS * 1.5);
        isShoot = false;
        bulletModels.add(this);
        this.mousePoint = mousePoint;
//        localPanel = EpsilonModel.getINSTANCE().getLocalPanel();
        createAbilityView(BulletView.class, id, x, y);
    }

    private boolean isShoot;


    public boolean isShoot() {
        return isShoot;
    }

    private void explode() {
        impact(this);
        bulletModels.remove(this);
        playBulletSound();
        destroy();
    }

    public void setShoot(boolean shoot) {
        isShoot = shoot;
    }
}
