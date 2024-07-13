package org.windowkillproject.view.entities.enemies.normals;

import org.windowkillproject.view.entities.enemies.EnemyView;

import java.awt.*;

import static org.windowkillproject.application.Config.ARCHMIRE_RADIUS;

public class ArchmireView extends EnemyView {
    public ArchmireView(String id) {
        super(id);
        setHovering(true);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        //g2D.drawImage(getImg(), getX()+2, getY()+2, getWidth(), getHeight(), null);
        g2D.setColor(new Color(136, 0, 21));
        g2D.setStroke(new BasicStroke(1));


        g2D.fillArc(getX()-7, getY(), ARCHMIRE_RADIUS * 2, (int) (ARCHMIRE_RADIUS * 2), 0, 180);
        if (getPolygon() != null) {
            g2D.drawPolygon(getPolygon());
        }
        g2D.fillArc(getX()-7, (int) (getY() + ARCHMIRE_RADIUS * 0.5), ARCHMIRE_RADIUS * 2, (int) (ARCHMIRE_RADIUS * 0.9), 180, 180);
    }
}
