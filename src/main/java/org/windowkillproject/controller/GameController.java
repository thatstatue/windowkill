package org.windowkillproject.controller;

import org.windowkillproject.application.Config;
import org.windowkillproject.application.listeners.ShotgunMouseListener;
import org.windowkillproject.application.panels.game.GamePanel;
import org.windowkillproject.model.Transferable;
import org.windowkillproject.model.Writ;
import org.windowkillproject.model.abilities.BulletModel;
import org.windowkillproject.model.abilities.Projectable;
import org.windowkillproject.model.entities.Circular;
import org.windowkillproject.model.entities.EntityModel;
import org.windowkillproject.model.entities.EpsilonModel;
import org.windowkillproject.model.abilities.VertexModel;
import org.windowkillproject.model.entities.enemies.*;
import org.windowkillproject.model.entities.enemies.attackstypes.Dashable;
import org.windowkillproject.model.entities.enemies.attackstypes.Hovering;
import org.windowkillproject.model.abilities.MomentModel;
import org.windowkillproject.model.entities.enemies.attackstypes.NonRotatable;
import org.windowkillproject.model.entities.enemies.attackstypes.Unmovable;
import org.windowkillproject.model.entities.enemies.minibosses.BlackOrbModel;
import org.windowkillproject.model.entities.enemies.normals.ArchmireModel;
import org.windowkillproject.model.entities.enemies.normals.WyrmModel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.windowkillproject.application.Application.getGameFrame;
import static org.windowkillproject.application.Config.*;
import static org.windowkillproject.application.Config.BULLET_ATTACK_HP;
import static org.windowkillproject.application.panels.game.GamePanel.gamePanels;
import static org.windowkillproject.application.panels.game.GamePanel.gamePanelsBounds;
import static org.windowkillproject.application.panels.shop.ShopPanel.*;
import static org.windowkillproject.controller.ElapsedTime.getTotalSeconds;
import static org.windowkillproject.controller.ElapsedTime.secondsPassed;
import static org.windowkillproject.controller.Utils.*;
import static org.windowkillproject.model.abilities.BulletModel.bulletModels;
import static org.windowkillproject.model.abilities.CollectableModel.collectableModels;
import static org.windowkillproject.model.entities.EntityModel.entityModels;
import static org.windowkillproject.model.entities.enemies.normals.ArchmireModel.archmireModels;
import static org.windowkillproject.model.entities.enemies.attackstypes.AoEAttacker.MOMENT_MODELS;
import static org.windowkillproject.model.entities.enemies.attackstypes.LaserOperator.LASER_LINES;


public abstract class GameController {
    public static Random random = new Random();

    public static ArrayList<EnemyModel> getEnemies() {
        ArrayList<EnemyModel> enemies = new ArrayList<>();
        for (int i = 0; i < entityModels.size(); i++) {
            EntityModel entityModel = entityModels.get(i);
            if (entityModel instanceof EnemyModel) {
                enemies.add((EnemyModel) entityModel);
            }
        }
        return enemies;
    }

    public static void impact(Projectable projectable) {
        for (int i = 0; i < entityModels.size(); i++) {
            EntityModel entity = entityModels.get(i);
            Point2D bulletPoint = new Point2D.Double(projectable.getX(), projectable.getY());
            if (entity.getAnchor()!=null && !(entity instanceof Hovering)) {
                Point2D deltaS = impactPoint(entity.getAnchor(), bulletPoint);
                if (!(deltaS.getY() < 1 && deltaS.getX() < 1)) {
                    Timer impactTimer = getImpactTimer(entity, deltaS, IMPACT_DURATION);
                    if (!(entity instanceof Unmovable)) impactTimer.start();
                }
            }
        }
    }

    public static void impact(EntityModel entityModel, EnemyModel enemyModel) {
        Point2D collisionPoint = getCollisionPoint(entityModel, enemyModel);

        //impact wave of collision
        Timer impactsTimer = getImpactsTimer(entityModels, entityModel, enemyModel, collisionPoint, IMPACT_DURATION);
        impactsTimer.start();
    }

