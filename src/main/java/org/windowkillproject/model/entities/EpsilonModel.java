package org.windowkillproject.model.entities;

import org.windowkillproject.model.abilities.BulletModel;
import org.windowkillproject.model.abilities.Vertex;

import java.util.ArrayList;

import static org.windowkillproject.application.Config.*;

public class EpsilonModel extends EntityModel {
    private static EpsilonModel INSTANCE;
    public static EpsilonModel getINSTANCE(){
        if (INSTANCE == null) INSTANCE = new EpsilonModel(GAME_WIDTH / 2, GAME_HEIGHT / 2);
        return INSTANCE;
    }

    private EpsilonModel(int x, int y){
        super(x , y);
        setRadius(EPSILON_RADIUS);
        setHp(100);
        setAttackHp(10);
        getVertices().add(new Vertex(getXO(), getYO()- getRadius(), this));
    }
}
