package org.windowkillproject.controller;

import org.windowkillproject.application.Config;
import org.windowkillproject.application.frames.GamePanel;
import org.windowkillproject.model.entities.Entity;
import org.windowkillproject.model.entities.Epsilon;
import org.windowkillproject.model.entities.Vertex;
import org.windowkillproject.model.entities.enemies.Enemy;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.util.ArrayList;


public class GameController {
    private static ArrayList<GamePanel> gamePanels;
    public static Timer gameTimer;
    public static void init(ArrayList<GamePanel> gamePanels){
        GameController.gamePanels = gamePanels;
    }
    public static void start(){
        gameTimer = new Timer(Config.DELAY, e -> {
            for (int i = 0 ; i< gamePanels.size(); i++) {
                GamePanel gp = gamePanels.get(i);
                for (Entity entity : gp.getEntities()) {
                    entity.rotate();
                }
                enemyIntersectionControl(gp);
                gp.repaint();
            }
            epsilonIntersectionControl();
        }
        );
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

    public static void impact(Entity entity){

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
                    impact(enemies.get(i));
                }
            }
        }

    }
    public static void epsilonIntersectionControl(){
        GamePanel epsilonPanel =gamePanels.get(0);
        Epsilon epsilon = epsilonPanel.getEpsilon();
        ArrayList<Enemy> enemies = getEnemies(epsilonPanel);
        for (Enemy enemy : enemies) {

            //vertex of enemy hit epsilon
            for (Vertex vertex: enemy.getVertices()){
                if (Math.abs(vertex.getX()- epsilon.getXO()) <= epsilon.getRadius()
                && Math.abs(vertex.getY()- epsilon.getYO()) <= epsilon.getRadius()){
                    epsilon.gotHit(enemy, epsilonPanel);
                    impact(epsilon);
                }

                break;
            }

            //vertex of epsilon hit enemy
            Area enemyA = new Area(enemy.getPolygon());
            for (Vertex epsilonV : epsilon.getVertices()){
                if (enemyA.contains(epsilonV.getX(), epsilonV.getY())){
                    enemy.gotHit(epsilon, epsilonPanel);
                    impact(epsilon);
                }
            }
        }
    }
}