    private static boolean isCollideAffected(EntityModel entityModel) {
        return !(entityModel instanceof Hovering) && !(entityModel instanceof Unmovable);
    }

    private static boolean isOverlappingDetected(EntityModel entityModel, EnemyModel enemyModel) {
        return !(entityModel instanceof Hovering || enemyModel instanceof Hovering);
    }

    private static Point2D getCollisionPoint(EntityModel entityModel, EnemyModel enemyModel) {
        //control overlapping
        Point2D closestPointOfEnemy = closestPointOnPolygon(entityModel.getAnchor(), enemyModel.getPointVertices());
        Point2D delta;
        if (closestPointOfEnemy == null) {
            delta = weighedVector(unitVector(entityModel.getAnchor(), enemyModel.getAnchor()),
                    entityModel.getRadius());

        } else {
            delta = weighedVector(unitVector(entityModel.getAnchor(), closestPointOfEnemy),
                    entityModel.getRadius() - closestPointOfEnemy.distance(entityModel.getAnchor()) / 2.0);
        }
        if (isOverlappingDetected(entityModel, enemyModel)) {
//
            if (!(entityModel instanceof Unmovable)) entityModel.move((int) delta.getX(), (int) delta.getY());
            if (!(enemyModel instanceof Unmovable)) enemyModel.move((int) -delta.getX(), (int) -delta.getY());
        }

        // rEaLiStiC impact on the colliding entities
        Point2D p1 = enemyModel.getRoutePoint();
        Point2D p2 = entityModel.getRoutePoint();
        if (p1 != null && p2 != null) {

            if (isOverlappingDetected(entityModel, enemyModel)) {
                collidedEntitiesImpact(entityModel, enemyModel, p1, p2);

                //torque
                if (closestPointOfEnemy != null) {
                    torqueOfCollision(entityModel, enemyModel, closestPointOfEnemy);
                }
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
        if (!(entityModel instanceof NonRotatable))
            entityModel.setTheta(rotateI(entityModel.getAnchor(), forceOfEnemy) * 6 * UNIT_DEGREE);
        if (!(enemyModel instanceof NonRotatable))
            enemyModel.setTheta(rotateI(enemyModel.getAnchor(), forceOfEntity) * 6 * UNIT_DEGREE);
    }

    private static Timer getImpactTimer(EntityModel entityModel, Point2D deltaS, int t) {
        AtomicInteger count = new AtomicInteger();
        Timer impactTimer = new Timer(Config.FPS / 5, null);
        impactTimer.addActionListener(e -> {
            if (count.get() < t) {
                entityModel.setImpact(true);
                entityModel.move((int) deltaS.getX(), (int) deltaS.getY());
//                keepEpsilonInBounds();//todo prob
                count.getAndIncrement();
            } else {
//                keepEpsilonInBounds();
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
                        if (entityModel.getAnchor()!= null && closestPointOfEnemy!= null &&
                                !(entityModel instanceof Hovering)) {
                            Point2D deltaS = impactPoint(entityModel.getAnchor(), closestPointOfEnemy);
                            entityModel.move((int) deltaS.getX(), (int) deltaS.getY());
                        }
                    }
                }
//                keepEpsilonInBounds();todo prob
                count.getAndIncrement();
            } else {
                for (EntityModel entityModel : entityModels) {
                    if (isImpactAffected(not1, not2, entityModel)) {
                        entityModel.setImpact(false);
                    }
                }
//                keepEpsilonInBounds();
                impactTimer.stop();
            }
        });
        return impactTimer;
    }

    private static boolean isImpactAffected(EntityModel not1, EntityModel not2, EntityModel entityModel) {
        return !(entityModel.equals(not1) || entityModel.equals(not2) || entityModel instanceof Unmovable || entityModel instanceof Hovering);
    }

