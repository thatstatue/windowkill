package org.windowkillproject.controller;

import org.windowkillproject.application.Config;
import org.windowkillproject.application.frames.GamePanel;
import org.windowkillproject.model.entities.EntityModel;
import org.windowkillproject.model.entities.EpsilonModel;
import org.windowkillproject.model.abilities.Vertex;
import org.windowkillproject.model.entities.enemies.EnemyModel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.util.ArrayList;

import static org.windowkillproject.application.Application.gameFrame;
import static org.windowkillproject.model.abilities.BulletModel.bulletModels;
import static org.windowkillproject.model.entities.EntityModel.entityModels;


public abstract class GameController {
    public static Timer gameTimer;
    public static void start(){
        GamePanel gamePanel = gameFrame.getGamePanel();
        gameFrame.shrinkFast();
        gameTimer = new Timer(Config.FPS, e -> {
            gameFrame.shrink();
            for (EntityModel entityModel : entityModels) {
                entityModel.rotate();
                if (entityModel instanceof EnemyModel) {
                    EnemyModel enemyModel = (EnemyModel) entityModel;
                    enemyModel.route(EpsilonModel.getINSTANCE()); //for 1 panel only
                }
            }
            for (int i = 0; i < bulletModels.size(); i++){
                bulletModels.get(i).move();
            }
            enemyIntersectionControl(gamePanel);
            gamePanel.keepEpsilonInBounds();
            gamePanel.repaint();
            epsilonIntersectionControl();
        });
        gameTimer.start();
    }
    public static ArrayList<EnemyModel> getEnemies(){
        ArrayList<EnemyModel> enemies = new ArrayList<>();
        for (EntityModel entityModel : entityModels){
            if (entityModel instanceof EnemyModel){
                enemies.add((EnemyModel) entityModel);
            }
        }
        return enemies;
    }
    public static double deltaAway(EntityModel entityModel, EntityModel other){
        double d1 = entityModel.getXO() - other.getXO();
        double d2 = entityModel.getYO() - other.getYO();
        double d = Math.sqrt(d1* d1 + d2*d2);
        int speed = (int) (Config.MAX_ENEMY_SPEED*3 - (d/6));
        return Math.max(0 , speed);
    }
    public static double vectorTheta(EntityModel entityModel, EntityModel other){
        return Math.atan2(entityModel.getYO() - other.getYO(), entityModel.getXO() - other.getXO());
    }


    public static void impact(EntityModel entityModel){
        for (EntityModel other : entityModels){
            if (!entityModel.equals(other) && (other instanceof EnemyModel)){
                EnemyModel enemyModel = (EnemyModel) other;
                double theta = vectorTheta(entityModel, enemyModel);
                double deltaS = deltaAway(entityModel, enemyModel) * 5;
                int yS = (int) (Math.cos(theta) * deltaS);
                int xS = (int) (Math.cos(theta) * deltaS);

                if (deltaS!=0 && entityModel instanceof EnemyModel){
                    xS +=20;
                    yS -= 20;
                }//todo: fix enemies getting stuck behind each other
                enemyModel.moveX(-xS);
                enemyModel.moveY(-yS);
            }
        }
    }
    public static void enemyIntersectionControl(GamePanel gamePanel) {
        ArrayList<EnemyModel> enemies = getEnemies();
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
        GamePanel gamePanel = (GamePanel) gameFrame.getContentPane();
        EpsilonModel epsilonModel = EpsilonModel.getINSTANCE();
        ArrayList<EnemyModel> enemies = getEnemies();
        for (EnemyModel enemyModel : enemies) {

            //vertex of enemy hit epsilon
            for (Vertex vertex: enemyModel.getVertices()){
                if (Math.abs(vertex.getX()- epsilonModel.getXO()) <= epsilonModel.getRadius()
                && Math.abs(vertex.getY()- epsilonModel.getYO()) <= epsilonModel.getRadius()){
                    epsilonModel.gotHit(enemyModel);
                    impact(epsilonModel);
                    break;
                }
            }

            //vertex of epsilon hit enemy
            Area enemyA = new Area(enemyModel.getPolygon());
            for (Vertex epsilonV : epsilonModel.getVertices()){
                if (enemyA.contains(epsilonV.getX(), epsilonV.getY())){
                    enemyModel.gotHit(epsilonModel);
                    impact(epsilonModel);
                    break;
                }
            }
        }
    }
}
