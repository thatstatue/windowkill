package org.windowkillproject.view.abilities;

import org.windowkillproject.application.panels.game.GamePanel;

import java.awt.*;

public class ProjectileView extends AbilityView{
    public ProjectileView(String id, int x, int y, Color topColor, Color bottomColor) {
        super(id, x, y);
        this.topColor = topColor;
        this.bottomColor = bottomColor;
    }
    private final Color topColor ,  bottomColor;

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setStroke(new BasicStroke(5));
        g2D.setColor(topColor);
        g2D.fillArc(getX() - 3, getY() - 3, 6, 6, 0, 180);
        g2D.setColor(bottomColor);
        g2D.fillArc(getX() - 3, getY() - 3, 6, 6, 180, 180);
    }
}
