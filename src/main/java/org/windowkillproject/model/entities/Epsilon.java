package org.windowkillproject.model.entities;

import org.windowkillproject.model.abilities.Bullet;
import org.windowkillproject.model.abilities.Vertex;
import org.windowkillproject.view.ImgData;

import java.awt.*;
import java.util.ArrayList;

public class Epsilon extends Entity {
  //  private final Shooter shooter;
    private ArrayList<Bullet> bullets;

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public void setBullets(ArrayList<Bullet> bullets) {
        this.bullets = bullets;
    }

    public Epsilon(int x, int y){
        super(x , y);
        setRadius(14);
        setHp(100);
        setAttackHp(10);
        setImg(ImgData.getData().getEpsilon());
        getVertices().add(new Vertex(getXO(), getYO()- getRadius(), this));
        //shooter = new Shooter(170, 50 , this);
        bullets = new ArrayList<>();
    }

    @Override
    public void paint(Graphics g){
        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(getImg(), getX()+2, getY()+2, getWidth(), getHeight(), null);
//        g2D.setColor(Color.white);
//        g2D.fillOval(getXO() - getRadius(), getYO() - getRadius(), getWidth(), getHeight());
//        g2D.setColor(Color.black);
//        g2D.fillOval(getXO() - getRadius() +2, getYO() - getRadius() +2, getWidth()-4, getHeight()-4);
        for (Vertex vertex : getVertices()){
            vertex.paint(g);
        }
        for (Bullet bullet : getBullets()){
            bullet.paint(g);
        }
    }
}
