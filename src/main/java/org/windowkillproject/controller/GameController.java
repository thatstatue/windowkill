package org.windowkillproject.controller;

import org.windowkillproject.application.Config;
import org.windowkillproject.model.abilities.CollectableModel;
import org.windowkillproject.model.entities.EntityModel;
import org.windowkillproject.model.entities.EpsilonModel;
import org.windowkillproject.model.abilities.Vertex;
import org.windowkillproject.model.entities.enemies.EnemyModel;
import org.windowkillproject.model.entities.enemies.TrigorathModel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static org.windowkillproject.application.Application.gameFrame;
import static org.windowkillproject.application.Config.LOOP;
import static org.windowkillproject.controller.Utils.impactPoint;
import static org.windowkillproject.model.abilities.CollectableModel.collectableModels;
import static org.windowkillproject.model.entities.EntityModel.entityModels;


public abstract class GameController {
    public static Random random = new Random();

    public static ArrayList<EnemyModel> getEnemies(){
        ArrayList<EnemyModel> enemies = new ArrayList<>();
        for (EntityModel entityModel : entityModels){
            if (entityModel instanceof EnemyModel){
                enemies.add((EnemyModel) entityModel);
            }
        }
        return enemies;
    }

    public static void impact(EntityModel entityModel){
        for (EntityModel other : entityModels){
            if (!entityModel.equals(other) && (other instanceof EnemyModel)){
                EnemyModel enemyModel = (EnemyModel) other;
                Point2D deltaS = impactPoint(enemyModel.getAnchor(), entityModel.getAnchor());
                enemyModel.move((int) deltaS.getX(), (int) deltaS.getY());
            }
        }
    }
    public static void enemyIntersectionControl() {
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

    public static void createWave(int level) {

        AtomicInteger count = new AtomicInteger();
        Timer creatorTimer = new Timer((int) (LOOP * (1 - 0.2*level)), null);
        creatorTimer.addActionListener(e -> {
            if (count.get() < (level * (level + 5))) {
                Direction direction = Direction.values()[random.nextInt(4)];
                int dX = random.nextInt(Config.GAME_WIDTH);
                int dY = random.nextInt(Config.GAME_HEIGHT);
                switch (direction) {
                    case TopRight -> new TrigorathModel(gameFrame.getWidth() + dX, -dY);
                    case TopLeft -> new TrigorathModel(-dX, -dY);
                    case ButtomLeft -> new TrigorathModel(-dX, gameFrame.getHeight() + dY);
                    case ButtomRight -> new TrigorathModel(gameFrame.getWidth() + dX, gameFrame.getHeight() + dY);
                }
                count.getAndIncrement();
            } else {
                creatorTimer.stop();
            }
        });
        creatorTimer.start();
        //todo: add square
    }
    public static void epsilonRewardControl(){
        for (int i = 0; i< collectableModels.size(); i++){
            CollectableModel collectableModel = collectableModels.get(i);
            if (collectableModel.isCollectedByEpsilon()){
                EpsilonModel.getINSTANCE().collected(collectableModel.getRewardHp());
                collectableModels.remove(collectableModel);
                collectableModel.destroy();
            }
        }
    }
    public static void epsilonIntersectionControl(){
        EpsilonModel epsilonModel = EpsilonModel.getINSTANCE();
        ArrayList<EnemyModel> enemies = getEnemies();
        for (EnemyModel enemyModel : enemies) {

            //vertex of enemy hit epsilon
            for (Vertex vertex: enemyModel.getVertices()){
                if (vertex.isCollectedByEpsilon()){
                    epsilonModel.gotHit(enemyModel);
                    impact(epsilonModel);
                    Point2D deltaS = impactPoint(epsilonModel.getAnchor(), enemyModel.getAnchor());
                    epsilonModel.move((int) deltaS.getX(), (int) deltaS.getY());
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
    private enum Direction{
        TopLeft, TopRight, ButtomLeft, ButtomRight
    }
}
