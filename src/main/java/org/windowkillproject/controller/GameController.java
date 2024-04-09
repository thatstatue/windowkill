package org.windowkillproject.controller;

import org.windowkillproject.application.Config;
import org.windowkillproject.application.frames.GamePanel;
import org.windowkillproject.model.abilities.Bullet;
import org.windowkillproject.model.entities.Entity;
import org.windowkillproject.model.entities.EpsilonModel;
import org.windowkillproject.model.abilities.Vertex;
import org.windowkillproject.model.entities.enemies.Enemy;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.util.ArrayList;

import static org.windowkillproject.application.Application.gameFrame;


public class GameController {
    public static Timer gameTimer;
    public static void start(){
        GamePanel gamePanel = gameFrame.getGamePanel();
        gameFrame.shrinkFast();
        gameTimer = new Timer(Config.DELAY, e -> {
            gameFrame.shrink();
            for (Entity entity : gamePanel.getEntities()) {
                entity.rotate();
                if (entity instanceof Enemy) {
                    Enemy enemy = (Enemy) entity;
                    enemy.route(gamePanel.getEpsilon()); //for 1 panel only
                }
            }
            ArrayList<Bullet> bullets = gamePanel.getEpsilon().getBullets();
            for (int i = 0 ; i < bullets.size(); i++){
                bullets.get(i).move();
            }
            enemyIntersectionControl(gamePanel);
            gamePanel.checkBounds();
            gamePanel.repaint();
            epsilonIntersectionControl();
        });
        gameTimer.start();
    }
    public static ArrayList<Enemy> getEnemies(GamePanel panel){
        ArrayList<Enemy> enemies = new ArrayList<>();
        for (Entity entity : panel.getEntities()){
            if (entity instanceof Enemy){
                enemies.add((Enemy) entity);
            }
        }
        return enemies;
    }
    public static double deltaAway(Entity entity, Entity other){
        double d1 = entity.getXO() - other.getXO();
        double d2 = entity.getYO() - other.getYO();
        double d = Math.sqrt(d1* d1 + d2*d2);
        int speed = (int) (Config.MAX_ENEMY_SPEED*3 - (d/6));
        return Math.max(0 , speed);
    }
    public static double vectorTheta(Entity entity, Entity other){
        return Math.atan2(entity.getYO() - other.getYO(), entity.getXO() - other.getXO());
    }


    public static void impact(Entity entity, GamePanel gp){
        for (Entity other : gp.getEntities()){
            if (!entity.equals(other) && (other instanceof Enemy)){
                Enemy enemy = (Enemy) other;
                double theta = vectorTheta(entity, enemy);
                double deltaS = deltaAway(entity, enemy) * 5;
                int yS = (int) (Math.cos(theta) * deltaS);
                int xS = (int) (Math.cos(theta) * deltaS);

                if (deltaS!=0 && entity instanceof Enemy){
                    xS +=20;
                    yS -= 20;
                }//todo: fix enemies getting stuck behind each other
                enemy.moveX(-xS);
                enemy.moveY(-yS);
            }
        }
    }
    public static void enemyIntersectionControl(GamePanel gamePanel) {
        ArrayList<Enemy> enemies = getEnemies(gamePanel);
        for (int i = 0; i < enemies.size(); i++) {
            Polygon p1 = enemies.get(i).getPolygon();
            Area a1 = new Area(p1);
            for (int j = i + 1; j < enemies.size(); j++) {
                Polygon p2 = enemies.get(j).getPolygon();
                Area a2 = new Area(p2);
                a2.intersect(a1);
                if (!a2.isEmpty()) {
                    impact(enemies.get(i), gamePanel);
                }
            }
        }

    }
    public static void epsilonIntersectionControl(){
        GamePanel gamePanel = (GamePanel) gameFrame.getContentPane();
        EpsilonModel epsilonModel = gamePanel.getEpsilon();
        ArrayList<Enemy> enemies = getEnemies(gamePanel);
        for (Enemy enemy : enemies) {

            //vertex of enemy hit epsilon
            for (Vertex vertex: enemy.getVertices()){
                if (Math.abs(vertex.getX()- epsilonModel.getXO()) <= epsilonModel.getRadius()
                && Math.abs(vertex.getY()- epsilonModel.getYO()) <= epsilonModel.getRadius()){
                    epsilonModel.gotHit(enemy, gamePanel);
                    impact(epsilonModel, gamePanel);
                    break;
                }
            }

            //vertex of epsilon hit enemy
            Area enemyA = new Area(enemy.getPolygon());
            for (Vertex epsilonV : epsilonModel.getVertices()){
                if (enemyA.contains(epsilonV.getX(), epsilonV.getY())){
                    enemy.gotHit(epsilonModel, gamePanel);
                    impact(epsilonModel, gamePanel);
                    break;
                }
            }
        }
    }
}
