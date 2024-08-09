package org.windowkillproject.client.view.entities.enemies.minibosses;

import org.windowkillproject.client.view.entities.enemies.EnemyView;

import java.awt.*;
import java.util.ArrayList;

import static org.windowkillproject.server.Config.ORB_RADIUS;


public class BlackOrbView extends EnemyView {
    public BlackOrbView(String globeId, String id, Polygon polygon) {
        super(globeId, id, polygon);
        blackOrbViews.add(this);
    }
    private static ArrayList<BlackOrbView> blackOrbViews = new ArrayList<>();
    public static void resetOrbViews(){
        blackOrbViews.clear();
    }
    @Override
    public void paint(Graphics g){
        Graphics2D g2D = (Graphics2D) g;
        if (isVisible()) {
            g2D.setStroke(new BasicStroke(40));
            g2D.setColor(new Color(255,0, 244, 50));
            for (BlackOrbView blackOrbView: blackOrbViews){
               // if (findModel(blackOrbView.getId())!= null){ todo? is it being checked from elsewhere
                    g2D.drawLine(getX()+ getWidth()/2, getY() + getHeight()/2,
                            blackOrbView.getX()+getWidth()/2, blackOrbView.getY()+ getHeight()/2);
            //    }
            }
            g2D.setStroke(new BasicStroke(3));
            g2D.setColor(Color.black);
            g2D.fillOval(getX(), getY(), getWidth(), getHeight());
            g2D.setColor(Color.magenta);
            g2D.drawOval(getX(), getY(), getWidth(), getHeight());
            g2D.fillOval(getX() + ORB_RADIUS / 2, getY() + ORB_RADIUS / 2, ORB_RADIUS / 3, ORB_RADIUS / 3);
        }

    }
}
