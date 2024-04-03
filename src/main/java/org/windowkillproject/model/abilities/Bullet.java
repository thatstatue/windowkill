package org.windowkillproject.model.abilities;

import java.awt.*;

public class Bullet extends Ability {
    private final int hp = 5;
    private final int delta = 6;
    protected Bullet(int x, int y) {
        super(x, y);
        isShoot = false;
    }
    private boolean isShoot;
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.fillOval(getX(), getY(), 5,5);
    }

    public boolean isShoot() {
        return isShoot;
    }
    public void move(){
        if (isShoot()) {
            setX(getX() + delta);
            setY(getY() + delta);
        }
       // if(getX()>= )
    }

    public void setShoot(boolean shoot) {
        isShoot = shoot;
    }
}
