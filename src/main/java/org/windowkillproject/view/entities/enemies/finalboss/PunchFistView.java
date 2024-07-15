package org.windowkillproject.view.entities.enemies.finalboss;

import org.windowkillproject.view.ImgData;
import org.windowkillproject.view.entities.enemies.EnemyView;

import java.awt.*;

public class PunchFistView extends EnemyView {
    public PunchFistView(String id) {
        super(id);
        setImg(ImgData.getData().getPunchFist());
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(getImg(), getX(), getY(), getWidth(), getHeight(), null);
    }
}
