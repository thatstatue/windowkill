package org.windowkillproject.controller;

import org.windowkillproject.server.model.Transferable;
import org.windowkillproject.server.model.abilities.BulletModel;
import org.windowkillproject.server.model.abilities.MomentModel;
import org.windowkillproject.server.model.abilities.Projectable;
import org.windowkillproject.server.model.abilities.VertexModel;
import org.windowkillproject.server.model.entities.Circular;
import org.windowkillproject.server.model.entities.EntityModel;
import org.windowkillproject.server.model.entities.EpsilonModel;
import org.windowkillproject.server.model.entities.enemies.EnemyModel;
import org.windowkillproject.server.model.entities.enemies.attackstypes.*;
import org.windowkillproject.server.model.entities.enemies.minibosses.BlackOrbModel;
import org.windowkillproject.server.model.entities.enemies.normals.ArchmireModel;
import org.windowkillproject.server.model.entities.enemies.normals.WyrmModel;
import org.windowkillproject.server.model.globe.GlobeModel;
import org.windowkillproject.server.model.panelmodels.PanelModel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static org.windowkillproject.Constants.*;
import static org.windowkillproject.Request.*;
import static org.windowkillproject.server.Config.*;
import static org.windowkillproject.controller.Utils.*;
import static org.windowkillproject.server.model.abilities.BulletModel.bulletModels;
import static org.windowkillproject.server.model.abilities.CollectableModel.collectableModels;
import static org.windowkillproject.server.model.abilities.PortalModel.lastPortal;
import static org.windowkillproject.server.model.entities.EntityModel.entityModels;
import static org.windowkillproject.server.model.entities.enemies.normals.ArchmireModel.archmireModels;
import static org.windowkillproject.server.model.entities.enemies.attackstypes.AoEAttacker.MOMENT_MODELS;
import static org.windowkillproject.server.model.entities.enemies.attackstypes.LaserOperator.LASER_LINES;


public class GameManager {
    public GameManager(GlobeModel globeModel) {
        this.globeModel = globeModel;
    }

    private final GlobeModel globeModel;
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
            if (entity.getAnchor() != null && !(entity instanceof Hovering)) {
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

    public static int hoverAwayInitSeconds = -200, pauseInitSeconds = -2000;

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
        if (entityModel instanceof WyrmModel && enemyModel instanceof WyrmModel) {
            var wyrm1 = (WyrmModel) entityModel;
            var wyrm2 = (WyrmModel) enemyModel;
            if (wyrm1.getRotationSign() == wyrm2.getRotationSign()) {
                wyrm1.setMinusRotationSpeed();
            } else {
                for (int i = 0; i < 5; i++) {
                    wyrm1.route();
                    wyrm2.route();
                }
            }
        }
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
        Timer impactTimer = new Timer(FPS / 5, null);
        impactTimer.addActionListener(e -> {
            if (count.get() < t) {
                entityModel.setImpact(true);
                entityModel.move((int) deltaS.getX(), (int) deltaS.getY());
                count.getAndIncrement();
            } else {
                entityModel.setImpact(false);
                impactTimer.stop();
            }
        });
        return impactTimer;
    }

    private static Timer getImpactsTimer(ArrayList<EntityModel> entityModels, EntityModel not1, EntityModel not2, Point2D closestPointOfEnemy, int t) {
        AtomicInteger count = new AtomicInteger();
        Timer impactTimer = new Timer(FPS / 5, null);
        impactTimer.addActionListener(e -> {
            if (count.get() < t) {
                for (EntityModel entityModel : entityModels) {
                    if (isImpactAffected(not1, not2, entityModel)) {
                        entityModel.setImpact(true);
                        if (entityModel.getAnchor() != null && closestPointOfEnemy != null &&
                                !(entityModel instanceof Hovering)) {
                            Point2D deltaS = impactPoint(entityModel.getAnchor(), closestPointOfEnemy);
                            entityModel.move((int) deltaS.getX(), (int) deltaS.getY());
                        }
                    }
                }
                count.getAndIncrement();
            } else {
                for (EntityModel entityModel : entityModels) {
                    if (isImpactAffected(not1, not2, entityModel)) {
                        entityModel.setImpact(false);
                    }
                }
                impactTimer.stop();
            }
        });
        return impactTimer;
    }

    private static boolean isImpactAffected(EntityModel not1, EntityModel not2, EntityModel entityModel) {
        return !(entityModel.equals(not1) || entityModel.equals(not2) || entityModel instanceof Unmovable || entityModel instanceof Hovering);
    }

