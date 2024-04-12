package org.windowkillproject.view.abilities;


import java.awt.*;
import java.util.ArrayList;

public class BulletView extends AbilityView {
    public BulletView(String id, int x, int y) {
        super(id, x, y);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(Color.red);
        g2D.fillOval(getX()-2, getY()-2, 5, 5);
    }

}
