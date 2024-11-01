package org.windowkillproject.client.view.abilities;

import java.awt.*;

import static org.windowkillproject.Constants.EPSILON_RADIUS;

public class PortalView extends AbilityView {
    public PortalView(String globeId, String id, int x, int y) {
        super(globeId, id, x, y);
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(new Color(1, 236, 255, 50));
        g2D.fillRect(getX() - 2, getY() - 2, EPSILON_RADIUS*5, EPSILON_RADIUS*5);
    }

}