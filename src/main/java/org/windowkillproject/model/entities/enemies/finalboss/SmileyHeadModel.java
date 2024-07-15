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
    public static SmileyHeadModel getInstance(){
        if (instance == null && !defeated) {
            instance = new SmileyHeadModel(CENTER_X,  HAND_RADIUS);
            leftHandModel = new LeftHandModel(instance.getX()-HAND_RADIUS*3, instance.getY()+MIN_HEAD_DISTANCE*2);
            rightHandModel = new RightHandModel(instance.getX()+HAND_RADIUS*3, instance.getY()+MIN_HEAD_DISTANCE*2);
        }
        return instance;
    }

    private SmileyHeadModel(int x, int y) {
        super(null, x, y, (int) (HAND_RADIUS*1.5), 300, 0, 0, 0);
        setLocalPanel(new InternalGamePanel(x, y, HAND_RADIUS*3, HAND_RADIUS*3,
                PanelStatus.shrinkable , true
        ));
        createEntityView(getId(), getX(),getY(),getWidth(),getHeight());
    }
    private boolean vulnerable;
    @Override
    public void gotHit(int attackHp) {
        if(vulnerable) {
            super.gotHit(attackHp);
        }
    }

    @Override
    public void destroy(){
        super.destroy();
        rightHandModel.destroy();
        leftHandModel.destroy();
        EpsilonModel.getINSTANCE().setXp(EpsilonModel.getINSTANCE().getXp()+250);
        defeated = true;
    }
    @Override
    public void gotShoot() {
        if(vulnerable) {
            super.gotShoot();
        }
    }

    public boolean isVulnerable() {
        return vulnerable;
    }

    public void setVulnerable(boolean vulnerable) {
        this.vulnerable = vulnerable;
    }
    public void appear(){
        int times = (gamePanelsBounds.get(EpsilonModel.getINSTANCE().getLocalPanel()).y -
                getInstance().getY() - getInstance().getHeight()) / HAND_SPEED;
        times = Math.min(times, (CENTER_Y/2)/HAND_SPEED);
        Timer moveDown = getTimer(times);
        moveDown.start();
    }
    private static Timer getTimer(int times){

        AtomicInteger count = new AtomicInteger();
        Timer timer = new Timer(Config.FPS / 5, null);
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
        if (getY()<0 || getX()<0 || getX()/2> CENTER_X*getRadius()) {
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
        if (getHp()< 200 && !PunchFistModel.isOn()){
            new PunchFistModel(getX(), getY()+ 3*getHeight());
        }
        getLocalPanel().setLocation((int) (getXO()-getRadius()*1.5), (int) (getYO()-getRadius()*1.5));

    }

    @Override
    public Point2D getRoutePoint() {
        return globalRoutePoint(this.getAnchor(),
                EpsilonModel.getINSTANCE().getAnchor());
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
        int randX = getXO() + random.nextInt(CENTER_X) - CENTER_X/2;
        int randY = getYO() + random.nextInt(CENTER_Y) - CENTER_Y/2;
        return new Point2D.Double(randX,randY);
    }
}
