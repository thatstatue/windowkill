package org.windowkillproject.model.abilities;

import org.windowkillproject.application.Config;
import org.windowkillproject.application.panels.game.GamePanel;
import org.windowkillproject.model.entities.EntityModel;
import org.windowkillproject.model.entities.EpsilonModel;
import org.windowkillproject.model.entities.enemies.EnemyModel;
import org.windowkillproject.model.entities.enemies.attackstypes.ProjectileOperator;


import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Objects;

import static org.windowkillproject.application.Config.*;
import static org.windowkillproject.application.SoundPlayer.playBulletSound;
import static org.windowkillproject.controller.Controller.createAbilityView;
import static org.windowkillproject.controller.GameController.impact;
import static org.windowkillproject.controller.GameController.random;
import static org.windowkillproject.controller.Utils.unitVector;
import static org.windowkillproject.controller.Utils.weighedVector;
import static org.windowkillproject.model.entities.EntityModel.entityModels;

public class ProjectileModel extends AbilityModel implements Projectable {
    private int attackHp;
    private final boolean isHovering;
    private GamePanel localPanel;


    public void setLocalPanel(GamePanel localPanel) {
        this.localPanel = localPanel;
    }

    public void setAttackHp(int attackHp) {
        this.attackHp = attackHp;
    }

    public static ArrayList<ProjectileModel> projectileModels = new ArrayList<>();
    private final Color topColor, bottomColor;

    public Color getTopColor() {
        return topColor;
    }

    public Color getBottomColor() {
        return bottomColor;
    }

    public int getAttackHp() {
        return attackHp;
    }

    private final ProjectileOperator parent;
    private Point2D delta;

    public ProjectileModel(GamePanel localPanel, ProjectileOperator parent, int attackHp, boolean isHovering, boolean isTowardsEpsilon, Color topColor, Color bottomColor) {
        super(localPanel, parent.getX() + parent.getRadius(), parent.getY() + parent.getRadius());
//        anchor = new Point2D.Double(x, y);
        isShoot = false;
        projectileModels.add(this);
        this.parent = parent;
        this.attackHp = attackHp;
        this.isHovering = isHovering;
        this.topColor = topColor;
        this.bottomColor = bottomColor;
        delta = unitVector(getPoint2D(isTowardsEpsilon), this.getAnchor());
        delta = weighedVector(delta, BULLET_SPEED / 2.0);
//        localPanel = parent.getLocalPanel();
        createAbilityView(id, x, y);

    }

    private Point2D getPoint2D(boolean isTowardsEpsilon) {
        final Point2D headedPoint;
        headedPoint = EpsilonModel.getINSTANCE().getAnchor();
        if (!isTowardsEpsilon) {
            headedPoint.setLocation(random.nextInt(CENTER_X*2),
                     random.nextInt(CENTER_Y*2));
        }
        return headedPoint;
    }

    @Override
    public void shoot() {
        setShoot(true);
        //move out of parent's area
        for (int i = 0; i < parent.getRadius(); i += BULLET_SPEED / 3) {
            move();
        }

    }

    @Override
    public void move() {
        if (isShoot()) {
            setX((int) (getX() + delta.getX()));
            setY((int) (getY() + delta.getY()));

            //enemies getting shot
            isEntityShot();

            //frame getting shot
        }
        if (!isHovering) isFrameShot();
    }

    private void isFrameShot() {
        if (localPanel != null) {
            boolean shotRight = getX() > localPanel.getWidth();
            boolean shotLeft = getX() < 0;
            boolean shotUp = getY() < 0;
            boolean shotDown = getY() > localPanel.getHeight();

            if (shotLeft || shotRight || shotUp || shotDown) {
                explode();
            }
        }
    }

    private void isEntityShot() {
        for (int i = 0; i < entityModels.size(); i++) {
            EntityModel entityModel = entityModels.get(i);
            if (!entityModel.equals(parent)) {
                //not hitting vertices
                boolean notHitVs = true;
                for (VertexModel vertexModel : entityModel.getVertices()) {
                    if (Objects.equals(vertexModel.getAnchor(), new Point2D.Double(getX(), getY()))) {
                        explode();
                        notHitVs = false;
                        break;
                    }
                }
                if (notHitVs && entityModel instanceof EpsilonModel &&
                        EpsilonModel.getINSTANCE().getAnchor().
                                distance(new Point2D.Double(getX(), getY())) < EPSILON_RADIUS) {
                    EpsilonModel.getINSTANCE().gotHit(attackHp);
                    explode();
                }
                //hitting enemy
                else if (entityModel instanceof EnemyModel) {
                    EnemyModel enemyModel = (EnemyModel) entityModel;
                    if (notHitVs && enemyModel.getPolygon() != null) {
                        Area enemyA = new Area(enemyModel.getPolygon());
                        if (enemyA.contains(this.getX(), this.getY())) {
                            explode();
                            break;
                        }
                    }
                }
            }
        }
    }

    private boolean isShoot;


    public boolean isShoot() {
        return isShoot;
    }

    private void explode() {
        if (!isHovering) impact(this);
        projectileModels.remove(this);
        playBulletSound();
        destroy();
    }

    public void setShoot(boolean shoot) {
        isShoot = shoot;
    }


}
