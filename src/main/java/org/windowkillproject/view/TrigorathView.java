package org.windowkillproject.view;

import org.windowkillproject.controller.Controller;
import org.windowkillproject.model.abilities.Vertex;
import org.windowkillproject.model.entities.enemies.TrigorathModel;

import java.awt.*;

public class TrigorathView extends EntityView{
    private final Polygon polygon;
    public TrigorathView(String id) {
        super(id);
    //    setImg(ImgData.getData().getTrigorath());
        this.polygon=((TrigorathModel)Controller.findModel(id)).getPolygon();
    }

    public Polygon getPolygon() {
        return polygon;
    }

    @Override
    public void paint(Graphics g){
        Graphics2D g2D = (Graphics2D) g;
        //g2D.drawImage(getImg(), getX()+2, getY()+2, getWidth(), getHeight(), null);
        g2D.setColor(Color.yellow);
        g2D.setStroke(new BasicStroke(2));
        g2D.drawPolygon(getPolygon());
    }
}
