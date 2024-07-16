package org.windowkillproject.model.entities.enemies.finalboss;

import org.windowkillproject.application.Config;
import org.windowkillproject.application.panels.game.InternalGamePanel;
import org.windowkillproject.application.panels.game.PanelStatus;
import org.windowkillproject.model.entities.Circular;
import org.windowkillproject.model.entities.EpsilonModel;
import org.windowkillproject.model.entities.enemies.EnemyModel;
import org.windowkillproject.model.entities.enemies.attackstypes.AoEAttacker;
import org.windowkillproject.model.entities.enemies.attackstypes.NonRotatable;

import javax.swing.*;
import java.awt.geom.Point2D;
import java.util.concurrent.atomic.AtomicInteger;

import static org.windowkillproject.application.Config.*;
import static org.windowkillproject.application.panels.game.GamePanel.gamePanelsBounds;
import static org.windowkillproject.controller.Controller.createEntityView;
import static org.windowkillproject.controller.ElapsedTime.getTotalSeconds;
import static org.windowkillproject.controller.GameController.random;
import static org.windowkillproject.controller.Utils.globalRoutePoint;

public class SmileyHeadModel extends EnemyModel implements Circular, NonRotatable, AoEAttacker {
    private static SmileyHeadModel instance;

    public static boolean isDefeated() {
        return defeated;
    }

    private static boolean defeated;
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

    @Override
    public void gotShoot() {
        if (vulnerable) {
            super.gotShoot();
        }
    }

    public boolean isVulnerable() {
        return vulnerable;
    }

    public void setVulnerable(boolean vulnerable) {
        this.vulnerable = vulnerable;
    }

    public void appear() {
//        int times = (gamePanelsBounds.get(getGameFrame()).y -
//                getInstance().getY() - getInstance().getHeight()) / HAND_SPEED;
//        times = Math.min(times, (CENTER_Y / 2) / HAND_SPEED);

        Timer moveDown = getTimer((CENTER_Y / 3) / HAND_SPEED);
        moveDown.start();
    }

    private static Timer getTimer(int times) {

        AtomicInteger count = new AtomicInteger();
        Timer timer = new Timer(Config.FPS / 2, null);
        timer.addActionListener(e -> {
            if (count.get() < times) {
                instance.move(0, HAND_SPEED);
                rightHandModel.move(0, HAND_SPEED);
                leftHandModel.move(0, HAND_SPEED);
                count.getAndIncrement();
            } else {
                timer.stop();
            }
        });
        return timer;
    }

    @Override
    public void route() {
        if (PunchFistModel.isOn()) {

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
        if (EpsilonModel.getINSTANCE().getAnchor().distance(getAnchor()) > 2 * (MIN_HEAD_DISTANCE + 2 * getRadius())) {
            move((int) getRoutePoint().getX(), (int) getRoutePoint().getY());
        }
        if (getHp() < 200 && !PunchFistModel.isOn()) {
            punchFistModel = new PunchFistModel(getX(), getY() + 3 * getHeight());
        }
        getLocalPanel().setLocation((int) (getXO() - getRadius() * 1.5), (int) (getYO() - getRadius() * 1.5));


    }

    private static int squeezeChangeMoment;
    private static boolean squeezing = false;

    private static void squeezeProjectile() {
        System.out.println("step 1");
        long timePassed = getTotalSeconds() - squeezeChangeMoment;

        if (!squeezing) {
            var epsilonAnchor = EpsilonModel.getINSTANCE().getAnchor();
            boolean smileyNear =
                    instance.getAnchor().distance(epsilonAnchor) < WYRM_DISTANCE
                            && instance.getY() < epsilonAnchor.getY();
            boolean headUpper = instance.getY()< Math.min(rightHandModel.getY(), leftHandModel.getY());
            if (timePassed > SQUEEZE_TIMEOUT && smileyNear && headUpper) {
                startSqueezing();
            } else {
                instance.goRoundEpsilon();
                rightHandModel.projectile();
                leftHandModel.projectile();
            }
        } else {
            if (timePassed > SQUEEZE_TIMEOUT / 2) {
                stopSqueezing();
            }else {
                squeezeHands();
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
//                instance.squeeze();
        squeezeHands();
    }

    private static void squeezeHands() {
        rightHandModel.squeeze();
        leftHandModel.squeeze();
    }

    private double rotationSpeed = UNIT_DEGREE / 4;

    private void goRoundEpsilon() {
        var epsilonModel = EpsilonModel.getINSTANCE();
        double degree = Math.atan2(this.getYO() - epsilonModel.getYO(), this.getXO() - epsilonModel.getXO());
        degree += rotationSpeed;
        int finalX = epsilonModel.getXO() + (int) (WYRM_DISTANCE * 0.8 * Math.cos(degree)) - getRadius();
        int finalY = epsilonModel.getYO() + (int) (WYRM_DISTANCE * 0.8 * Math.sin(degree)) - getRadius();
        move(finalX - getX(), finalY - getY());
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
        int randY = getYO() + random.nextInt(CENTER_Y) - CENTER_Y / 2;
        return new Point2D.Double(randX, randY);
    }
}
