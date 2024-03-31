package org.windowkillproject.model.entities.enemies;

import org.windowkillproject.model.entities.Entity;
import org.windowkillproject.model.entities.Vertex;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public abstract class Enemy extends Entity {
    protected Enemy(int x, int y) {
        super(x, y);
    }
    private int speed;
    private double acceleration;
    private Polygon polygon;

    public Polygon getPolygon() {
        return polygon;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }

    public void route(){

    }
    @Override
    public void rotate(){
        super.rotate();
        for (int i = 0 ; i < getVertices().size(); i ++){
            polygon.xpoints[i] = getVertices().get(i).getX();
            polygon.ypoints[i] = getVertices().get(i).getY();
        }


        /*
        BufferedImage buffer = getImg();
        AffineTransform tx = new AffineTransform();

        double rads = 0.084;
        double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
        int w = img.getWidth();
        int h = img.getHeight();
        int newWidth = (int) Math.ceil(w * cos + h * sin);
        int newHeight = (int) Math.ceil(h * cos + w * sin);

        tx.translate((newWidth - w) / 2, (newHeight - h) / 2);

        int x = w / 2;
        int y = h / 2;

        tx.rotate(rads, x, y);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        setImg(op.filter(buffer, null));

         */
    }
}
