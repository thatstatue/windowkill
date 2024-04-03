package org.windowkillproject.model.entities;

import org.windowkillproject.model.abilities.Vertex;
import org.windowkillproject.view.ImgData;

import java.awt.*;

public class Epsilon extends Entity {


    public Epsilon(int x, int y){
        super(x , y);
        setRadius(14);
        setHp(100);
        setAttackHp(10);
        setImg(ImgData.getData().getEpsilon());
        getVertices().add(new Vertex(getXO(), getYO()- getRadius(), this));
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
    }
}
