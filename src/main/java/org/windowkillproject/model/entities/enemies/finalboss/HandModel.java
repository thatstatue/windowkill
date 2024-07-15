package org.windowkillproject.model.entities.enemies.finalboss;

import org.windowkillproject.application.Config;
import org.windowkillproject.application.panels.game.InternalGamePanel;
import org.windowkillproject.application.panels.game.PanelStatus;
import org.windowkillproject.model.abilities.ProjectileModel;
import org.windowkillproject.model.abilities.VertexModel;
import org.windowkillproject.model.entities.EpsilonModel;
import org.windowkillproject.model.entities.enemies.EnemyModel;
import org.windowkillproject.model.entities.enemies.attackstypes.NonRotatable;
import org.windowkillproject.model.entities.enemies.attackstypes.ProjectileOperator;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static org.windowkillproject.application.Application.getGameFrame;
import static org.windowkillproject.application.Config.*;
import static org.windowkillproject.application.panels.game.GamePanel.gamePanels;
import static org.windowkillproject.application.panels.game.GamePanel.gamePanelsBounds;
import static org.windowkillproject.controller.Controller.createEntityView;
import static org.windowkillproject.controller.Controller.deleteGamePanel;
import static org.windowkillproject.controller.ElapsedTime.getTotalSeconds;
import static org.windowkillproject.controller.Utils.globalRoutePoint;

public class HandModel extends EnemyModel implements ProjectileOperator , NonRotatable {
    private static ArrayList<HandModel> hands = new ArrayList<>();
    protected HandModel(int x, int y) {
        super(null, x, y, Config.HAND_RADIUS, 100, 10, 10, 10);
        setLocalPanel(new InternalGamePanel(x, y, HAND_RADIUS*3, HAND_RADIUS*3,
                PanelStatus.isometric , true
        ));

        initVertices();
        initPolygon();
        createEntityView(getId(), getX(),getY(),getWidth(),getHeight());
        hands.add(this);

    }
    private void squeeze(){
        if (hands.size()==2){
            setVulnerable(false);
            SmileyHeadModel.getInstance().setVulnerable(true);
            long timePassed = getTotalSeconds()-lastSqueeze;
            if(timePassed > SQUEEZE_TIMEOUT) {
                lastSqueeze = getTotalSeconds();
                getLocalPanel().setFlexible(false);
                Timer squeezeTimer = getSqueezeTimer();
                squeezeTimer.start();
            }else if(timePassed > SQUEEZE_TIMEOUT/2){
                getLocalPanel().setFlexible(true);
                SmileyHeadModel.getInstance().setVulnerable(false);
            }
        }
    }

    private long lastShot, lastSqueeze;
    private Timer getSqueezeTimer(){
        int panelWidth = gamePanelsBounds.get(getLocalPanel()).width;
        int panelHeight = gamePanelsBounds.get(getLocalPanel()).height;

        int goX = getGameFrame().getMainPanelX() - panelWidth;
        if (this instanceof RightHandModel){
            goX += getGameFrame().getMainPanelWidth() + panelWidth;
        }
        int goY = getGameFrame().getMainPanelY() + (getGameFrame().getMainPanelHeight()-panelHeight)/2;

        return moveTowards(goX, goY);
    }

    private Timer moveTowards(int goX, int goY) {
        int handXSpeed = (goX - getX())/5;
        int handYSpeed = (goY - getY())/5;

        AtomicInteger count = new AtomicInteger();
        Timer timer = new Timer(Config.FPS / 5, null);
        timer.addActionListener(e -> {
            if (count.get() < 5) {
                move(handXSpeed, handYSpeed);
                count.getAndIncrement();
            } else {
                timer.stop();
            }
        });
        return timer;
    }

    @Override
    public void route() {
        if (PunchFistModel.isOn()){

        }else {
            var smileyAnchor =SmileyHeadModel.getInstance().getAnchor();
            var epsilonAnchor = EpsilonModel.getINSTANCE().getAnchor();
            if (smileyAnchor.distance(epsilonAnchor) < WYRM_DISTANCE && smileyAnchor.getY()< epsilonAnchor.getY()){
                squeeze();
            }else {
                projectile();
            }
        }

        getLocalPanel().setLocation((int) (getXO()-getRadius()*1.5), (int) (getYO()-getRadius()*1.5));
    }

    private void projectile() {
        if (!hands.isEmpty()) {
            setVulnerable(true);
            shoot();
            goRoundEpsilon();
        }
    }

    @Override
    public void shoot() {
        if (getTotalSeconds() - lastShot > PROJECTILE_TIMEOUT) {
            new ProjectileModel(getLocalPanel(),this, 4, true, true,
                    Color.yellow, Color.gray).shoot();
            lastShot = getTotalSeconds();
        }
    }
    private double rotationSpeed = UNIT_DEGREE/4;


    public void setMinusRotationSpeed() {
        this.rotationSpeed = -rotationSpeed;
    }

    private void goRoundEpsilon() {
        var epsilonModel = EpsilonModel.getINSTANCE();
//        double rotationRadius = getAnchor().distance(epsilonModel.getAnchor());
        double degree = Math.atan2( this.getYO() - epsilonModel.getYO(),this.getXO() - epsilonModel.getXO());
        degree += rotationSpeed;
        int finalX =epsilonModel.getXO() + (int)(WYRM_DISTANCE*1.2 * Math.cos(degree)) - getRadius();
        int finalY = epsilonModel.getYO() + (int) (WYRM_DISTANCE*1.2 * Math.sin(degree)) - getRadius();
        move(finalX - getX(), finalY - getY());
    }

    @Override
    public Point2D getRoutePoint() {
        return globalRoutePoint(this.getAnchor(),
                EpsilonModel.getINSTANCE().getAnchor(), MIN_ENEMY_SPEED+0.5);
    }
    @Override
    public void destroy() {
        super.destroy();
        hands.remove(this);
        deleteGamePanel(getLocalPanel());
    }

    private boolean vulnerable;
    @Override
    public void gotHit(int attackHp) {
        if(vulnerable) {
            super.gotHit(attackHp);
        }
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


    @Override
    protected void initVertices() {
        int halfSideLength = (int) (getRadius() / Math.sqrt(2)+14);


        getVertices().add(new VertexModel(getXO() - halfSideLength, getYO() - halfSideLength, this));
        getVertices().add(new VertexModel(getXO() + halfSideLength, getYO() - halfSideLength, this));
        getVertices().add(new VertexModel(getXO() + halfSideLength, getYO() + halfSideLength, this));
        getVertices().add(new VertexModel(getXO() - halfSideLength, getYO() + halfSideLength, this));

    }

}
