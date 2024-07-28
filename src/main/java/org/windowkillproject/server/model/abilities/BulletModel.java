package org.windowkillproject.server.model.abilities;

import org.windowkillproject.client.ui.panels.game.GamePanel;
import org.windowkillproject.server.model.Transferable;
import org.windowkillproject.server.model.entities.Circular;
import org.windowkillproject.server.model.entities.EpsilonModel;
import org.windowkillproject.server.model.entities.enemies.EnemyModel;
import org.windowkillproject.server.model.entities.enemies.attackstypes.Hideable;


import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Objects;


import static org.windowkillproject.client.ui.App.getGameFrame;
import static org.windowkillproject.client.ui.SoundPlayer.playBulletSound;
import static org.windowkillproject.client.ui.panels.game.GamePanel.gamePanelsBounds;
import static org.windowkillproject.controller.Controller.createAbilityView;
import static org.windowkillproject.controller.GameController.*;
import static org.windowkillproject.controller.Utils.*;
import static org.windowkillproject.server.model.entities.enemies.normals.OmenoctModel.omenoctModels;

public class BulletModel extends AbilityModel implements Projectable, Transferable {
    private static int attackHp = BULLET_ATTACK_HP;
    private final Point2D mousePoint;

    private boolean slaughter;
    private ArrayList<GamePanel> allowedPanels = new ArrayList<>();

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
        Area epsilonArea = EpsilonModel.getINSTANCE().getAllowedArea();
//        area.subtract(epsilonArea);
//        if (area.isEmpty()) area = epsilonArea;
//        else area = getAllowedArea();

        if (!epsilonArea.contains(getX(), getY())) {
            var gamePanelCorner = getClosestPanelCorner(new Point2D.Double(getX(), getY()));

            hit(gamePanelCorner.gamePanel(), gamePanelCorner.corner());
            explode();

        }
    }

    private void hit(GamePanel gamePanel, int code) {
        getGameFrame().stretch(gamePanel, code);
        hitWall(gamePanel, code);
    }

    @Override
    public void addToAllowedArea(GamePanel panel) {
        allowedPanels.add(panel);
        Area area = new Area(gamePanelsBounds.get(panel));
        getAllowedArea().add(area);
    }

    @Override
    public void setAllowedPanels(ArrayList<GamePanel> allowedPanels) {
        this.allowedPanels = allowedPanels;

    }

    private void hitWall(GamePanel gamePanel, int code) {
        for (int i = 0; i < omenoctModels.size(); i++) {
            var omenoctModel = omenoctModels.get(i);
            if (omenoctModel.getLocalPanelModel() != null &&
                    omenoctModel.getLocalPanelModel().equals(gamePanel)) {
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

    public BulletModel(int x, int y, Point2D mousePoint) {
        super(EpsilonModel.getINSTANCE().getLocalPanelModel(), x, y);
        anchor = new Point2D.Double(x + EPSILON_RADIUS / 2, y + EPSILON_RADIUS * 1.5);
        isShoot = false;
        bulletModels.add(this);
        this.mousePoint = mousePoint;
        createAbilityView(id, x, y);
    }

    private boolean isShoot;


    public boolean isShoot() {
        return isShoot;
    }

    private void explode() {
        impact(this);
        bulletModels.remove(this);
        playBulletSound();
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
