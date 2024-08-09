package org.windowkillproject.client.view.entities.enemies.finalboss;

import org.windowkillproject.client.view.ImgData;
import org.windowkillproject.client.view.entities.enemies.EnemyView;

import java.awt.*;

public class RightHandView extends EnemyView {
    public RightHandView(String globeId, String id, Polygon polygon) {
        super(globeId, id, polygon);
        setImg(ImgData.getData().getRightHand());
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(getImg(), getX(), getY(), getWidth(), getHeight(), null);
    }
}
