package org.windowkillproject.model.entities;

import org.windowkillproject.view.ImgData;

import java.awt.*;

public class Epsilon extends Entity {

    private final int RADIUS = 12;

    public int getXO() {
        return xO;
    }

    public void setXO(int xO) {
        this.xO = xO;
    }

    public int getYO() {
        return yO;
    }

    public void setYO(int yO) {
        this.yO = yO;
    }

    private int xO, yO;

    public Epsilon(int x, int y){
        super(x , y);
        setHeight(2 * RADIUS);
        setWidth(2 * RADIUS);
        xO = x + RADIUS;
        yO = y + RADIUS;
        setImg(ImgData.getData().getEpsilon());
    }

    @Override
    public void paint(Graphics g){
        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(getImg(), getX(), getY(), getWidth(), getHeight(), null);
//        g2D.setColor(Color.magenta);
//        g2D.fillOval(getXO() - RADIUS, getYO() - RADIUS, getWidth(), getHeight());
//        g2D.setColor(Color.black);
//        g2D.fillOval(getXO() - RADIUS +2, getYO() - RADIUS +2, getWidth()-4, getHeight()-4);
    }
}
