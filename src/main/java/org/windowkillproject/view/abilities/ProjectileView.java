package org.windowkillproject.view.abilities;

import java.awt.*;

public class ProjectileView extends AbilityView{
    public ProjectileView(String id, int x, int y) {
        super(id, x, y);
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setStroke(new BasicStroke(5));
        g2D.setColor(Color.red);
        g2D.fillArc(getX() - 3, getY() - 3, 6, 6, 0, 180);
        g2D.setColor(Color.WHITE);
        g2D.fillArc(getX() - 3, getY() - 3, 6, 6, 180, 180);
    }
}
