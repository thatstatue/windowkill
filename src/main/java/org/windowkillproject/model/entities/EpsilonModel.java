package org.windowkillproject.model.entities;

import org.windowkillproject.application.Config;
import org.windowkillproject.model.abilities.CollectableModel;
import org.windowkillproject.model.abilities.Vertex;

import java.awt.geom.Point2D;

import static org.windowkillproject.application.Application.gameFrame;
import static org.windowkillproject.application.Application.initScoreFrame;
import static org.windowkillproject.application.Config.*;
import static org.windowkillproject.application.listeners.EpsilonKeyListener.*;

public class EpsilonModel extends EntityModel {
    private static EpsilonModel INSTANCE;
    public static EpsilonModel getINSTANCE(){
        if (INSTANCE == null) INSTANCE = new EpsilonModel(GAME_WIDTH / 2, GAME_HEIGHT / 2);
        return INSTANCE;
    }
    private int xp;

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }
    public void route(){
        EpsilonModel eM = getINSTANCE();

        int endX = eM.getWidth() + eM.getX() + eM.getRadius();
        int endY = eM.getHeight() + eM.getY() + 3* eM.getRadius();
        if (!((isLeftPressed && isRightPressed) || (isDownPressed && isUpPressed))) {
            if (isUpPressed && eM.getY() - Config.EPSILON_SPEED >= 0)
                eM.move(0,-Config.EPSILON_SPEED);
            else if (isDownPressed && endY + Config.EPSILON_SPEED <= gameFrame.getHeight())
                eM.move(0,Config.EPSILON_SPEED);
            if (isLeftPressed && eM.getX() - Config.EPSILON_SPEED >= 0)
                eM.move(-Config.EPSILON_SPEED,0);
            else if (isRightPressed && endX + Config.EPSILON_SPEED <= gameFrame.getWidth())
                eM.move(Config.EPSILON_SPEED,0);

        }
    }

    private EpsilonModel(int x, int y){
        super(x , y);
        setRadius(EPSILON_RADIUS);
        setHp(100);
        setAttackHp(10);
        getVertices().add(new Vertex(getXO(), getYO()- getRadius(), this));
    }

    @Override
    public Point2D getRoutePoint() {
        Point2D point2D = new Point2D.Double(0,0);
        if (!((isLeftPressed && isRightPressed) || (isDownPressed && isUpPressed))) {
            if (isUpPressed)
                point2D.setLocation(point2D.getX(), -2*EPSILON_SPEED);
            if (isDownPressed)
                point2D.setLocation(point2D.getX(), 2*EPSILON_SPEED);
            if (isLeftPressed)
                point2D.setLocation(-2*EPSILON_SPEED, point2D.getY());
            if (isRightPressed)
                point2D.setLocation(2*EPSILON_SPEED, point2D.getY());
        }
        return point2D;
    }

    @Override
    public void gotHit(int attackHp){
        super.gotHit(attackHp);
        gameFrame.setHpAmount(getHp());

    }
    @Override
    public void destroy(){
        super.destroy();
        initScoreFrame();
    }

    public void collected(int rewardXp){
        setXp(getXp()+rewardXp);
        gameFrame.setXpAmount(getXp());
        System.out.println();
    }
}
