package org.windowkillproject.model.entities.enemies;

import org.windowkillproject.model.abilities.Vertex;
import org.windowkillproject.view.ImgData;

import java.awt.*;

public class TrigorathModel extends EnemyModel {
    private final double RAD3_ON_2 = 0.866;
    public TrigorathModel(int x, int y) {
        super(x, y);
        setRadius(20);
        setHp(15);
        setAttackHp(10);

        getVertices().add(new Vertex(getXO(), getYO()- getRadius(), this));
        getVertices().add(new Vertex(getXO()- (int)(getRadius()*RAD3_ON_2), getYO()+ getRadius()/2, this));
        getVertices().add(new Vertex(getXO()+ (int)(getRadius()*RAD3_ON_2), getYO()+ getRadius()/2, this));
        int[] xpoints = new int[3];
        int[] ypoints = new int[3];
        for (int i = 0 ; i < 3; i++){
            xpoints[i] = getVertices().get(i).getX();
            ypoints[i] = getVertices().get(i).getY();
        }
        setPolygon(new Polygon(xpoints , ypoints, 3));
    }


}
