package org.windowkillproject.controller;

import org.windowkillproject.application.Config;
import org.windowkillproject.model.abilities.CollectableModel;
import org.windowkillproject.model.entities.EntityModel;
import org.windowkillproject.model.entities.EpsilonModel;
import org.windowkillproject.model.abilities.Vertex;
import org.windowkillproject.model.entities.enemies.EnemyModel;
import org.windowkillproject.model.entities.enemies.SquarantineModel;
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
import static org.windowkillproject.controller.ElapsedTime.secondsPassed;
import static org.windowkillproject.controller.Utils.*;
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

    public static void impact(EntityModel entityModel, EnemyModel enemyModel) {
        // rEaLiStiC impact on the colliding entities
        Point2D p1 = enemyModel.getRoutePoint();
        Point2D p2 = entityModel.getRoutePoint();
        Point2D smallerP = p1;
        if (magnitude(p1)>magnitude(p2)) {
            smallerP = p2;
        }
        if ((p1.getX()*p2.getX() >0)|| (p1.getY()*p2.getY() >0)){

            smallerP = inverse(smallerP);
        }
        if (magnitude(p1)>magnitude(p2)) {
            p2 = smallerP;
        }else{
            p1 = smallerP;
        }
        //todo: epsilon in bounds
        double s1 = 1, s2=1;
        if (magnitude(p1)<5) s1 = 2.5;
        if (magnitude(p2)<5) s2 = 2.5;

        Timer impactTimer1 = getImpactTimer(entityModel, weighedVector(p1, s1));
        Timer impactTimer2 = getImpactTimer(enemyModel, weighedVector(p2, s2));
        impactTimer1.start();
        impactTimer2.start();
        //wave of collision
        Point2D collisionPoint = Utils.closestPointOnPolygon(
                entityModel.getAnchor(), enemyModel.getPointVertices());

        for (EntityModel entity : entityModels) {
            if (!(entity.equals(entityModel) || entity.equals(enemyModel))) {
                Point2D deltaS = impactPoint(entity.getAnchor(), collisionPoint);
                if(!(deltaS.getY()<1 && deltaS.getX()<1)) {
                    Timer impactTimer = getImpactTimer(entity, deltaS);
                    impactTimer.start();
                }
            }
        }
//        AtomicInteger count = new AtomicInteger();
//        Timer impactTimer = new Timer(Config.FPS/5, null);
//        impactTimer.addActionListener(e -> {
//            if (count.get() < 7) {
//                for (EntityModel entity : entityModels) {
//                    if (!(entity.equals(entityModel) || entity.equals(enemyModel))) {
//                        Point2D deltaS = impactPoint(entity.getAnchor(), collisionPoint);
//                        entityModel.move((int) deltaS.getX(), (int) deltaS.getY());
//                        isImpact = true;
//                    }
//                }
//                count.getAndIncrement();
//            } else {
//                isImpact = false;
//                impactTimer.stop();
//            }
//
//        } );
//        impactTimer.start();
    }

    private static Timer getImpactTimer(EntityModel entityModel, Point2D deltaS) {
        AtomicInteger count = new AtomicInteger();
        Timer impactTimer = new Timer(Config.FPS/5, null);
        impactTimer.addActionListener(e -> {
            if (count.get() <7) {
                entityModel.setImpact(true);
                entityModel.move((int) deltaS.getX(), (int) deltaS.getY());
                if (entityModel instanceof EpsilonModel){
                    gameFrame.getGamePanel().keepEpsilonInBounds();
                }
                count.getAndIncrement();
            }else {
                entityModel.setImpact(false);
                impactTimer.stop();
            }
        });
        return impactTimer;
    }

    public static void enemyIntersectionControl() {
        ArrayList<EnemyModel> enemies = getEnemies();
        for (int i = 0; i < enemies.size(); i++) {
            var enemyModel = enemies.get(i);
            Polygon p1 = enemyModel.getPolygon();
            Area a1 = new Area(p1);
            boolean tempCollision = false;
            //impact controller
            for (int j = i + 1; j < enemies.size(); j++) {
                var collidedEnemy = enemies.get(j);
                Polygon p2 = collidedEnemy.getPolygon();
                Area a2 = new Area(p2);
                a2.intersect(a1);
                if (!a2.isEmpty()) {
                    impact(enemyModel, collidedEnemy);
                    tempCollision = true;
                }
            }
            //dash controller
            if (enemyModel instanceof SquarantineModel){
                var squarantine = (SquarantineModel) enemyModel;
                squarantine.setCollision(tempCollision);
            }
        }
    }

    public static void epsilonRewardControl(){
        for (int i = 0; i< collectableModels.size(); i++){
            var collectableModel = collectableModels.get(i);
            if (collectableModel.isCollectedByEpsilon()){
                EpsilonModel.getINSTANCE().collected(collectableModel.getRewardXp());
                collectableModels.remove(collectableModel);
                collectableModel.destroy();
            }
            if (secondsPassed(collectableModel.getInitSeconds()) >= 10){
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
//            Point2D d = Utils.closestPointOnPolygon(
//                    epsilonModel.getAnchor(), enemyModel.getPointVertices());
//            if(Math.abs(d.distance(epsilonModel.getAnchor())) <= epsilonModel.getRadius()){
            for (Vertex vertex: enemyModel.getVertices()){
                if (vertex.isCollectedByEpsilon()) {
                    epsilonModel.gotHit(enemyModel.getAttackHp());
                    impact(epsilonModel, enemyModel);
//                    Point2D deltaS = impactPoint(epsilonModel.getAnchor(), enemyModel.getAnchor());
//                    epsilonModel.move((int) deltaS.getX(), (int) deltaS.getY());
                    break;
                }
            }

            //vertex of epsilon hit enemy
            Area enemyA = new Area(enemyModel.getPolygon());
            for (Vertex epsilonV : epsilonModel.getVertices()){
                if (enemyA.contains(epsilonV.getX(), epsilonV.getY())){
                    enemyModel.gotHit(epsilonModel.getAttackHp());
                    impact(epsilonModel, enemyModel);
                    break;
                }
            }
        }
    }
}
