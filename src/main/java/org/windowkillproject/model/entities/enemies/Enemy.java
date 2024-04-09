package org.windowkillproject.model.entities.enemies;

import org.windowkillproject.application.Config;
import org.windowkillproject.controller.GameController;
import org.windowkillproject.model.entities.Entity;
import org.windowkillproject.model.entities.EpsilonModel;

import java.awt.*;

public abstract class Enemy extends Entity {
    protected Enemy(int x, int y) {
        super(x, y);
        setySpeed(Config.MAX_ENEMY_SPEED);
        setxSpeed(Config.MAX_ENEMY_SPEED);
    }
    private int xSpeed, ySpeed;
    private Polygon polygon;

    public int getxSpeed() {
        return xSpeed;
    }

    public void setxSpeed(int xSpeed) {
        this.xSpeed = xSpeed;
    }

    public int getySpeed() {
        return ySpeed;
    }

    public void setySpeed(int ySpeed) {
        this.ySpeed = ySpeed;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }


    private double deltaSpeed(Entity entity, Entity other){
        double d1 = entity.getXO() - other.getXO();
        double d2 = entity.getYO() - other.getYO();
        double d = Math.sqrt(d1* d1 + d2*d2);
        int speed = (int) (d/25 + 0.75);
        //System.out.println("the speed forward " + speed);
        return Math.min(Config.MAX_ENEMY_SPEED , speed);
    }

    public void route(EpsilonModel epsilonModel){
        //set accel
        double theta = GameController.vectorTheta(epsilonModel,this);
        double deltaS = deltaSpeed(this, epsilonModel);
        int xS = (int) (Math.cos(theta) * deltaS);
        int yS = (int) (Math.sin(theta) * deltaS);
        //move
        moveX(xS);
        moveY(yS);
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
