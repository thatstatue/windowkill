package org.windowkillproject.model.entities;

import org.windowkillproject.application.Config;
import org.windowkillproject.model.abilities.CollectableModel;
import org.windowkillproject.model.abilities.Vertex;

import java.awt.geom.Point2D;

import static org.windowkillproject.application.Application.gameFrame;
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

    private EpsilonModel(int x, int y){
        super(x , y);
        setRadius(EPSILON_RADIUS);
        setHp(1000);
//        setAttackHp(10);
       // getVertices().add(new Vertex(getXO(), getYO()- getRadius(), this));
    }

    @Override
    public Point2D getRoutePoint() {
        Point2D point2D = new Point2D.Double(0,0);
        if (!((isLeftPressed && isRightPressed) || (isDownPressed && isUpPressed))) {
            if (isUpPressed)
                point2D.setLocation(point2D.getX(), -2*EPSILON_SPEED);
            else if (isDownPressed)
                point2D.setLocation(point2D.getX(), 2*EPSILON_SPEED);
            if (isLeftPressed)
                point2D.setLocation(-2*EPSILON_SPEED, point2D.getY());
            else if (isRightPressed)
                point2D.setLocation(2*EPSILON_SPEED, point2D.getY());
        }
        return point2D;
    }

    @Override
    public void gotHit(int attackHp){
        super.gotHit(attackHp);
        gameFrame.setHpAmount(getHp());
    }

    public void collected(int rewardXp){
        setXp(getXp()+rewardXp);
        gameFrame.setXpAmount(getXp());
        System.out.println();
    }
}
