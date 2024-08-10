package org.windowkillproject.client.view.abilities;

import java.awt.*;

public class ProjectileView extends AbilityView{
    public ProjectileView(String globeId, String id, int x, int y, Color topColor, Color bottomColor) {
        super(globeId, id, x, y);
        this.topColor = topColor;
        this.bottomColor = bottomColor;
    }
    public ProjectileView(String globeId, String id, int x, int y){
        this(globeId, id,x, y, Color.yellow, Color.yellow);
    }
    private final Color topColor ,  bottomColor;

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2D = (Graphics2D) g;
        if (isVisible()) {
            g2D.setStroke(new BasicStroke(5));
            g2D.setColor(topColor);
            g2D.fillArc(getX() - 3, getY() - 3, 6, 6, 0, 180);
            g2D.setColor(bottomColor);
            g2D.fillArc(getX() - 3, getY() - 3, 6, 6, 180, 180);
        }
    }
}