    public static void enemyIntersectionControl() {
        ArrayList<EnemyModel> enemies = getEnemies();
        for (int i = 0; i < enemies.size(); i++) {
            var enemyModel = enemies.get(i);
            Polygon p1 = enemyModel.getPolygon();
            if (p1 != null) {
                Area a1 = new Area(p1);
                boolean tempCollision = false;
                //impact controller
                for (int j = i + 1; j < enemies.size(); j++) {
                    var collidedEnemy = enemies.get(j);
                    Polygon p2 = collidedEnemy.getPolygon();
                    if (p2 != null) {
                        Area a2 = new Area(p2);
                        a2.intersect(a1);
                        if (!a2.isEmpty()) {
                            impact(enemyModel, collidedEnemy);
                            tempCollision = true;
                        }
                    }
                    if (enemyModel instanceof Circular && !
                            (enemyModel instanceof Hovering
                            || collidedEnemy instanceof Hovering
                            || collidedEnemy instanceof Circular) &&
                            enemyModel.getAnchor().distance(
                                    closestPointOnPolygon(enemyModel.getAnchor(),
                                    collidedEnemy.getPointVertices()))
                                    < enemyModel.getRadius()) {
                        impact(enemyModel, collidedEnemy);
                        tempCollision = true;
                    }
                }
                //dash controller
                if (enemyModel instanceof Dashable) {
                    ((Dashable) enemyModel).setCollision(tempCollision);
                }
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

    private static void setTransferableBoundsAllowed() {
        for (int j = 0; j < gamePanels.size(); j++) {
            gamePanels.get(j).resetCanShrink();
        }
        setEntitiesBoundsAllowed();
        setBulletsBoundsAllowed();
    }

    private static void setEntitiesBoundsAllowed() {
        for (int i = 0; i < entityModels.size(); i++) {
            EntityModel entityModel = entityModels.get(i);
            int t = 0;
            entityModel.setAllowedArea(new Area());
            entityModel.setAllowedPanels(new ArrayList<>());
            for (int j = 0; j < gamePanels.size(); j++) {
                GamePanel gamePanel = gamePanels.get(j);
                var rectangle = gamePanelsBounds.get(gamePanel);
                if (isTransferableInBounds(entityModel, rectangle, true)) {
                    entityModel.addToAllowedArea(gamePanel);
                    if (entityModel instanceof EpsilonModel && t > 0)
                        checkShrinking(entityModel, rectangle, gamePanel);
                    t++;
                }
            }
        }
    }

    private static void setBulletsBoundsAllowed() {
        for (int i = 0; i < bulletModels.size(); i++) {
            BulletModel bulletModel = bulletModels.get(i);
            int t = 0;
            bulletModel.setAllowedArea(new Area());
            bulletModel.setAllowedPanels(new ArrayList<>());
            for (int j = 0; j < gamePanels.size(); j++) {
                GamePanel gamePanel = gamePanels.get(j);
                var rectangle = gamePanelsBounds.get(gamePanel);
                if (isTransferableInBounds(bulletModel, rectangle, true)) {
                    bulletModel.addToAllowedArea(gamePanel);
                    if (t>0) checkShrinking(bulletModel, rectangle, gamePanel);
                    t++;
                }
            }
        }
    }

    private static void checkShrinking(Transferable transferable, Rectangle rectangle, GamePanel gamePanel) {
        if (transferable.getX() + transferable.getWidth() / 2 >= rectangle.x + rectangle.width / 2) {
            gamePanel.setCanShrinkRight(false);
        }
        if (transferable.getX() + transferable.getWidth() / 2 <= rectangle.x + rectangle.width / 2) {
            gamePanel.setCanShrinkLeft(false);
        }
        if (transferable.getY() + transferable.getHeight() / 2 >= rectangle.y + rectangle.height / 2) {
            gamePanel.setCanShrinkDown(false);
        }
        if (transferable.getY() + +transferable.getHeight() / 2 <= rectangle.y + rectangle.height / 2) {
            gamePanel.setCanShrinkUp(false);
        }
    }

    private static int lastAoEAttack;

    public static void areaOfEffectControl() {
        for (int i = 0; i < MOMENT_MODELS.size(); i++) {
            MomentModel momentModel = MOMENT_MODELS.get(i);
            if (ElapsedTime.getTotalSeconds() > momentModel.getTime() + 5) MOMENT_MODELS.remove(momentModel);
        }
        //timeout
        if (ElapsedTime.getTotalSeconds() - lastAoEAttack > AOE_TIMEOUT) {
            lastAoEAttack = ElapsedTime.getTotalSeconds();
            for (int j = 0; j < entityModels.size(); j++) {
                EntityModel entityModel = entityModels.get(j);
                //Drown
                for (int k = 0; k < archmireModels.size(); k++) {
                    ArchmireModel archmireModel = archmireModels.get(k);
                    if (archmireModel.drown(entityModel)) break;
                }

                //AOE
                for (int i = 0; i < MOMENT_MODELS.size(); i++) {
                    MomentModel momentModel = MOMENT_MODELS.get(i);
                    if (entityModel.getAnchor().distance(momentModel.getCenterOfArea()) < momentModel.getRadius()) {
                        entityModel.gotHit(momentModel.getAttackHP());
                        break;
                    }
                }
                //LASER
                for (int i = 0; i < LASER_LINES.size(); i++) {
                    var laserPoint = LASER_LINES.get(i);
                    if (!(entityModel instanceof BlackOrbModel) &&
                            entityModel.getAnchor().distance(laserPoint) < LASER_RADIUS) {
                        entityModel.gotHit(LASER_ATTACK_HP);
                        break;
                    }
                }
            }
        }
    }

    public static GamePanelCorner getClosestPanelCorner(Point2D point2D) {
        final GamePanel[] closestPanel = {gamePanels.getFirst()};
        final Point2D[] closestPoint = {new Point2D.Double(10000, 10000)};
        final int[] indexOfCorner = {0};

        gamePanelsBounds.forEach((panel, rectangle) -> {
            ArrayList<Point2D> corners = new ArrayList<>();
            corners.add(new Point2D.Double(rectangle.x, rectangle.y)); //tl 0
            corners.add(new Point2D.Double(rectangle.x + rectangle.width, rectangle.y));//tr 1
            corners.add(new Point2D.Double(rectangle.x, rectangle.y + rectangle.height)); //bl 2
            corners.add(new Point2D.Double(rectangle.x + rectangle.width, rectangle.y + rectangle.height));//br 3
            for (int i = 0; i < corners.size(); i++) {
                if (point2D.distance(corners.get(i)) < point2D.distance(closestPoint[0])) {
                    closestPoint[0] = corners.get(i);
                    indexOfCorner[0] = i;
                    closestPanel[0] = panel;
                }
            }
        });
        int code = DOWN_CODE;
        switch (indexOfCorner[0]) {
            case 0 -> {
                code = UP_CODE;
                if (Math.abs(point2D.getX() - closestPoint[0].getX()) <
                        Math.abs(point2D.getY() - closestPoint[0].getY())) code = LEFT_CODE;
            }
            case 1 -> {
                code = UP_CODE;
                if (Math.abs(point2D.getX() - closestPoint[0].getX()) <
                        Math.abs(point2D.getY() - closestPoint[0].getY())) code = RIGHT_CODE;
            }
            case 2 -> {
                if (Math.abs(point2D.getX() - closestPoint[0].getX()) <
                        Math.abs(point2D.getY() - closestPoint[0].getY())) code = LEFT_CODE;
            }
            case 3 -> {
                if (Math.abs(point2D.getX() - closestPoint[0].getX()) <
                        Math.abs(point2D.getY() - closestPoint[0].getY())) code = RIGHT_CODE;
            }
        }
        return new GamePanelCorner(closestPanel[0], code);
    }

    private static void setTransferableLocalPanel(Transferable transferable) {
        AtomicInteger t = new AtomicInteger();
        gamePanelsBounds.forEach((gamePanel, rectangle) -> {
            if (isTransferableInBounds(transferable, rectangle, false)) {
                t.getAndIncrement();
                transferable.setLocalPanel(gamePanel);
            }
        });
        if (t.get() > 1) transferable.setLocalPanel(null);
    }

    public static void keepTransferableInBounds() {
        setTransferableBoundsAllowed();

        if (!getGameFrame().isExploding()) {
            keepEpsilonInBounds();
            keepBulletsInBounds();
        }

    }

    private static void keepEpsilonInBounds() {
        var epsilonModel = EpsilonModel.getINSTANCE();
        setTransferableLocalPanel(epsilonModel);

        Area allowedArea = epsilonModel.getAllowedArea();
        if (!isTransferableInBounds(epsilonModel, allowedArea, false)) {
            if (epsilonModel.getLocalPanel() == null) {
//                    keepInPanel(epsilonModel.getAllowedPanels().get(0));todo cant shrink local
            } else {
                keepInPanel(epsilonModel.getLocalPanel());
            }
        }
        getGameFrame().setHpAmount(epsilonModel.getHp());
    }

    private static void keepBulletsInBounds() {
        for (int i = 0; i < bulletModels.size(); i++) {
            BulletModel bulletModel = bulletModels.get(i);
            setTransferableLocalPanel(bulletModel);
            Area bulletAllowedArea = bulletModel.getAllowedArea();
            if (!isTransferableInBounds(bulletModel, bulletAllowedArea, false)) {
                if (bulletModel.getLocalPanel() == null) {
//                    keepInPanel(bulletModel.getAllowedPanels().get(0));todo
                } else {
                    keepInPanel(bulletModel.getLocalPanel());
                }
            }
        }
    }


    public static void keepInPanel(GamePanel panel) {
        var epsilonModel = EpsilonModel.getINSTANCE();
        int endX = epsilonModel.getXO() + epsilonModel.getRadius();
        int endY = epsilonModel.getYO() + epsilonModel.getRadius();

        var localPanelBounds = gamePanelsBounds.get(panel);
        int localPanelX = localPanelBounds.x;
        int localPanelY = localPanelBounds.y;
        int endOfLocalPanelY = localPanelY + localPanelBounds.height;
        int endOfLocalPanelX = localPanelX + localPanelBounds.width;

        if (endY > endOfLocalPanelY) {
            int deltaY = endOfLocalPanelY - endY;
            epsilonModel.move(0, deltaY);
        }
        if (endX > endOfLocalPanelX) {
            int deltaX = endOfLocalPanelX - endX;
            epsilonModel.move(deltaX, 0);
        }
        if (epsilonModel.getY() < localPanelY) {
            int deltaY = localPanelY - epsilonModel.getY();
            epsilonModel.move(0, deltaY);
        }
        if (epsilonModel.getX() < localPanelX) {
            int deltaX = localPanelX - epsilonModel.getX();
            epsilonModel.move(deltaX, 0);
        }
    }

    public static void epsilonIntersectionControl() {
        EpsilonModel epsilonModel = EpsilonModel.getINSTANCE();
        ArrayList<EnemyModel> enemies = getEnemies();
        for (EnemyModel enemyModel : enemies) {

            //vertex of epsilon hit enemy
            if (enemyModel.getPolygon() != null) {
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
                    impact(epsilonModel, enemyModel);
                    break;
                }
            }
            if (enemyModel instanceof Circular &&
                    enemyModel.getAnchor().distance(epsilonModel.getAnchor()) <
                            enemyModel.getRadius() + epsilonModel.getRadius()) {
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
