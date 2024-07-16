package org.windowkillproject.view.entities.enemies.normals;

import org.windowkillproject.view.entities.enemies.EnemyView;

import java.awt.*;

import static org.windowkillproject.application.Config.WYRM_RADIUS;

public class WyrmView extends EnemyView {
    public WyrmView(String id) {
        super(id);
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setStroke(new BasicStroke(3));
        g2D.setColor(Color.magenta);
        g2D.drawArc(getX()+10 , getY()+10 , WYRM_RADIUS*2 - 20, WYRM_RADIUS*2 - 10, 0, 180);
        if (getPolygon() != null) {
            g2D.drawPolygon(getPolygon());
        }
        g2D.drawArc(getX()+10 , getY()- 10, WYRM_RADIUS*2 - 20, WYRM_RADIUS*2 - 10, 180, 180);
        drawBlackRect(g2D);
        g2D.fillOval(getX()+25 , getY()+15 , WYRM_RADIUS*2 - 50, WYRM_RADIUS*2 - 40);

        g2D.setColor(Color.magenta);
        g2D.drawOval(getX()+WYRM_RADIUS- 5, getY() + (WYRM_RADIUS), 38, 38);
    }

    private void drawBlackRect(Graphics2D g2D) {
        int[] xPoints = new int[6];
        int[] yPoints = new int[6];
        xPoints[0] = getPolygon().xpoints[0]+2;
        yPoints[0] = getPolygon().ypoints[0]-2;
        xPoints[1] = getPolygon().xpoints[1]-2;
        yPoints[1] = getPolygon().ypoints[1]-2;

        xPoints[2] = getPolygon().xpoints[2]-1;
        yPoints[2] = getPolygon().ypoints[2];

        xPoints[3] = getPolygon().xpoints[3]-6;
        yPoints[3] = getPolygon().ypoints[3]+2;
        xPoints[4] = getPolygon().xpoints[4]+6;
        yPoints[4] = getPolygon().ypoints[4]+2;

        xPoints[5] = getPolygon().xpoints[5]+1;
        yPoints[5] = getPolygon().ypoints[5];

        Polygon blackRect = new Polygon(xPoints,yPoints,6);
        g2D.setColor(Color.black);
        g2D.fillPolygon(blackRect);
    }
}
