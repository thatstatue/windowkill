package org.windowkillproject.view;


import java.awt.*;
import java.util.ArrayList;

public class BulletView extends AbilityView {
    public static ArrayList<BulletView> bulletViews = new ArrayList<>();

    public BulletView(String id, int x, int y) {
        super(id, x, y);
        bulletViews.add(this);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(Color.red);
        g2D.fillOval(getX(), getY(), 5, 5);
    }

}
