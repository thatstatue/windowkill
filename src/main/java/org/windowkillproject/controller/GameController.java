package org.windowkillproject.controller;

import org.windowkillproject.application.Config;
import org.windowkillproject.application.listeners.ShotgunMouseListener;
import org.windowkillproject.model.abilities.BulletModel;
import org.windowkillproject.model.entities.EntityModel;
import org.windowkillproject.model.entities.EpsilonModel;
import org.windowkillproject.model.abilities.Vertex;
import org.windowkillproject.model.entities.enemies.EnemyModel;
import org.windowkillproject.model.entities.enemies.SquarantineModel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static org.windowkillproject.application.Application.gameFrame;
import static org.windowkillproject.application.Config.BANISH_DURATION;
import static org.windowkillproject.application.Config.IMPACT_DURATION;
import static org.windowkillproject.application.panels.ShopPanel.*;
import static org.windowkillproject.controller.ElapsedTime.getTotalSeconds;
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
    public static void impact(BulletModel bulletModel) {
        for (EntityModel entity : entityModels) {
            Point2D bulletPoint = new Point2D.Double( bulletModel.getX(), bulletModel.getY());
                Point2D deltaS = impactPoint(entity.getAnchor(),bulletPoint);
                if(!(deltaS.getY()<1 && deltaS.getX()<1)) {
                    Timer impactTimer = getImpactTimer(entity, deltaS,IMPACT_DURATION);
                    impactTimer.start();
                }
        }
    }

    public static void impact(EntityModel entityModel, EnemyModel enemyModel) {
        //control overlapping
        Point2D closestPointOfEnemy = closestPointOnPolygon(entityModel.getAnchor() , enemyModel.getPointVertices());
        Point2D delta;
//        if (entityModel instanceof EnemyModel){
//            EnemyModel entity = (EnemyModel) entityModel;
//            Point2D closestPointOfEntity = closestPointOnPolygon(enemyModel.getAnchor() , entity.getPointVertices());
//            delta =  weighedVector(unitVector(closestPointOfEntity, closestPointOfEnemy),
//                    closestPointOfEnemy.distance(closestPointOfEntity)/2.0);
//            if (magnitude(delta)<5) delta = weighedVector(delta, enemyModel.getRadius()*1.2);
//        }else{
            delta = weighedVector(unitVector(entityModel.getAnchor(), closestPointOfEnemy),
                    entityModel.getRadius()-closestPointOfEnemy.distance(entityModel.getAnchor())/2.0);

//        }
        entityModel.move((int) delta.getX(), (int) delta.getY());
        enemyModel.move((int) -delta.getX(), (int) -delta.getY());

        // rEaLiStiC impact on the colliding entities
        Point2D p1 = enemyModel.getRoutePoint();
        Point2D p2 = entityModel.getRoutePoint();

        if ((p1.getX()*p2.getX() >0)|| (p1.getY()*p2.getY() >0)){

            if (magnitude(p1)>magnitude(p2)) {
                Point2D smallerP = inverse(p2);
                p2 = p1;
                p1 = smallerP;
            }else {
                Point2D smallerP = inverse(p1);
                p1 = p2;
                p2 = smallerP;
            }
        }
//        if (magnitude(p1)>magnitude(p2)) {
//            p2 = smallerP;
//        }else{
//            p1 = smallerP;
//        }
        //todo: epsilon in bounds
        double s1 = 1, s2=1;
        if (magnitude(p1)<5) s1 = 2.5;
        if (magnitude(p2)<5) s2 = 2.5;
        if (entityModel instanceof EpsilonModel) s2 = 3;

        Timer impactTimer1 = getImpactTimer(entityModel, weighedVector(p1, s1),IMPACT_DURATION);
        Timer impactTimer2 = getImpactTimer(enemyModel, weighedVector(p2, s2),IMPACT_DURATION);
        impactTimer1.start();
        impactTimer2.start();
        //wave of collision

        for (EntityModel entity : entityModels) {
            if (!(entity.equals(entityModel) || entity.equals(enemyModel))) {
                Point2D deltaS = impactPoint(entity.getAnchor(), closestPointOfEnemy);
                if(!(deltaS.getY()<1 && deltaS.getX()<1)) {
                    Timer impactTimer = getImpactTimer(entity, deltaS,IMPACT_DURATION);
                    impactTimer.start();
                }
            }
        }
    }

    private static Timer getImpactTimer(EntityModel entityModel, Point2D deltaS, int t) {
        AtomicInteger count = new AtomicInteger();
        Timer impactTimer = new Timer(Config.FPS/5, null);
        impactTimer.addActionListener(e -> {
            if (count.get() <t) {
                entityModel.setImpact(true);
                entityModel.move((int) deltaS.getX(), (int) deltaS.getY());
                if (entityModel instanceof EpsilonModel){
                    keepEpsilonInBounds();
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
    public static void keepEpsilonInBounds(){
        var epsilonModel = EpsilonModel.getINSTANCE();
        int endX = epsilonModel.getWidth() + epsilonModel.getX() + 5+ epsilonModel.getRadius();
        int endY = epsilonModel.getHeight() + epsilonModel.getY() + 3* epsilonModel.getRadius();
        if (endY > gameFrame.getHeight()) {
            int deltaY = gameFrame.getHeight() - endY;
            epsilonModel.move(0, deltaY);
        }
        if (endX > gameFrame.getWidth()) {
            int deltaX = gameFrame.getWidth() - endX;
            epsilonModel.move(deltaX,0);
        }
    }
    public static void epsilonIntersectionControl(){
        EpsilonModel epsilonModel = EpsilonModel.getINSTANCE();
        ArrayList<EnemyModel> enemies = getEnemies();
        for (EnemyModel enemyModel : enemies) {

            //vertex of enemy hit epsilon
            Point2D d = Utils.closestPointOnPolygon(
                    epsilonModel.getAnchor(), enemyModel.getPointVertices());
            if(Math.abs(d.distance(epsilonModel.getAnchor())) <= epsilonModel.getRadius()) {
                for (Vertex vertex : enemyModel.getVertices()) {
                    if (vertex.isCollectedByEpsilon()) {
                        epsilonModel.gotHit(enemyModel.getAttackHp());
                        break;
                    }
                }
                impact(epsilonModel, enemyModel);
                break;
                //  }
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
    public static void specialtiesControl(){
        if (banish.isOn()){
            for (EntityModel entity : entityModels) {
                    Point2D deltaS = impactPoint(entity.getAnchor(), EpsilonModel.getINSTANCE().getAnchor());
                    deltaS = weighedVector(deltaS, 4);
                    if(!(deltaS.getY()<1 && deltaS.getX()<1)) {
                        Timer impactTimer = getImpactTimer(entity, deltaS, BANISH_DURATION);
                        impactTimer.start();
                    }
            }
            banish.setOn(false);

        }
        if (empower.isOn()){
            ShotgunMouseListener.empowerInitSeconds = getTotalSeconds();
            empower.setOn(false);
        }
        if (heal.isOn()){
            var epsilon = EpsilonModel.getINSTANCE();
            epsilon.setHp(epsilon.getHp()+10);
            heal.setOn(false);
            heal.setPurchased(false);
        }
    }
}
