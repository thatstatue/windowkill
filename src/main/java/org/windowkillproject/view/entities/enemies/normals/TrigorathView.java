package org.windowkillproject.view.entities.enemies.normals;

import org.windowkillproject.view.entities.enemies.EnemyView;

import java.awt.*;

public class TrigorathView extends EnemyView {
    public TrigorathView(String id) {
        super(id);
    }

    @Override
    public void paint(Graphics g){
        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(Color.yellow);
        g2D.setStroke(new BasicStroke(2));
        if (getPolygon() != null) g2D.drawPolygon(getPolygon());
    }
}
