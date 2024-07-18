package org.windowkillproject.model.entities.enemies.finalboss;

import org.windowkillproject.application.Config;
import org.windowkillproject.application.panels.game.InternalGamePanel;
import org.windowkillproject.application.panels.game.MainGamePanel;
import org.windowkillproject.application.panels.game.PanelStatus;
import org.windowkillproject.model.abilities.ProjectileModel;
import org.windowkillproject.model.entities.Circular;
import org.windowkillproject.model.entities.EpsilonModel;
import org.windowkillproject.model.entities.enemies.EnemyModel;
import org.windowkillproject.model.entities.enemies.attackstypes.AoEAttacker;
import org.windowkillproject.model.entities.enemies.attackstypes.NonRotatable;
import org.windowkillproject.model.entities.enemies.attackstypes.ProjectileOperator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.concurrent.atomic.AtomicInteger;

import static org.windowkillproject.application.Application.getGameFrame;
import static org.windowkillproject.application.Config.*;
import static org.windowkillproject.controller.Controller.createEntityView;
import static org.windowkillproject.controller.ElapsedTime.getTotalSeconds;
import static org.windowkillproject.controller.GameController.random;
import static org.windowkillproject.controller.Utils.globalRoutePoint;

public class SmileyHeadModel extends EnemyModel implements Circular, NonRotatable, AoEAttacker, ProjectileOperator {
    private static SmileyHeadModel instance;

    public static boolean isAppearing() {
        return appearing;
    }

    public static boolean isDefeated() {
        return defeated;
    }

    private static boolean defeated, appearing;

    public static void setPunching(boolean punching) {
        SmileyHeadModel.punching = punching;
    }

    private static RightHandModel rightHandModel;
    private static LeftHandModel leftHandModel;
    private static PunchFistModel punchFistModel;

    public static SmileyHeadModel getInstance() {
        if (instance == null && !defeated) {
            instance = new SmileyHeadModel(CENTER_X, HAND_RADIUS);
            leftHandModel = new LeftHandModel(instance.getX() - HAND_RADIUS * 3, instance.getY() + MIN_HEAD_DISTANCE * 2);
            rightHandModel = new RightHandModel(instance.getX() + HAND_RADIUS * 3, instance.getY() + MIN_HEAD_DISTANCE * 2);
        }
        return instance;
    }

    private SmileyHeadModel(int x, int y) {
        super(null, x, y, (int) (HAND_RADIUS * 1.5), 300, 0, 0, 0);
        setLocalPanel(new InternalGamePanel(x, y, HAND_RADIUS * 3, HAND_RADIUS * 3,
                PanelStatus.shrinkable, true
        ));
        getLocalPanel().setForSmiley(true);
        createEntityView(getId(), getX(), getY(), getWidth(), getHeight());
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
        EpsilonModel.getINSTANCE().setXp(EpsilonModel.getINSTANCE().getXp() + 250);
        defeated = true;
    }

    public static void setSlapping(boolean slapping) {
        SmileyHeadModel.slapping = slapping;
    }

    @Override
    public void gotShot() {
        if (vulnerable) {
            super.gotShot();
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
        Timer timer = new Timer(Config.FPS , null);
        timer.addActionListener(e -> {
            if (count.get() < times) {
                if (instance.getYO() +instance.getRadius()*2 < MainGamePanel.getInstance().getY())
                    instance.move(0, HAND_SPEED);
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
            if (PunchFistModel.isOn()) {
                if (getTotalSeconds() - lastVomit > ATTACK_TIMEOUT) {
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
            if (getHp() < 200 && !PunchFistModel.isOn()) {
                punchFistModel = new PunchFistModel(getX(), getY() + 3 * getHeight());
            }
            getLocalPanel().setLocation((int) (getXO() - getRadius() * 1.5), (int) (getYO() - getRadius() * 1.5));

        }
    }

    private void moveTowardsEpsilon() {
        if (EpsilonModel.getINSTANCE().getAnchor().distance(getAnchor()) > 2 * (MIN_HEAD_DISTANCE + 2 * getRadius())) {
            move((int) getRoutePoint().getX(), (int) getRoutePoint().getY());
        }
    }

    private static int squeezeChangeMoment, lastVomit;
    private static boolean squeezing , vomiting,  punching, slapping, firing ;

    private static void squeezeProjectile() {
        long timePassed = getTotalSeconds() - squeezeChangeMoment;

        if (squeezing) {
            if (timePassed > SQUEEZE_TIMEOUT / 2) {
                stopSqueezing();
            }else {
                squeezeHands();
            }

        }else if (instance.isHandFree()){
            var epsilonPanel = EpsilonModel.getINSTANCE().getLocalPanel();
            if (epsilonPanel== null) epsilonPanel = MainGamePanel.getInstance();
            var rect = epsilonPanel.getBounds();
            Point2D anchor = new Point2D.Double(rect.x+rect.width/2D, rect.y);
            boolean smileyNear =
                    instance.getAnchor().distance(anchor) < WYRM_DISTANCE
                            && instance.getY() < anchor.getY();
            boolean headUpper = instance.getY() + instance.getRadius()< Math.min(rightHandModel.getY(), leftHandModel.getY());

            if (timePassed > SQUEEZE_TIMEOUT && smileyNear && headUpper) {
                startSqueezing();
            } else {
                instance.goRoundEpsilon();
                rightHandModel.projectile();
                leftHandModel.projectile();
//                if(!headUpper){
//                    instance.move(0 , -2*HAND_SPEED);
//                }
            }
        }
    }

    private static void stopSqueezing() {
        squeezing = false;
        leftHandModel.getLocalPanel().setFlexible(true);
        rightHandModel.getLocalPanel().setFlexible(true);
        instance.setVulnerable(false);
    }

    private static void startSqueezing() {
        instance.setVulnerable(true);
        squeezeChangeMoment = getTotalSeconds();
        squeezing = true;
        squeezeHands();
    }

    private static void squeezeHands() {
        rightHandModel.squeeze();
        leftHandModel.squeeze();
    }

    private double rotationSpeed = UNIT_DEGREE / 2;

    private void goRoundEpsilon() {
        var epsilonModel = EpsilonModel.getINSTANCE();
        double degree = Math.atan2(this.getYO() - epsilonModel.getYO(), this.getXO() - epsilonModel.getXO());
        degree += rotationSpeed;
        int finalX = epsilonModel.getXO() + (int) (WYRM_DISTANCE * 1.2 * Math.cos(degree)) - getRadius();
        int finalY = epsilonModel.getYO() + (int) (WYRM_DISTANCE * 1.2 * Math.sin(degree)) - getRadius();
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
        //todo
    }
    private void vomit(){
        if (!vomiting) {
            if (EpsilonModel.getINSTANCE().getAnchor().distance(getAnchor()) < 2 *
                    (MIN_HEAD_DISTANCE + 2 * getRadius())) {
                vomiting = true;
                lastVomit = getTotalSeconds();
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
//        todo
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
                EpsilonModel.getINSTANCE().getAnchor(), MIN_ENEMY_SPEED + 1);
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
    public void shoot() {
        new ProjectileModel(getLocalPanel(), this, 5, false, false, Color.yellow, Color.yellow).shoot();
    }
}
