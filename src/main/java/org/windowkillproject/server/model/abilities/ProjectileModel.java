package org.windowkillproject.server.model.abilities;

import org.windowkillproject.Request;
import org.windowkillproject.server.model.panelmodels.PanelModel;
import org.windowkillproject.server.model.entities.EntityModel;
import org.windowkillproject.server.model.entities.EpsilonModel;
import org.windowkillproject.server.model.entities.enemies.EnemyModel;
import org.windowkillproject.server.model.entities.enemies.attackstypes.ProjectileOperator;


import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.util.Objects;

import static org.windowkillproject.Constants.*;
import static org.windowkillproject.controller.GameManager.random;
import static org.windowkillproject.controller.Utils.unitVector;
import static org.windowkillproject.controller.Utils.weighedVector;

public class ProjectileModel extends AbilityModel implements Projectable {
    private int attackHp;
    private final boolean isHovering;
    private PanelModel localPanel;


    public void setAttackHp(int attackHp) {
        this.attackHp = attackHp;
    }

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

    public ProjectileModel(PanelModel localPanel, ProjectileOperator parent, int attackHp, boolean isHovering, EpsilonModel epsilonModel, Color topColor, Color bottomColor) {
        super(parent.getGlobeModel(), localPanel, parent.getX() + parent.getRadius(), parent.getY() + parent.getRadius());
//        anchor = new Point2D.Double(x, y);
        isShoot = false;
        globeModel.projectileModels.add(this);
        this.parent = parent;
        this.attackHp = attackHp;
        this.isHovering = isHovering;
        this.topColor = topColor;
        this.bottomColor = bottomColor;
        delta = unitVector(getPoint2D(epsilonModel), this.getAnchor());
        delta = weighedVector(delta, BULLET_SPEED / 2.0);
//        localPanel = parent.getLocalPanel();
        globeModel.getGlobeController().createAbilityView(id, x, y);

    }

    private Point2D getPoint2D( EpsilonModel epsilonModel) {
        Point2D headedPoint;
        if (epsilonModel == null) {
            headedPoint = new Point2D.Double(random.nextInt(CENTER_X*2),
                     random.nextInt(CENTER_Y*2));
        }else headedPoint = epsilonModel.getAnchor();
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
        for (int i = 0; i < globeModel.entityModels.size(); i++) {
            EntityModel entityModel = globeModel.entityModels.get(i);
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
                for (EpsilonModel epsilon :globeModel.getEpsilons()) {
                    if (notHitVs && entityModel instanceof EpsilonModel &&
                            epsilon.getAnchor().
                                    distance(new Point2D.Double(getX(), getY())) < EPSILON_RADIUS) {
                        epsilon.gotHit(attackHp);
                        explode();
                    }
                }
                //hitting enemy
                if (entityModel instanceof EnemyModel) {
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
        if (!isHovering) globeModel.getGameManager().impact(this);
        globeModel.projectileModels.remove(this);
        globeModel.performAction(Request.REQ_PLAY_BULLET_SOUND);
        destroy();
    }

    public void setShoot(boolean shoot) {
        isShoot = shoot;
    }


}
