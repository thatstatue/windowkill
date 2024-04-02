package org.windowkillproject.model.entities;

import java.awt.*;

public class Bullet extends Ability{
    protected Bullet(int x, int y) {
        super(x, y);
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.fillOval(getX(), getY(), 5,5);
    }
}
