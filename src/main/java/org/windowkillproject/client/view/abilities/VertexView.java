package org.windowkillproject.client.view.abilities;

import java.awt.*;

public class VertexView extends AbilityView {
    public VertexView(String globeId, String id, int x, int y) {
        super(globeId, id, x, y);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(Color.orange);
        g2D.fillOval(getX() - 2, getY() - 2, 5, 5);
    }
}
