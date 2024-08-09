package org.windowkillproject.client.view.entities.enemies.normals;

import org.windowkillproject.client.view.entities.enemies.EnemyView;

import java.awt.*;


public class NecropickView extends EnemyView {
    public NecropickView(String globeId, String id, Polygon polygon) {
        super(globeId, id, polygon);
    }
    @Override
    public void paint(Graphics g) {
        if (isVisible()) {
            Graphics2D g2D = (Graphics2D) g;
            g2D.setColor(Color.gray);
            g2D.setStroke(new BasicStroke(10));
            if (getPolygon() != null) {

                g2D.fillPolygon(getPolygon());
                g2D.setColor(Color.darkGray);
                drawGreyRect(g2D);
            }
        }
    }

    private void drawGreyRect(Graphics2D g2D) {
         int[] xPoints = new int[4];
         int[] yPoints = new int[4];

        for (int i = 3; i < 7; i++) {
            xPoints[i-3] = getPolygon().xpoints[i];
            yPoints[i-3] = getPolygon().ypoints[i];
        }

        Polygon greyRect = new Polygon(xPoints,yPoints,4);
        g2D.fillPolygon(greyRect);
    }

}
