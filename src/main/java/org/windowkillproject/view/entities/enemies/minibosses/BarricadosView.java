package org.windowkillproject.view.entities.enemies.minibosses;

import org.windowkillproject.view.ImgData;
import org.windowkillproject.view.entities.enemies.EnemyView;

import java.awt.*;

public class BarricadosView extends EnemyView {
    public BarricadosView(String id) {
        super(id);
           setImg(ImgData.getData().getBarricados());
    }

    @Override
    public void paint(Graphics g){
        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(getImg(), getX(), getY(), getWidth(), getHeight(), null);
    }
}
