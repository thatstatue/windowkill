package org.windowkillproject.server.model.entities.enemies.finalboss;

import org.windowkillproject.server.model.globe.GlobeModel;
import org.windowkillproject.server.model.panelmodels.InternalPanelModel;

import org.windowkillproject.server.model.panelmodels.PanelStatus;
import org.windowkillproject.server.model.abilities.ProjectileModel;
import org.windowkillproject.server.model.entities.Circular;
import org.windowkillproject.server.model.entities.enemies.EnemyModel;
import org.windowkillproject.server.model.entities.enemies.attackstypes.AoEAttacker;
import org.windowkillproject.server.model.entities.enemies.attackstypes.NonRotatable;
import org.windowkillproject.server.model.entities.enemies.attackstypes.ProjectileOperator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.concurrent.atomic.AtomicInteger;

import static org.windowkillproject.Constants.*;
import static org.windowkillproject.server.Config.*;

import static org.windowkillproject.controller.GameManager.random;
import static org.windowkillproject.controller.Utils.globalRoutePoint;

public class SmileyHeadModel extends EnemyModel implements Circular, NonRotatable, AoEAttacker, ProjectileOperator {

    public boolean isAppearing() {
        return appearing;
    }

    public boolean isDefeated() {
        return defeated;
    }

    private boolean created, defeated, appearing;

    public void setPunching(boolean punching) {
        this.punching = punching;
    }

    private RightHandModel rightHandModel;
    private LeftHandModel leftHandModel;
    private PunchFistModel punchFistModel;
    public void initHands() {
        if (!created && !defeated) {
            leftHandModel = new LeftHandModel(globeModel,getX() - HAND_RADIUS * 3, getY() + MIN_HEAD_DISTANCE * 2);
            rightHandModel = new RightHandModel(globeModel, getX() + HAND_RADIUS * 3, getY() + MIN_HEAD_DISTANCE * 2);
            created = true ;
        }

    }

    public SmileyHeadModel(GlobeModel globeModel) {
        super(globeModel, null, CENTER_X, HAND_RADIUS, (int) (HAND_RADIUS * 1.5), 300, 0, 0, 0);
        setLocalPanelModel(new InternalPanelModel(globeModel, new Rectangle(x, y, HAND_RADIUS * 3, HAND_RADIUS * 3),
                PanelStatus.shrinkable, true
        ));
        getLocalPanelModel().setForSmiley(true);
        globeModel.getGlobeController().createEntityView(getId(), getX(), getY(), getWidth(), getHeight());
    }

    private boolean vulnerable;

