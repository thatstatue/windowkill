package org.windowkillproject.view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class BulletView extends JLabel {
    String id;
    Point2D location=new Point2D.Double(0,0);
    public static ArrayList<BulletView> bulletViews =new ArrayList<>();
    public BulletView(String id) {
        this.id = id;
        bulletViews.add(this);
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(Color.red);
        g2D.fillOval(getX(), getY(), 5,5);
    }

}
