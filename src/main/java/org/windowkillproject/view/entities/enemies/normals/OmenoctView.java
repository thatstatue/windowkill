package org.windowkillproject.view.entities.enemies.normals;

import org.windowkillproject.application.panels.game.GamePanel;
import org.windowkillproject.view.entities.enemies.EnemyView;

import java.awt.*;
import java.util.Arrays;

public class OmenoctView extends EnemyView {
    public OmenoctView(String id) {
        super(id);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        //g2D.drawImage(getImg(), getX()+2, getY()+2, getWidth(), getHeight(), null);
        g2D.setColor(Color.WHITE);
        g2D.setStroke(new BasicStroke(3));
        if (getPolygon() != null) {
            g2D.fillPolygon(getPolygon());
            g2D.setColor(Color.RED);
            drawRedTriangles(g2D);
        }
    }

    private void drawRedTriangles(Graphics2D g2D) {

        for (int i = 0; i < 8; i += 2) {
            int[] xPoints = new int[3];
            int[] yPoints = new int[3];
            for (int j = 0; j < 2; j++) {
                xPoints[j] = getPolygon().xpoints[i+j];
                yPoints[j] = getPolygon().ypoints[i+j];
            }

            xPoints[2] = getX()+(getWidth()/2);
            yPoints[2] = getY()+(getHeight()/2);

            Polygon redTriangle = new Polygon(xPoints,yPoints,3);
            g2D.fillPolygon(redTriangle);
        }
    }
}
