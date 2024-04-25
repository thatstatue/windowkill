package org.windowkillproject.controller;

import org.windowkillproject.application.Config;
import org.windowkillproject.application.listeners.ShotgunMouseListener;
import org.windowkillproject.model.Writ;
import org.windowkillproject.model.abilities.BulletModel;
import org.windowkillproject.model.entities.EntityModel;
import org.windowkillproject.model.entities.EpsilonModel;
import org.windowkillproject.model.abilities.VertexModel;
import org.windowkillproject.model.entities.enemies.EnemyModel;
import org.windowkillproject.model.entities.enemies.SquarantineModel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static org.windowkillproject.application.Application.getGameFrame;
import static org.windowkillproject.application.Config.*;
import static org.windowkillproject.application.Config.BULLET_ATTACK_HP;
import static org.windowkillproject.application.panels.ShopPanel.*;
import static org.windowkillproject.controller.ElapsedTime.getTotalSeconds;
import static org.windowkillproject.controller.ElapsedTime.secondsPassed;
import static org.windowkillproject.controller.Utils.*;
import static org.windowkillproject.model.abilities.CollectableModel.collectableModels;
import static org.windowkillproject.model.entities.EntityModel.entityModels;


public abstract class GameController {
    public static Random random = new Random();

    public static ArrayList<EnemyModel> getEnemies() {
        ArrayList<EnemyModel> enemies = new ArrayList<>();
        for (EntityModel entityModel : entityModels) {
            if (entityModel instanceof EnemyModel) {
                enemies.add((EnemyModel) entityModel);
            }
        }
        return enemies;
    }

    public static void impact(BulletModel bulletModel) {
        for (EntityModel entity : entityModels) {
            Point2D bulletPoint = new Point2D.Double(bulletModel.getX(), bulletModel.getY());
            Point2D deltaS = impactPoint(entity.getAnchor(), bulletPoint);
            if (!(deltaS.getY() < 1 && deltaS.getX() < 1)) {
                Timer impactTimer = getImpactTimer(entity, deltaS, IMPACT_DURATION);
                impactTimer.start();
            }
        }
    }

