package org.windowkillproject.model.abilities;

import org.windowkillproject.application.Config;
import org.windowkillproject.application.panels.game.GamePanel;
import org.windowkillproject.model.Transferable;
import org.windowkillproject.model.entities.Circular;
import org.windowkillproject.model.entities.EntityModel;
import org.windowkillproject.model.entities.EpsilonModel;
import org.windowkillproject.model.entities.enemies.EnemyModel;


import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Objects;


import static org.windowkillproject.application.Application.getGameFrame;
import static org.windowkillproject.application.Config.*;
import static org.windowkillproject.application.SoundPlayer.playBulletSound;
import static org.windowkillproject.application.panels.game.GamePanel.gamePanelsBounds;
import static org.windowkillproject.controller.Controller.createAbilityView;
import static org.windowkillproject.controller.GameController.getClosestPanelCorner;
import static org.windowkillproject.controller.GameController.impact;
import static org.windowkillproject.controller.Utils.*;
import static org.windowkillproject.model.entities.EntityModel.entityModels;
import static org.windowkillproject.model.entities.enemies.normals.OmenoctModel.omenoctModels;

public class BulletModel extends AbilityModel implements Projectable, Transferable {
    private static int attackHp = BULLET_ATTACK_HP;
    private final Point2D mousePoint;

    private ArrayList<GamePanel> allowedPanels = new ArrayList<>();
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
            delta = weighedVector(delta, Config.BULLET_SPEED);
            setX((int) (getX() + delta.getX()));
            setY((int) (getY() + delta.getY()));

            //enemies getting shot
            isEnemyShot();

            //frame getting shot
            isFrameShot();
        }
    }

    private void isFrameShot() {
        if (!EpsilonModel.getINSTANCE().getAllowedArea().contains(getX(), getY())) {
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
    public void addToAllowedArea(GamePanel panel){
        allowedPanels.add(panel);
        Area area = new Area(gamePanelsBounds.get(panel));
        getAllowedArea().add(area); //todo daijovah?
    }

    @Override
    public void setAllowedPanels(ArrayList<GamePanel> allowedPanels) {
        this.allowedPanels = allowedPanels;

    }

    private void hitWall(GamePanel gamePanel, int code) {
        for (int i = 0; i < omenoctModels.size(); i++) {
            var omenoctModel = omenoctModels.get(i);
            if (omenoctModel.getLocalPanel() != null &&
                    omenoctModel.getLocalPanel().equals(gamePanel)) {
                omenoctModel.hitWall(code);
            }
        }
    }

    private void isEnemyShot() {
        for (int i = 0; i < entityModels.size(); i++) {
            EntityModel entityModel = entityModels.get(i);
            if (entityModel instanceof EnemyModel) {
                EnemyModel enemyModel = (EnemyModel) entityModel;
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
                if (notHitVs && ((EnemyModel) entityModel).getPolygon() != null) {
                    Area enemyA = new Area(enemyModel.getPolygon());
                    if (enemyA.contains(this.getX(), this.getY())) {
                        enemyModel.gotShoot();
                        explode();
                        break;
                    }
                }
                if (enemyModel instanceof Circular) {
                    if (enemyModel.getAnchor().distance(new Point2D.Double(getX(), getY())) < enemyModel.getRadius()) {
                        enemyModel.gotShoot();
                        explode();
                        break;
                    }
                }

            }
        }
    }

    public BulletModel(int x, int y, Point2D mousePoint) {
        super(EpsilonModel.getINSTANCE().getLocalPanel(), x, y);
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
