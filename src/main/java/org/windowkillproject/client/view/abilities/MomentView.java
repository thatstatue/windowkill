package org.windowkillproject.client.view.abilities;

import java.awt.*;

public class MomentView extends AbilityView {
    public MomentView(String globeId, String id, int x, int y, int radius) {
        super(globeId, id, x, y);
        setWidth(radius);
        setHeight(radius);
    }
    private int colorOpacity;
    public void setColorOpacity(int opacity){
        colorOpacity = Math.max(Math.min(250, opacity),0);
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(new Color(250,0,0, colorOpacity));
        g2D.fillOval(getX(), getY(), getWidth(), getHeight());
    }
}