    @Override
    public void gotHit(int attackHp) {
        if (vulnerable) {
            super.gotHit(attackHp);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        rightHandModel.destroy();
        leftHandModel.destroy();
        targetEpsilon.setXp(targetEpsilon.getXp() + 250);
        defeated = true;
    }

    public void setSlapping(boolean slapping) {
        this.slapping = slapping;
    }

    @Override
    public void gotShot(int attackHP) {
        if (vulnerable) {
            super.gotShot(attackHP);
        }
    }

    public boolean isVulnerable() {
        return vulnerable;
    }

    public void setVulnerable(boolean vulnerable) {
        this.vulnerable = vulnerable;
    }

    public void appear() {
        appearing = true;
        int times = (CENTER_Y / 3) / HAND_SPEED;
        AtomicInteger count = new AtomicInteger();
        Timer timer = new Timer(FPS , null);
        timer.addActionListener(e -> {
            if (count.get() < times) {
                if (getYO() +getRadius()*2 < globeModel.getMainPanelModel().getY())
                    move(0, HAND_SPEED);
                rightHandModel.move(0, HAND_SPEED);
                leftHandModel.move(0, HAND_SPEED);
                count.getAndIncrement();
            } else {
                appearing = false;
                timer.stop();
            }
        });
        timer.start();
    }



    @Override
    public void route() {
        if (!appearing) {
            if (punchFistModel.isOn()) {
                if (globeModel.getElapsedTime().getTotalSeconds() - lastVomit > ATTACK_TIMEOUT) {
                    vomiting = false;
                    vulnerable = false;
                }
                attack();
            } else {
                squeezeProjectile();
            }


            if (getY() < 0 || getX() < 0 || getX() / 2 > CENTER_X * getRadius()) {
                if (getX() < 0) {
                    move(HAND_SPEED * 2, 0);
                }
                if (getX() / 2 > CENTER_X * getRadius()) {
                    move(-HAND_SPEED * 2, 0);
                }
                if (getY() < 0) {
                    move(0, 2 * HAND_SPEED);
                }
            }
            if (!vomiting) {
                moveTowardsEpsilon();
            }
            if (getHp() < 200 && !punchFistModel.isOn()) {
                punchFistModel = new PunchFistModel(globeModel, getX(), getY() + 3 * getHeight());
            }
            getLocalPanelModel().setX((int) (getXO() - getRadius() * 1.5));
            getLocalPanelModel().setY((int) (getYO() - getRadius() * 1.5));

        }
    }

    private void moveTowardsEpsilon() {
        if (targetEpsilon.getAnchor().distance(getAnchor()) > 2 * (MIN_HEAD_DISTANCE + 2 * getRadius())) {
            move((int) getRoutePoint().getX(), (int) getRoutePoint().getY());
        }
    }

    private int squeezeChangeMoment, lastVomit;
    private boolean squeezing , vomiting,  punching, slapping, firing ;

    private void squeezeProjectile() {
        long timePassed = globeModel.getElapsedTime().getTotalSeconds() - squeezeChangeMoment;

        if (squeezing) {
            if (timePassed > SQUEEZE_TIMEOUT / 2) {
                stopSqueezing();
            }else {
                squeezeHands();
            }

        }else if (isHandFree()){
           // var epsilonPanel = targetEpsilon.getLocalPanelModel(); //todo
            /*if (epsilonPanel== null)*/var epsilonPanel = globeModel.getMainPanelModel();
            var rect = epsilonPanel.getBounds();
            Point2D anchor = new Point2D.Double(rect.x+rect.width/2D, rect.y);
            boolean smileyNear =
                    getAnchor().distance(anchor) < WYRM_DISTANCE
                            && getY() < anchor.getY();
            boolean headUpper = getY() + getRadius()< Math.min(rightHandModel.getY(), leftHandModel.getY());

            if (timePassed > SQUEEZE_TIMEOUT && smileyNear && headUpper) {
                startSqueezing();
            } else {
                goRoundEpsilon();
                rightHandModel.projectile();
                leftHandModel.projectile();
//                if(!headUpper){
//                    move(0 , -2*HAND_SPEED);
//                }
            }
        }
    }

    private void stopSqueezing() {
        squeezing = false;
        leftHandModel.getLocalPanelModel().setFlexible(true);
        rightHandModel.getLocalPanelModel().setFlexible(true);
        setVulnerable(false);
    }

    private void startSqueezing() {
        setVulnerable(true);
        squeezeChangeMoment = globeModel.getElapsedTime().getTotalSeconds();
        squeezing = true;
        squeezeHands();
    }

    private void squeezeHands() {
        rightHandModel.squeeze();
        leftHandModel.squeeze();
    }

    private double rotationSpeed = UNIT_DEGREE / 2;

    private void goRoundEpsilon() {
        double degree = Math.atan2(this.getYO() - targetEpsilon.getYO(), this.getXO() - targetEpsilon.getXO());
        degree += rotationSpeed;
        int finalX = targetEpsilon.getXO() + (int) (WYRM_DISTANCE * 1.2 * Math.cos(degree)) - getRadius();
        int finalY = targetEpsilon.getYO() + (int) (WYRM_DISTANCE * 1.2 * Math.sin(degree)) - getRadius();
        move(finalX - getX(), finalY - getY());
    }
    private void attack(){
        switch (random.nextInt(6)){
            case 0 -> squeezeProjectile();
            case 1 -> quake();
            case 2-> vomit();
            case 3 -> powerPunch();
            case 4 -> slap();
            case 5 -> rapidFire();
        }
    }
    private void quake(){
        //
    }
    private void vomit(){
        if (!vomiting) {
            if (targetEpsilon.getAnchor().distance(getAnchor()) < 2 *
                    (MIN_HEAD_DISTANCE + 2 * getRadius())) {
                vomiting = true;
                lastVomit = globeModel.getElapsedTime().getTotalSeconds();
                vulnerable = true;
                for (int i = 0; i < 50; i++) {
                    AOE();
                }
            }else moveTowardsEpsilon();
        }
    }
    private void powerPunch(){
        if(isHandFree()){
            vulnerable = true;
            punching = true;
            punchFistModel.tightenEpsilonPanel();
        }
    }
    private void rapidFire(){
        if (!firing) {
            firing = true;
            vulnerable = true;
            AtomicInteger integer = new AtomicInteger();
            Timer timer = new Timer(1000, null);
            ActionListener actionListener = e -> {
                integer.getAndIncrement();
                if (integer.get() < 30) {
                    shoot();
                } else if (integer.get()> 30 + 4){
                    vulnerable = false;
                    firing = false;
                    timer.stop();
                }
            };
            timer.addActionListener(actionListener);
            timer.start();
        }
    }
    private void slap(){
        if(isHandFree()){
            vulnerable = true;
            slapping = true;
            punchFistModel.slap();
        }
    }
    private boolean isHandFree(){
        return !(squeezing || punching || slapping);
    }

    @Override
    public Point2D getRoutePoint() {
        return globalRoutePoint(this.getAnchor(),
                targetEpsilon.getAnchor(), MIN_ENEMY_SPEED + 1);
    }

    @Override
    protected void initVertices() {

    }


    @Override
    public int getAoEAttackHP() {
        return 2;
    }

    @Override
    public Point2D getMoment() {
        int randX = getXO() + random.nextInt(CENTER_X) - CENTER_X / 2;
        int randY = getYO() + random.nextInt(CENTER_X) - CENTER_X / 2;
        return new Point2D.Double(randX, randY);
    }

    @Override
    public int getInitSecond() {
        return globeModel.getElapsedTime().getTotalSeconds();
    }

    @Override
    public void shoot() {
        new ProjectileModel(getLocalPanelModel(), this, 5, false, null, Color.yellow, Color.yellow).shoot();
    }
}
