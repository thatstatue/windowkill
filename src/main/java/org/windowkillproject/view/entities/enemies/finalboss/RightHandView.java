package org.windowkillproject.view.entities.enemies.finalboss;

import org.windowkillproject.view.ImgData;
import org.windowkillproject.view.entities.enemies.EnemyView;

import java.awt.*;

public class RightHandView extends EnemyView {
    public RightHandView(String id) {
        super(id);
        setImg(ImgData.getData().getRightHand());
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(getImg(), getX(), getY(), getWidth(), getHeight(), null);
    }
}
