package org.windowkillproject.view.abilities;

import org.windowkillproject.application.panels.game.GamePanel;

import java.awt.*;

public class CollectableView extends AbilityView {
    public CollectableView(String id, int x, int y) {
        super(id, x, y);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(Color.cyan);
        g2D.fillOval(getX() - 2, getY() - 2, 5, 5);
    }
}
