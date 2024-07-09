package org.windowkillproject.view.entities.enemies;

import org.windowkillproject.application.panels.game.GamePanel;

import java.awt.*;

public class TrigorathView extends EnemyView {
    public TrigorathView(String id) {
        super(id);
    //    setImg(ImgData.getData().getTrigorath());
    }

    @Override
    public void paint(Graphics g){
        Graphics2D g2D = (Graphics2D) g;
        //g2D.drawImage(getImg(), getX()+2, getY()+2, getWidth(), getHeight(), null);
        g2D.setColor(Color.yellow);
        g2D.setStroke(new BasicStroke(2));
        if (getPolygon() != null) g2D.drawPolygon(getPolygon());
    }
}
