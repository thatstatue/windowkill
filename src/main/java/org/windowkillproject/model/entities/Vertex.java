package org.windowkillproject.model.entities;

import org.windowkillproject.view.ImgData;

import java.awt.*;

public class Vertex extends Ability {
    private Entity parent;

    public Vertex(int x, int y, Entity parent) {
        super(x, y);
        this.parent = parent;
        //setImg(ImgData.getData().getVertex());
    }
    void rotate(){
        double degree = Math.atan2( this.getY() - parent.getyO(),this.getX() -parent.getxO());
        degree += 0.084;
        setY(parent.getyO() + (int) (parent.getRadius() * Math.sin(degree)));
        setX(parent.getxO() + (int)(parent.getRadius() * Math.cos(degree)));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2D = (Graphics2D) g;
        //g2D.setColor(Color.red);
        g2D.fillOval(getX(), getY(), 5,5);
    }
}
