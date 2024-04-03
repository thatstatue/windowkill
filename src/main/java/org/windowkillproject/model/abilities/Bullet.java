package org.windowkillproject.model.abilities;

import org.windowkillproject.application.Config;
import org.windowkillproject.application.frames.GamePanel;
import org.windowkillproject.model.entities.enemies.Enemy;

import java.awt.*;
import java.awt.geom.Area;

import static org.windowkillproject.application.Application.gameFrame;

public class Bullet extends Ability {
    private int attackHp = 5;
    private final int delta = 10;
    private double theta;

    public double getTheta() {
        return theta;
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }

    public void setAttackHp(int attackHp) {
        this.attackHp = attackHp;
    }
    public void destroy(GamePanel gamePanel){
        gamePanel.getEntities().remove(this);
    }//todo: update
    public int getAttackHp() {
        return attackHp;
    }
    public void move(){

        if (isShoot()) {
            setX(getX()+(int) (delta * Math.cos(theta)));
            setY(getY()+(int) (delta * Math.sin(theta)));
            GamePanel gamePanel = gameFrame.getGamePanel();
            for (Enemy enemy : gamePanel.getEnemies()) {
                Area enemyA = new Area(enemy.getPolygon());
                if (enemyA.contains(this.getX(), this.getY())) {
                    enemy.gotShoot(this, gamePanel);
                    explode(gamePanel);
                    break;
                }
            }
            if (getX()<0 || getX()> gameFrame.getWidth() ||
                    getY()<0 || getY() > gameFrame.getHeight()){
                if (getX()<0){
                    gameFrame.stretch(Config.BULLET_HIT_LEFT);
                }
                if (getX()> gameFrame.getWidth()){
                    gameFrame.stretch(Config.BULLET_HIT_RIGHT);
                }
                if (getY()<0){
                    gameFrame.stretch(Config.BULLET_HIT_UP);
                }
                if (getY() > gameFrame.getHeight()){
                    gameFrame.stretch(Config.BULLET_HIT_DOWN);
                }
                explode(gamePanel);
            }
        }
    }

    protected Bullet(int x, int y) {
        super(x, y);
        isShoot = false;
    }
    private boolean isShoot;
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(Color.red);
        g2D.fillOval(getX(), getY(), 5,5);
    }

    public boolean isShoot() {
        return isShoot;
    }
    public void explode(GamePanel gamePanel){
          gamePanel.getEpsilon().getBullets().remove(this);
    }

    public void setShoot(boolean shoot) {
        isShoot = shoot;
    }
}
