package org.windowkillproject.controller;

import org.windowkillproject.application.Config;
import org.windowkillproject.application.listeners.ShotgunMouseListener;
import org.windowkillproject.application.panels.game.GamePanel;
import org.windowkillproject.model.Writ;
import org.windowkillproject.model.abilities.BulletModel;
import org.windowkillproject.model.abilities.Projectable;
import org.windowkillproject.model.entities.EntityModel;
import org.windowkillproject.model.entities.EpsilonModel;
import org.windowkillproject.model.abilities.VertexModel;
import org.windowkillproject.model.entities.enemies.*;

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
import static org.windowkillproject.application.panels.game.GamePanel.gamePanels;
import static org.windowkillproject.application.panels.shop.ShopPanel.*;
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

    public static void impact(Projectable projectable) {
        for (EntityModel entity : entityModels) {
            Point2D bulletPoint = new Point2D.Double(projectable.getX(), projectable.getY());
            Point2D deltaS = impactPoint(entity.getAnchor(), bulletPoint);
            if (!(deltaS.getY() < 1 && deltaS.getX() < 1)) {
                Timer impactTimer = getImpactTimer(entity, deltaS, IMPACT_DURATION);
                impactTimer.start();
            }
        }
    }

    public static void impact(EntityModel entityModel, EnemyModel enemyModel) {
        Point2D collisionPoint = getCollisionPoint(entityModel, enemyModel);

        //impact wave of collision
        Timer impactsTimer = getImpactsTimer(entityModels, entityModel, enemyModel, collisionPoint, IMPACT_DURATION);
        impactsTimer.start();
    }
    private static boolean isCollideAffected(EntityModel entityModel){
        return !(entityModel instanceof Hovering) && !(entityModel instanceof Unmovable);
    }
    private static boolean isOverlappingDetected(EntityModel entityModel, EnemyModel enemyModel){
        return !(entityModel instanceof Hovering) && !(enemyModel instanceof Hovering);
    }
    private static Point2D getCollisionPoint(EntityModel entityModel, EnemyModel enemyModel) {
        //control overlapping
        Point2D closestPointOfEnemy = closestPointOnPolygon(entityModel.getAnchor(), enemyModel.getPointVertices());
        Point2D delta;
        delta = weighedVector(unitVector(entityModel.getAnchor(), closestPointOfEnemy),
                entityModel.getRadius() - closestPointOfEnemy.distance(entityModel.getAnchor()) / 2.0);

        if (isOverlappingDetected(entityModel,enemyModel)) {
            entityModel.move((int) delta.getX(), (int) delta.getY());
            enemyModel.move((int) -delta.getX(), (int) -delta.getY());
        }

        // rEaLiStiC impact on the colliding entities
        Point2D p1 = enemyModel.getRoutePoint();
        Point2D p2 = entityModel.getRoutePoint();
        if (p1!= null && p2!= null) {
            collidedEntitiesImpact(entityModel, enemyModel, p1, p2);

            //torque
            if (isOverlappingDetected(entityModel, enemyModel)) {
                torqueOfCollision(entityModel, enemyModel, closestPointOfEnemy);
            }
        }
        return closestPointOfEnemy;
    }

    private static void collidedEntitiesImpact(EntityModel entityModel, EnemyModel enemyModel, Point2D p1, Point2D p2) {
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
        double s1 = 1, s2 = 1;
        if (magnitude(p1) < 5) s1 = 2.5;
        if (magnitude(p2) < 5) s2 = 2.5;

        Timer impactTimer1 = getImpactTimer(entityModel, weighedVector(p1, s1), IMPACT_DURATION);
        Timer impactTimer2 = getImpactTimer(enemyModel, weighedVector(p2, s2), IMPACT_DURATION);
        if (isCollideAffected(entityModel)) impactTimer1.start();
        if (isCollideAffected(enemyModel)) impactTimer2.start();
        if (entityModel instanceof WyrmModel) ((WyrmModel) entityModel).setMinusRotationSpeed();
        if (enemyModel instanceof WyrmModel) ((WyrmModel) enemyModel).setMinusRotationSpeed();

    }

    private static void torqueOfCollision(EntityModel entityModel, EnemyModel enemyModel, Point2D closestPointOfEnemy) {
        Point2D forceOfEntity = calculateForce(
                unitVector(closestPointOfEnemy, entityModel.getAnchor()),
                rotateI(closestPointOfEnemy, entityModel.getRoutePoint()));
        Point2D forceOfEnemy = calculateForce(
                unitVector(closestPointOfEnemy, enemyModel.getAnchor()),
                rotateI(closestPointOfEnemy, enemyModel.getRoutePoint()));
        entityModel.setTheta(rotateI(entityModel.getAnchor(), forceOfEnemy) * 6 * UNIT_DEGREE);
        enemyModel.setTheta(rotateI(enemyModel.getAnchor(), forceOfEntity) * 6 * UNIT_DEGREE);
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

    private static Timer getImpactsTimer(ArrayList<EntityModel> entityModels, EntityModel not1, EntityModel not2, Point2D closestPointOfEnemy, int t) {
        AtomicInteger count = new AtomicInteger();
        Timer impactTimer = new Timer(Config.FPS / 5, null);
        impactTimer.addActionListener(e -> {
            if (count.get() < t) {
                for (EntityModel entityModel : entityModels) {
                    if (isImpactAffected(not1, not2, entityModel)) {
                        entityModel.setImpact(true);
                        Point2D deltaS = impactPoint(entityModel.getAnchor(), closestPointOfEnemy);
                        entityModel.move((int) deltaS.getX(), (int) deltaS.getY());
                    }
                }
                keepEpsilonInBounds();
                count.getAndIncrement();
            } else {
                for (EntityModel entityModel : entityModels) {
                    if (isImpactAffected(not1, not2, entityModel)) {
                        entityModel.setImpact(false);
                    }
                }
                keepEpsilonInBounds();
                impactTimer.stop();
            }
        });
        return impactTimer;
    }

    private static boolean isImpactAffected(EntityModel not1, EntityModel not2, EntityModel entityModel) {
        return !(entityModel.equals(not1) || entityModel.equals(not2) || entityModel instanceof Unmovable);
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
                if (enemyModel instanceof WyrmModel &&
                        enemyModel.getAnchor().distance(closestPointOnPolygon(enemyModel.getAnchor(),
                                collidedEnemy.getPointVertices()))< enemyModel.getRadius()){
                    impact(enemyModel,collidedEnemy);
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
    public static void setEntitiesBoundsAllowed(){
        for (int i = 0; i < entityModels.size(); i++) {
            EntityModel entityModel = entityModels.get(i);
            int t = 0;
            for (int j = 0; j < gamePanels.size(); j++) {
                GamePanel gamePanel = gamePanels.get(j);
                if (entityPartlyInBounds(entityModel, gamePanel)) {
                    entityModel.addToAllowedArea(gamePanel);
                    t++;
                }
            }
//            if (t>1) entityModel.setLocalPanel(null);
        }
    }

    public static void keepEpsilonInBounds() {
        if (!getGameFrame().isExploding()) {
            var epsilonModel = EpsilonModel.getINSTANCE();
            int endX = epsilonModel.getXO() + epsilonModel.getRadius();
            int endY = epsilonModel.getYO() + epsilonModel.getRadius();
//            System.out.println("eps is "+ endX + " "+ endY);

            int localPanelX=epsilonModel.getLocalPanel().getX();
            int localPanelY = epsilonModel.getLocalPanel().getY();
            int endOfLocalPanelY= localPanelY+ epsilonModel.getLocalPanel().getHeight();
            int endOfLocalPanelX = localPanelX+ epsilonModel.getLocalPanel().getWidth();

            System.out.println("epsilon x is allowed from "+ localPanelX+ " to " +endOfLocalPanelX);
//            System.out.println(" should be allowed from "+ gamePanels.get(1).getX()+ " to " +gamePanels.get(1).getX()+gamePanels.get(1).getWidth());

            if (endY > endOfLocalPanelY) {
                int deltaY = endOfLocalPanelY - endY;
                epsilonModel.move(0, deltaY);
            }
            if (endX > endOfLocalPanelX) {
                int deltaX = endOfLocalPanelX - endX;
                epsilonModel.move(deltaX, 0);
            }
            if (epsilonModel.getY() < localPanelY) {
                int deltaY = localPanelY-epsilonModel.getY();
                epsilonModel.move(0, deltaY);
            }
            if (epsilonModel.getX() < localPanelX) {
                int deltaX = localPanelX-epsilonModel.getX();
                epsilonModel.move(deltaX, 0);
            }
            getGameFrame().setHpAmount(epsilonModel.getHp());
        }

    }

    public static void epsilonIntersectionControl() {
        EpsilonModel epsilonModel = EpsilonModel.getINSTANCE();
        ArrayList<EnemyModel> enemies = getEnemies();
        for (EnemyModel enemyModel : enemies) {

            //vertex of epsilon hit enemy
            Area enemyA = new Area(enemyModel.getPolygon());
            for (VertexModel epsilonV : epsilonModel.getVertices()) {
                if (enemyA.contains(epsilonV.getX(), epsilonV.getY())) {
                    enemyModel.gotHit(epsilonModel.getMeleeAttackHp());
                    impact(epsilonModel, enemyModel);
                    break;
                }
            }

            //vertex of enemy hit epsilon
            Point2D d = Utils.closestPointOnPolygon(
                    epsilonModel.getAnchor(), enemyModel.getPointVertices());
            if (Math.abs(d.distance(epsilonModel.getAnchor())) <= epsilonModel.getRadius()) {
                for (VertexModel vertexModel : enemyModel.getVertices()) {
                    if (vertexModel.isCollectedByEpsilon()) {
                        epsilonModel.gotHit(enemyModel.getMeleeAttackHp());
                        break;
                    }
                }
                System.out.println("impact start on epsilon and "+ enemyModel +"\n with: "+ enemyModel.getAnchor());
                impact(epsilonModel, enemyModel);
                break;
            }
            if (enemyModel instanceof WyrmModel &&
                    enemyModel.getAnchor().distance(epsilonModel.getAnchor())<
                            enemyModel.getRadius()+epsilonModel.getRadius()){
                impact(epsilonModel, enemyModel);
            }

        }
    }

    public static void writControl() {
        long now = getTotalSeconds();
        if (Writ.getInitSeconds() > 0 && now - Writ.getInitSeconds() <= WRIT_DURATION) {
            switch (Writ.getChosenSkill()) {
                case Ares -> {
                    BulletModel.setAttackHp(BULLET_ATTACK_HP + 2);
                }
                case Aceso -> {
                    if (now - Writ.getInitSeconds() >= Writ.getTimes() && Writ.getTimes() < 10) {
                        EpsilonModel.getINSTANCE().setHp(EpsilonModel.getINSTANCE().getHp() + 1);
                        Writ.timesAddIncrement();
                    }
                }
                case Proteus -> {
                    if (Writ.getTimes() < Writ.getAcceptedClicks()) {
                        EpsilonModel.getINSTANCE().spawnVertex();
                        Writ.timesAddIncrement();
                    }
                }
            }
        } else {
            BulletModel.setAttackHp(BULLET_ATTACK_HP + 2);
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
