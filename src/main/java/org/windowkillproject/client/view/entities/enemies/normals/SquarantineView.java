package org.windowkillproject.client.view.entities.enemies.normals;

import org.windowkillproject.client.view.entities.enemies.EnemyView;

import java.awt.*;

public class SquarantineView extends EnemyView {
    public SquarantineView(String globeId, String id, Polygon polygon) {
        super(globeId, id, polygon);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(Color.green);
        g2D.setStroke(new BasicStroke(3));
        if (getPolygon() != null) g2D.drawPolygon(getPolygon());
    }

}
