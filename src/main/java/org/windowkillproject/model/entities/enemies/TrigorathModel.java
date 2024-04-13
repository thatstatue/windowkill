package org.windowkillproject.model.entities.enemies;

import org.windowkillproject.model.abilities.Vertex;

import java.awt.*;

public class TrigorathModel extends EnemyModel {
    private final double RAD3_ON_2 = 0.866;
    public TrigorathModel(int x, int y) {
        super(x, y);
        setRadius(20);
        setHp(15);
        setAttackHp(10);
        setRewardCount(2);
        setRewardXps(5);
        initVertices();
        int[] xPoints = new int[3];
        int[] yPoints = new int[3];
        for (int i = 0 ; i < 3; i++){
            xPoints[i] = getVertices().get(i).getX();
            yPoints[i] = getVertices().get(i).getY();
        }
        setPolygon(new Polygon(xPoints , yPoints, 3));
    }

    private void initVertices(){
        getVertices().add(new Vertex(getXO(), getYO()- getRadius(), this));
        getVertices().add(new Vertex(getXO()- (int)(getRadius()*RAD3_ON_2), getYO()+ getRadius()/2, this));
        getVertices().add(new Vertex(getXO()+ (int)(getRadius()*RAD3_ON_2), getYO()+ getRadius()/2, this));
    }
}
