package org.windowkillproject.model.entities;

import org.windowkillproject.model.abilities.Bullet;
import org.windowkillproject.model.abilities.Vertex;
import org.windowkillproject.view.ImgData;

import java.awt.*;
import java.util.ArrayList;

import static org.windowkillproject.application.Config.*;

public class EpsilonModel extends Entity {
    private static EpsilonModel INSTANCE;
    public static EpsilonModel getINSTANCE(){
        if (INSTANCE == null) INSTANCE = new EpsilonModel(GAME_WIDTH / 2, GAME_HEIGHT / 2);
        return INSTANCE;
    }
    private final ArrayList<Bullet> bullets;

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    private EpsilonModel(int x, int y){
        super(x , y);
        setRadius(EPSILON_RADIUS);
        setHp(100);
        setAttackHp(10);
        setImg(ImgData.getData().getEpsilon());
        getVertices().add(new Vertex(getXO(), getYO()- getRadius(), this));
        bullets = new ArrayList<>();
    }

    @Override
    public void paint(Graphics g){
        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(getImg(), getX()+2, getY()+2, getWidth(), getHeight(), null);
        for (Vertex vertex : getVertices()){
            vertex.paint(g);
        }
        for (Bullet bullet : getBullets()){
            bullet.paint(g);
        }
    }
}
