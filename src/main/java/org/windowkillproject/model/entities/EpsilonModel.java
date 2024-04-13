package org.windowkillproject.model.entities;

import org.windowkillproject.model.abilities.CollectableModel;
import org.windowkillproject.model.abilities.Vertex;

import static org.windowkillproject.application.Application.gameFrame;
import static org.windowkillproject.application.Config.*;

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
        setHp(100);
//        setAttackHp(10);
       // getVertices().add(new Vertex(getXO(), getYO()- getRadius(), this));
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
