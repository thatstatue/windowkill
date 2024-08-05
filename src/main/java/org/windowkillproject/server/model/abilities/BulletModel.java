package org.windowkillproject.server.model.abilities;

import org.windowkillproject.server.model.entities.EpsilonModel;
import org.windowkillproject.server.model.globe.GlobeModel;
import org.windowkillproject.server.model.panelmodels.PanelModel;
import org.windowkillproject.server.model.Transferable;
import org.windowkillproject.server.model.entities.Circular;
import org.windowkillproject.server.model.entities.enemies.EnemyModel;
import org.windowkillproject.server.model.entities.enemies.attackstypes.Hideable;


import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Objects;


import static org.windowkillproject.Constants.*;
import static org.windowkillproject.Request.REQ_PLAY_BULLET_SOUND;
import static org.windowkillproject.controller.GameManager.*;
import static org.windowkillproject.controller.Utils.*;
import static org.windowkillproject.server.model.entities.enemies.normals.OmenoctModel.omenoctModels;

public class BulletModel extends AbilityModel implements Projectable, Transferable {
    private static int attackHp = BULLET_ATTACK_HP;
    private final Point2D mousePoint;

    private boolean slaughter;
    private ArrayList<PanelModel> allowedPanels = new ArrayList<>();

    public void setSlaughter(boolean slaughter) {
        this.slaughter = slaughter;
    }

    public static void setAttackHp(int attackHp) {
        BulletModel.attackHp = attackHp;
    }

    public static ArrayList<BulletModel> bulletModels = new ArrayList<>();

    public static int getAttackHp() {
        return attackHp;
    }

    public Point2D getMousePoint() {
        return mousePoint;
    }

    @Override
    public void shoot() {
        setShoot(true);
        move();
        move();
        move();
    }

    @Override
    public void move() {
        if (isShoot()) {
            Point2D delta = unitVector(getMousePoint(), this.getAnchor());
            delta = weighedVector(delta, BULLET_SPEED);
            setX((int) (getX() + delta.getX()));
            setY((int) (getY() + delta.getY()));

            //enemies getting shot
            isEnemyShot();

            //frame getting shot
            isFrameShot();
        }
    }

    private void isFrameShot() {
//        Area area = getAllowedArea();
//        System.out.println(area.getBounds());
        Area epsilonArea = shooterEpsilon.getAllowedArea();
//        area.subtract(epsilonArea);
//        if (area.isEmpty()) area = epsilonArea;
//        else area = getAllowedArea();

        if (!epsilonArea.contains(getX(), getY())) {
            var gamePanelCorner = globeModel.getGameManager().getClosestPanelCorner(new Point2D.Double(getX(), getY()));

            hit(gamePanelCorner.panelModel(), gamePanelCorner.corner());
            explode();

        }
    }

    private void hit(PanelModel panelModel, int code) {
        globeModel.stretch(panelModel, code);
        hitWall(panelModel, code);
    }

    @Override
    public void addToAllowedArea(PanelModel panel) {
        allowedPanels.add(panel);
        Area area = new Area(
                new Rectangle
                (panel.getX(), panel.getY(),
                panel.getWidth(), panel.getHeight()));
        getAllowedArea().add(area);
    }

    @Override
    public void setAllowedPanelModels(ArrayList<PanelModel> allowedPanels) {
        this.allowedPanels = allowedPanels;

    }

    private void hitWall(PanelModel panelModel, int code) {
        for (int i = 0; i < omenoctModels.size(); i++) {
            var omenoctModel = omenoctModels.get(i);
            if (omenoctModel.getLocalPanelModel() != null &&
                    omenoctModel.getLocalPanelModel().equals(panelModel)) {
                omenoctModel.hitWall(code);
            }
        }
    }

    private void isEnemyShot() {
        var enemyModels = getEnemies();
        for (int i = 0; i < enemyModels.size(); i++) {
            EnemyModel enemyModel = enemyModels.get(i);
            if (enemyModel instanceof Hideable && !((Hideable) enemyModel).isVisible())
                continue;
            //not hitting vertices
            boolean notHitVs = true;
            for (VertexModel vertexModel : enemyModel.getVertices()) {
                if (Objects.equals(vertexModel.getAnchor(), new Point2D.Double(getX(), getY()))) {
                    explode();
                    notHitVs = false;
                    break;
                }
            }
            //hitting enemy
            if (notHitVs && enemyModel.getPolygon() != null) {
                Area enemyA = new Area(enemyModel.getPolygon());
                if (enemyA.contains(this.getX(), this.getY())) {
                    if (slaughter) enemyModel.gotShot(50);
                    else enemyModel.gotShot();
                    explode();
                    break;
                }
            }
            if (enemyModel instanceof Circular) {
                if (enemyModel.getAnchor().distance(new Point2D.Double(getX(), getY())) < enemyModel.getRadius()) {
                    if (slaughter) enemyModel.gotShot(50);
                    else enemyModel.gotShot();
                    explode();
                    break;
                }
            }

        }

    }
    private final EpsilonModel shooterEpsilon;
    public BulletModel(GlobeModel globeModel, int x, int y, Point2D mousePoint, EpsilonModel shooterEpsilon) {
        super(globeModel, null, x, y);
        anchor = new Point2D.Double(x + EPSILON_RADIUS / 2.0, y + EPSILON_RADIUS * 1.5);
        isShoot = false;
        bulletModels.add(this);
        this.mousePoint = mousePoint;
        this.shooterEpsilon = shooterEpsilon;
        globeModel.getGlobeController().createAbilityView(id, x, y);
    }

    private boolean isShoot;


    public boolean isShoot() {
        return isShoot;
    }

    private void explode() {
        impact(this);
        bulletModels.remove(this);
        globeModel.performAction(REQ_PLAY_BULLET_SOUND);
        destroy();
    }

    public void setShoot(boolean shoot) {
        isShoot = shoot;
    }

    @Override
    public int getWidth() {
        return 5;
    }

    @Override
    public int getHeight() {
        return 5;
    }

}
