package org.windowkillproject.model.entities.enemies;

import org.windowkillproject.application.Config;
import org.windowkillproject.model.abilities.CollectableModel;
import org.windowkillproject.model.entities.EntityModel;

import java.awt.*;

import static org.windowkillproject.application.SoundPlayer.playDestroy;
import static org.windowkillproject.controller.GameController.random;

public abstract class EnemyModel extends EntityModel {
    protected EnemyModel(int x, int y) {
        super(x, y);
        setySpeed(Config.MAX_ENEMY_SPEED);
        setxSpeed(Config.MAX_ENEMY_SPEED);
    }

    private int rewardCount, rewardXps;
    private static int enemiesKilled;

    public static void setEnemiesKilled(int enemiesKilled) {
        EnemyModel.enemiesKilled = enemiesKilled;
    }

    public static int getEnemiesKilled() {
        return enemiesKilled;
    }

    protected void setRewardCount(int rewardCount) {
        this.rewardCount = rewardCount;
    }

    public void setRewardXps(int rewardXps) {
        this.rewardXps = rewardXps;
    }

    private int xSpeed, ySpeed;
    private Polygon polygon;

    public void setxSpeed(int xSpeed) {
        this.xSpeed = xSpeed;
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


    @Override
    public void destroy() {
        super.destroy();
        playDestroy();
        enemiesKilled++;
        for (int i = 0; i < rewardCount; i++) {
            int x = getXO() + random.nextInt(2 * getRadius()) - getRadius();
            int y = getYO() + random.nextInt(2 * getRadius()) - getRadius();

            new CollectableModel(x, y, rewardXps);
        }
    }

    abstract void initVertices();

    @Override
    public void rotate() {
        super.rotate();
        for (int i = 0; i < getVertices().size(); i++) {
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