    public static void impact(EntityModel entityModel, EnemyModel enemyModel) {
        //control overlapping
        Point2D closestPointOfEnemy = closestPointOnPolygon(entityModel.getAnchor(), enemyModel.getPointVertices());
        Point2D delta;
//        if (entityModel instanceof EnemyModel){
//            EnemyModel entity = (EnemyModel) entityModel;
//            Point2D closestPointOfEntity = closestPointOnPolygon(enemyModel.getAnchor() , entity.getPointVertices());
//            delta =  weighedVector(unitVector(closestPointOfEntity, closestPointOfEnemy),
//                    closestPointOfEnemy.distance(closestPointOfEntity)/2.0);
//            if (magnitude(delta)<5) delta = weighedVector(delta, enemyModel.getRadius()*1.2);
//        }else{
        delta = weighedVector(unitVector(entityModel.getAnchor(), closestPointOfEnemy),
                entityModel.getRadius() - closestPointOfEnemy.distance(entityModel.getAnchor()) / 2.0);

//        }
        entityModel.move((int) delta.getX(), (int) delta.getY());
        enemyModel.move((int) -delta.getX(), (int) -delta.getY());

        // rEaLiStiC impact on the colliding entities
        Point2D p1 = enemyModel.getRoutePoint();
        Point2D p2 = entityModel.getRoutePoint();

        if ((p1.getX() * p2.getX() > 0) || (p1.getY() * p2.getY() > 0)) {

            if (magnitude(p1) > magnitude(p2)) {
                Point2D smallerP = inverse(p2);
                p2 = p1;
                p1 = smallerP;
            } else {
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
        double s1 = 1, s2 = 1;
        if (magnitude(p1) < 5) s1 = 2.5;
        if (magnitude(p2) < 5) s2 = 2.5;
        if (entityModel instanceof EpsilonModel) s2 = 3;

        Timer impactTimer1 = getImpactTimer(entityModel, weighedVector(p1, s1), IMPACT_DURATION);
        Timer impactTimer2 = getImpactTimer(enemyModel, weighedVector(p2, s2), IMPACT_DURATION);
        impactTimer1.start();
        impactTimer2.start();
        //wave of collision
        Timer impactsTimer = getImpactsTimer(entityModels,entityModel,enemyModel, closestPointOfEnemy, IMPACT_DURATION);
        impactsTimer.start();
    }

    private static Timer getImpactTimer(EntityModel entityModel, Point2D deltaS, int t) {
        AtomicInteger count = new AtomicInteger();
        Timer impactTimer = new Timer(Config.FPS / 5, null);
        impactTimer.addActionListener(e -> {
            if (count.get() < t) {
                entityModel.setImpact(true);
                entityModel.move((int) deltaS.getX(), (int) deltaS.getY());
                keepEpsilonInBounds();
                count.getAndIncrement();
            } else {
                keepEpsilonInBounds();
                entityModel.setImpact(false);
                impactTimer.stop();
            }
        });
        return impactTimer;
    }
    private static Timer getImpactsTimer(ArrayList<EntityModel> entityModels, EntityModel not1, EntityModel not2,Point2D closestPointOfEnemy, int t){
        AtomicInteger count = new AtomicInteger();
        Timer impactTimer = new Timer(Config.FPS / 5, null);
        impactTimer.addActionListener(e -> {
            if (count.get() < t) {
                for (EntityModel entityModel : entityModels){
                    if (!(entityModel.equals(not1)|| entityModel.equals(not2))) {
                        entityModel.setImpact(true);
                        Point2D deltaS = impactPoint(entityModel.getAnchor(), closestPointOfEnemy);
                        entityModel.move((int) deltaS.getX(), (int) deltaS.getY());
                    }
                }
                keepEpsilonInBounds();
                count.getAndIncrement();
            } else {
                for (EntityModel entityModel : entityModels){
                    if (!(entityModel.equals(not1)|| entityModel.equals(not2))) {
                        entityModel.setImpact(false);
                    }
                }
                keepEpsilonInBounds();
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
            if (enemyModel instanceof SquarantineModel) {
                var squarantine = (SquarantineModel) enemyModel;
                squarantine.setCollision(tempCollision);
            }
        }
    }

    public static void epsilonRewardControl() {
        for (int i = 0; i < collectableModels.size(); i++) {
            var collectableModel = collectableModels.get(i);
            if (collectableModel.isCollectedByEpsilon()) {
                EpsilonModel.getINSTANCE().collected(collectableModel.getRewardXp());
                collectableModels.remove(collectableModel);
                collectableModel.destroy();
            }
            if (secondsPassed(collectableModel.getInitSeconds()) >= 10) {
                collectableModels.remove(collectableModel);
                collectableModel.destroy();
            }
        }
    }

    public static void keepEpsilonInBounds() {
        var epsilonModel = EpsilonModel.getINSTANCE();
        int endX =  epsilonModel.getXO() +2*epsilonModel.getRadius();
        int endY = epsilonModel.getHeight() + epsilonModel.getYO() + 2*epsilonModel.getRadius();
        if (endY > getGameFrame().getHeight()) {
            int deltaY = getGameFrame().getHeight() - endY;
            epsilonModel.move(0, deltaY);
        }
        if (endX > getGameFrame().getWidth()) {
            int deltaX = getGameFrame().getWidth() - endX;
            epsilonModel.move(deltaX, 0);
        }
        if (epsilonModel.getY() < 0) {
            int deltaY = - epsilonModel.getY();
            epsilonModel.move(0, deltaY);
        }
        if (epsilonModel.getX() < 0) {
            int deltaX = - epsilonModel.getX();
            epsilonModel.move(deltaX ,0);
        }
        getGameFrame().setHpAmount(epsilonModel.getHp());

    }

    public static void epsilonIntersectionControl() {
        EpsilonModel epsilonModel = EpsilonModel.getINSTANCE();
        ArrayList<EnemyModel> enemies = getEnemies();
        for (EnemyModel enemyModel : enemies) {
            //vertex of enemy hit epsilon
            Point2D d = Utils.closestPointOnPolygon(
                    epsilonModel.getAnchor(), enemyModel.getPointVertices());
            if (Math.abs(d.distance(epsilonModel.getAnchor())) <= epsilonModel.getRadius()) {
                for (VertexModel vertexModel : enemyModel.getVertices()) {
                    if (vertexModel.isCollectedByEpsilon()) {
                        epsilonModel.gotHit(enemyModel.getAttackHp());
                        break;
                    }
                }
                impact(epsilonModel, enemyModel);
                break;
            }

            //vertex of epsilon hit enemy
            Area enemyA = new Area(enemyModel.getPolygon());
            for (VertexModel epsilonV : epsilonModel.getVertices()) {
                if (enemyA.contains(epsilonV.getX(), epsilonV.getY())) {
                    enemyModel.gotHit(epsilonModel.getAttackHp());
                    impact(epsilonModel, enemyModel);
                    break;
                }
            }
        }
    }

    public static void writControl() {
        long now = getTotalSeconds();
        if (Writ.getInitSeconds()>0 && now - Writ.getInitSeconds() <= WRIT_DURATION ) {
            switch (Writ.getChosenSkill()){
                case Ares ->{
                    BulletModel.setAttackHp(BULLET_ATTACK_HP +2);
                }
                case Aceso -> {
                    if (now -  Writ.getInitSeconds()>=Writ.getTimes() && Writ.getTimes()<10){
                        EpsilonModel.getINSTANCE().setHp(EpsilonModel.getINSTANCE().getHp()+1);
                        Writ.timesAddIncrement();
                    }
                }
                case Proteus -> {
                    if (Writ.getTimes()< Writ.getAcceptedClicks()){
                        EpsilonModel.getINSTANCE().spawnVertex();
                        Writ.timesAddIncrement();
                    }
                }
            }
        }else{
            BulletModel.setAttackHp(BULLET_ATTACK_HP +2);
        }
    }

    public static void specialtiesControl() {
        if (banish.isOn()) {
            for (EntityModel entity : entityModels) {
                Point2D deltaS = impactPoint(entity.getAnchor(), EpsilonModel.getINSTANCE().getAnchor());
                deltaS = weighedVector(deltaS, 4);
                if (!(deltaS.getY() < 1 && deltaS.getX() < 1)) {
                    Timer impactTimer = getImpactTimer(entity, deltaS, BANISH_DURATION);
                    impactTimer.start();
                }
            }
            banish.setOn(false);
        }
        if (empower.isOn()) {
            ShotgunMouseListener.empowerInitSeconds = getTotalSeconds();
            empower.setOn(false);
        }
        if (heal.isOn()) {
            var epsilon = EpsilonModel.getINSTANCE();
            epsilon.setHp(epsilon.getHp() + 10);
            heal.setOn(false);
            heal.setPurchased(false);
        }
    }
}
