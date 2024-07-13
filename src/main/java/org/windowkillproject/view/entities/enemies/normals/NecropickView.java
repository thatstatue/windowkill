package org.windowkillproject.view.entities.enemies.normals;

import org.windowkillproject.application.panels.game.GamePanel;
import org.windowkillproject.view.entities.enemies.EnemyView;

import java.awt.*;
import java.util.Arrays;

public class NecropickView extends EnemyView {
    public NecropickView(String id) {
        super(id);
    }
    @Override
    public void paint(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        //g2D.drawImage(getImg(), getX()+2, getY()+2, getWidth(), getHeight(), null);
        g2D.setColor(Color.gray);
        g2D.setStroke(new BasicStroke(10));
        if (getPolygon() != null) {
//            System.out.println("HOY "+ Arrays.toString(getPolygon().xpoints));
//            System.out.println("HOY "+ Arrays.toString(getPolygon().ypoints));

            g2D.fillPolygon(getPolygon());
            g2D.setColor(Color.darkGray);
            drawGreyRect(g2D);
        }
    }

    private void drawGreyRect(Graphics2D g2D) {
         int[] xPoints = new int[4];
         int[] yPoints = new int[4];

        for (int i = 3; i < 7; i++) {
            xPoints[i-3] = getPolygon().xpoints[i];
            yPoints[i-3] = getPolygon().ypoints[i];
        }
//        System.out.println("\n"+ Arrays.toString(xPoints));
//        System.out.println(Arrays.toString(yPoints)+"\n");

        Polygon greyRect = new Polygon(xPoints,yPoints,4);
        g2D.fillPolygon(greyRect);
    }
}