    public void enemyIntersectionControl() {
        ArrayList<EnemyModel> enemies = getEnemies();
        for (int i = 0; i < enemies.size(); i++) {
            var enemyModel = enemies.get(i);
            if (enemyModel instanceof Hideable && !((Hideable) enemyModel).isVisible())
                continue;

            Polygon p1 = enemyModel.getPolygon();
            if (p1 != null) {
                Area a1 = new Area(p1);
                boolean tempCollision = false;
                //impact controller
                for (int j = i + 1; j < enemies.size(); j++) {
                    var collidedEnemy = enemies.get(j);
                    if (collidedEnemy instanceof Hideable && !((Hideable) collidedEnemy).isVisible())
                        continue;
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

    private static boolean checkPointOn;

    public static boolean isCheckPointOn() {
        return checkPointOn;
    }

    public void setCheckPointOn(boolean checkPointOn) {
        GameManager.checkPointOn = checkPointOn;
    }

    public void epsilonsRewardControl() {
        for (EpsilonModel epsilonModel : globeModel.getEpsilons()) {
            for (int i = 0; i < collectableModels.size(); i++) {
                var collectableModel = collectableModels.get(i);
                if(collectableModel.isCollectedByEpsilon(epsilonModel)){
                    epsilonModel.collected(collectableModel.getRewardXp());
                    collectableModels.remove(collectableModel);
                    collectableModel.destroy();
                }
                if (globeModel.getElapsedTime().secondsPassed(collectableModel.getInitSeconds()) >= 10) {
                    collectableModels.remove(collectableModel);
                    collectableModel.destroy();
                }
            }
            if (checkPointOn) {
                if (globeModel.getElapsedTime().secondsPassed(lastPortal.getInitSeconds()) >= 10) {
                    checkPointOn = false;
                    lastPortal.destroy();
                }
                var rect = new Rectangle(lastPortal.getX(),
                        lastPortal.getY(),
                        lastPortal.getWidth(),
                        lastPortal.getHeight());

                if (isTransferableInBounds(epsilonModel, rect, false)) {
//                    pauseUpdate();
//                    int result = showPRPopUP();
//                    int currentXP = epsilonModel.getXp();
//                    if (result == JOptionPane.OK_OPTION) {
//                        if (currentXP > getPR(epsilonModel)) {
//                            epsilonModel.setXp(currentXP - getPR(epsilonModel));
//                            epsilonModel.setHp(epsilonModel.getHp() + 10);
//                           // checkpointSave(); todo URGENT
//                        }
//                    } else epsilonModel.setXp(currentXP + (getPR(epsilonModel) / 10));
//                    checkPointOn = false;
//                    lastPortal.destroy();
//                    resumeUpdate();

                }
            }
        }
    }

    private void setTransferableBoundsAllowed() {
        var panelModels = globeModel.getPanelModels();
        for (int j = 0; j < panelModels.size(); j++) {
            panelModels.get(j).resetCanShrink();
        }
        setEntitiesBoundsAllowed();
        setBulletsBoundsAllowed();
    }

    private int showPRPopUP() {
        return JOptionPane.showConfirmDialog(null,
                "do you wanna save your progress rate?"
        );
    }

    private void setEntitiesBoundsAllowed() {
        for (int i = 0; i < entityModels.size(); i++) {
            EntityModel entityModel = entityModels.get(i);
            int t = 0;
            entityModel.setAllowedArea(new Area());
            entityModel.setAllowedPanelModels(new ArrayList<>());
            for (int j = 0; j < globeModel.getPanelModels().size(); j++) {
                var panelModel = globeModel.getPanelModels().get(j);
                var rectangle = panelModel.getBounds();
                if (isTransferableInBounds(entityModel, rectangle, true)) {
                    entityModel.addToAllowedArea(panelModel);
                    if (entityModel instanceof EpsilonModel && t > 0)
                        checkShrinking(entityModel, rectangle, panelModel);
                    t++;
                }
            }
        }
    }

    private void setBulletsBoundsAllowed() {
        for (int i = 0; i < bulletModels.size(); i++) {
            BulletModel bulletModel = bulletModels.get(i);
            int t = 0;
            bulletModel.setAllowedArea(new Area());
            bulletModel.setAllowedPanelModels(new ArrayList<>());
            for (int j = 0; j < globeModel.getPanelModels().size(); j++) {
                var panelModel = globeModel.getPanelModels().get(j);
                var rectangle = panelModel.getBounds();
                if (isTransferableInBounds(bulletModel, rectangle, true)) {
                    bulletModel.addToAllowedArea(panelModel);
                    if (t > 0) checkShrinking(bulletModel, rectangle, panelModel);
                    t++;
                }
            }
        }
    }

    private void checkShrinking(Transferable transferable, Rectangle rectangle, PanelModel panelModel) {
        if (transferable.getX() + transferable.getWidth() / 2 >= rectangle.x + rectangle.width / 2) {
            panelModel.setCanShrinkRight(false);
        }
        if (transferable.getX() + transferable.getWidth() / 2 <= rectangle.x + rectangle.width / 2) {
            panelModel.setCanShrinkLeft(false);
        }
        if (transferable.getY() + transferable.getHeight() / 2 >= rectangle.y + rectangle.height / 2) {
            panelModel.setCanShrinkDown(false);
        }
        if (transferable.getY() + +transferable.getHeight() / 2 <= rectangle.y + rectangle.height / 2) {
            panelModel.setCanShrinkUp(false);
        }
    }

    private int lastAoEAttack;

    public void areaOfEffectControl() {
        for (int i = 0; i < MOMENT_MODELS.size(); i++) {
            MomentModel momentModel = MOMENT_MODELS.get(i);
            if (globeModel.getElapsedTime().getTotalSeconds() > momentModel.getTime() + 5) MOMENT_MODELS.remove(momentModel);
        }
        //timeout
        if (globeModel.getElapsedTime().getTotalSeconds() - lastAoEAttack > AOE_TIMEOUT) {
            lastAoEAttack = globeModel.getElapsedTime().getTotalSeconds();
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

    public GamePanelCorner getClosestPanelCorner(Point2D point2D) {
        var panelModels = globeModel.getPanelModels();
        final PanelModel[] closestPanel = {panelModels.getFirst()};
        final Point2D[] closestPoint = {new Point2D.Double(10000, 10000)};
        final int[] indexOfCorner = {0};

        for (int j = 0; j < panelModels.size(); j++) {
            var panelModel = panelModels.get(j);
            var rectangle = panelModel.getBounds();
            ArrayList<Point2D> corners = new ArrayList<>();
            corners.add(new Point2D.Double(rectangle.x, rectangle.y)); //tl 0
            corners.add(new Point2D.Double(rectangle.x + rectangle.width, rectangle.y));//tr 1
            corners.add(new Point2D.Double(rectangle.x, rectangle.y + rectangle.height)); //bl 2
            corners.add(new Point2D.Double(rectangle.x + rectangle.width, rectangle.y + rectangle.height));//br 3
            for (int i = 0; i < corners.size(); i++) {
                if (point2D.distance(corners.get(i)) < point2D.distance(closestPoint[0])) {
                    closestPoint[0] = corners.get(i);
                    indexOfCorner[0] = i;
                    closestPanel[0] = panelModel;
                }
            }
        }
        int code = getMovementCode(point2D, indexOfCorner, closestPoint);
        return new GamePanelCorner(closestPanel[0], code);
    }


    private void setTransferableLocalPanel(Transferable transferable) {
        AtomicInteger t = new AtomicInteger();
        var panelModels = globeModel.getPanelModels();
        for (int i = 0; i < panelModels.size(); i++) {
            var panelModel = panelModels.get(i);
            if (isTransferableInBounds(transferable, panelModel.getBounds(), false)) {
                t.getAndIncrement();
                transferable.setLocalPanelModel(panelModel);
            }
        }
        if (t.get() > 1) transferable.setLocalPanelModel(null);
    }

    public void keepTransferableInBounds() {
        setTransferableBoundsAllowed();
        if (!globeModel.getMainPanelModel().isExploding()) {
            keepEpsilonsInBounds();
            keepBulletsInBounds();
        }

    }

    private void keepEpsilonsInBounds() {
        for (EpsilonModel epsilonModel : globeModel.getEpsilons()) {
            setTransferableLocalPanel(epsilonModel);

            Area allowedArea = epsilonModel.getAllowedArea();
            if (!isTransferableInBounds(epsilonModel, allowedArea, false)) {
                if (epsilonModel.getLocalPanelModel() != null) {
                    keepInPanel(epsilonModel, epsilonModel.getLocalPanelModel());
                }
            }
            epsilonModel.getMessageQueue().enqueue(RES_EPSILON_HP + REGEX_SPLIT + epsilonModel.getHp());
        }
    }

    private void keepBulletsInBounds() {
        for (int i = 0; i < bulletModels.size(); i++) {
            BulletModel bulletModel = bulletModels.get(i);
            setTransferableLocalPanel(bulletModel);
            Area bulletAllowedArea = bulletModel.getAllowedArea();
            if (!isTransferableInBounds(bulletModel, bulletAllowedArea, false)) {
//                if (bulletModel.getLocalPanelModel() != null) {
//                    keepInPanel(bulletModel, bulletModel.getLocalPanelModel());
//                } todo why should i keep epsilon in bullet's panel???
            }
        }
    }

    public static void keepInPanel(EntityModel entityModel, PanelModel panel) {
        int endX = entityModel.getXO() + entityModel.getRadius();
        int endY = entityModel.getYO() + entityModel.getRadius();

        int localPanelX = panel.getX();
        int localPanelY = panel.getY();
        int endOfLocalPanelY = localPanelY + panel.getHeight();
        int endOfLocalPanelX = localPanelX + panel.getWidth();

        if (endY > endOfLocalPanelY) {
            int deltaY = endOfLocalPanelY - endY;
            entityModel.move(0, deltaY);
        }
        if (endX > endOfLocalPanelX) {
            int deltaX = endOfLocalPanelX - endX;
            entityModel.move(deltaX, 0);
        }
        if (entityModel.getY() < localPanelY) {
            int deltaY = localPanelY - entityModel.getY();
            entityModel.move(0, deltaY);
        }
        if (entityModel.getX() < localPanelX) {
            int deltaX = localPanelX - entityModel.getX();
            entityModel.move(deltaX, 0);
        }
    }

    public void epsilonIntersectionControl() {
        for (EpsilonModel epsilonModel : globeModel.getEpsilons()) {
            ArrayList<EnemyModel> enemies = getEnemies();
            for (EnemyModel enemyModel : enemies) {
                if (enemyModel instanceof Hideable && !((Hideable) enemyModel).isVisible())
                    continue;
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
                            if (vertexModel.isCollectedByEpsilon(epsilonModel)) {
                                if (epsilonModel.isMelame()) {
                                    if (random.nextInt(20) == 0) break; //%5 not hitting
                                }
                                epsilonModel.gotHit(enemyModel.getMeleeAttackHp());
                                break;
                            }
                        }
                        if (epsilonModel.isAstrapper()) enemyModel.gotHit(2);
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
    }
    public void empowerBullets(EpsilonModel shooterEpsilon, Point2D relativePoint) {
        var anchor = shooterEpsilon.getAnchor();
        Point2D point = weighedVector(unitVector(anchor, relativePoint), 10);
        var extraBullet1 = new BulletModel(globeModel,
                (int) (anchor.getX() + point.getX()),
                (int) (anchor.getY() + point.getY()), relativePoint, shooterEpsilon);
        point = weighedVector(point, 2);
        var extraBullet2 = new BulletModel(globeModel,
                (int) (anchor.getX() + point.getX()),
                (int) (anchor.getY() + point.getY()), relativePoint, shooterEpsilon);
        extraBullet1.shoot();
        extraBullet2.shoot();
    }

    public void writControl() {
        for (EpsilonModel epsilon : globeModel.getEpsilons())
             epsilon.getWrit().check();
    }
    public void deleteGamePanel(PanelModel panelModel) {
        if (panelModel !=null) {
            synchronized (LOCK) {
                globeModel.getPanelModels().remove(panelModel);
            }
//            app.getGameFrame().getLayeredPane().remove(panelView);
        }
    }

    public void specialtiesControl() {//todo un-static banish URGENT
//        for (EpsilonModel epsilonModel : globeModel.getEpsilons()) {
//            if (banish.isOn()) {
//                for (EntityModel entity : entityModels) {
//                    Point2D deltaS = impactPoint(entity.getAnchor(), epsilonModel.getAnchor());
//                    deltaS = weighedVector(deltaS, 4);
//                    if (!(deltaS.getY() < 1 && deltaS.getX() < 1)) {
//                        Timer impactTimer = getImpactTimer(entity, deltaS, BANISH_DURATION);
//                        impactTimer.start();
//                    }
//                }
//                banish.setOn(false);
//            }
//            if (empower.isOn()) {
//                ShotgunMouseListener.empowerInitSeconds = getTotalSeconds();
//                empower.setOn(false);
//            }
//            if (heal.isOn()) {
//                epsilonModel.setHp(epsilonModel.getHp() + 10);
//                heal.setOn(false);
//                heal.setPurchased(false);
//            }
//            if (dismay.isOn()) {
//                hoverAwayInitSeconds = getTotalSeconds();
//                dismay.setOn(false);
//            }
//            if (slaughter.isOn()) {
//                if (getTotalSeconds() - hoverAwayInitSeconds > 10) ShotgunMouseListener.slaughter = true;
//                slaughter.setOn(false);
//            }
//            if (slumber.isOn()) {
//                if (getTotalSeconds() - pauseInitSeconds > 120) pauseInitSeconds = getTotalSeconds();
//                slumber.setOn(false);
//            }
//        }
    }
}
