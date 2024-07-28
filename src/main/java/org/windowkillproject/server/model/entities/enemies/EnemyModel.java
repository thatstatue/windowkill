package org.windowkillproject.server.model.entities.enemies;

import org.windowkillproject.server.ClientHandlerTeam;
import org.windowkillproject.server.Config;
import org.windowkillproject.server.model.abilities.CollectableModel;
import org.windowkillproject.server.model.entities.EntityModel;
import org.windowkillproject.server.model.entities.EpsilonModel;

import java.awt.*;

import static org.windowkillproject.controller.Controller.createEntityView;
import static org.windowkillproject.controller.GameController.random;
import static org.windowkillproject.server.model.entities.EpsilonModel.clientEpsilonModelMap;

public abstract class EnemyModel extends EntityModel {
    public EnemyModel(ClientHandlerTeam team, Rectangle localPanel, int x, int y, int radius, int hp , int meleeAttackHp, int rewardCount, int rewardXps) {
        super(team, localPanel,x, y, radius,hp, meleeAttackHp);
        setReward(rewardCount, rewardXps);
        setySpeed(Config.MAX_ENEMY_SPEED);
        setxSpeed(Config.MAX_ENEMY_SPEED);
        if (localPanel!= null) createEntityView(getId(), getX(),getY(),getWidth(),getHeight());

    }
    protected void initPolygon() {
        int vertices=getVertices().size();
        int[] xPoints = new int[vertices];
        int[] yPoints = new int[vertices];
        for (int i = 0; i < vertices; i++) {
            xPoints[i] = getVertices().get(i).getX();
            yPoints[i] = getVertices().get(i).getY();
        }
        setPolygon(new Polygon(xPoints, yPoints, vertices));
    }

    private int rewardCount, rewardXps;
    private static int killedEnemiesInWave, killedEnemiesTotal;
//    private InternalGamePanel bgPanel;

    public static int getKilledEnemiesTotal() {
        return killedEnemiesTotal;
    }

    public static void setKilledEnemiesTotal(int killedEnemiesTotal) {
        EnemyModel.killedEnemiesTotal = killedEnemiesTotal;
    }
//
//    public InternalGamePanel getBgPanel() {
//        return bgPanel;
//    }
//
//    public void setBgPanel(InternalGamePanel bgPanel) {
//        this.bgPanel = bgPanel;
//    }

    public static void setKilledEnemiesInWave(int killedEnemiesInWave) {
        EnemyModel.killedEnemiesInWave = killedEnemiesInWave;
    }

    public static int getKilledEnemiesInWave() {
        return killedEnemiesInWave;
    }

    protected void setReward(int rewardCount, int rewardXps) {
        this.rewardCount = rewardCount;
        this.rewardXps = rewardXps;
    }


    private int xSpeed, ySpeed;
    private Polygon polygon;

    public void setxSpeed(int xSpeed) {
        this.xSpeed = xSpeed;
    }

    public void setySpeed(int ySpeed) {
        this.ySpeed = ySpeed;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }
    @Override
    public void gotHit(int attackHp) {
        super.gotHit(attackHp);
        var epsilonModel = clientEpsilonModelMap.get();
        if (epsilonModel.isChironner())
            epsilonModel.setHp(epsilonModel.getHp()+3);
    }


        @Override
    public void destroy() {
        super.destroy();
        playDestroySound();
        killedEnemiesInWave++;
        killedEnemiesTotal++;
        for (int i = 0; i < rewardCount; i++) {
            int x = getXO() + random.nextInt(2 * getRadius()) - getRadius();
            int y = getYO() + random.nextInt(2 * getRadius()) - getRadius();

            new CollectableModel(getLocalPanelModel(), x, y, rewardXps);
        }
    }

    protected abstract void initVertices();
    protected void moveBGPanel(int x, int y) {
        var bgPanel = getBgPanel();
        var epsilonPanel = EpsilonModel.getINSTANCE().getLocalPanelModel();
        if (bgPanel != null && (epsilonPanel==null || !epsilonPanel.equals(bgPanel))) {
            var loc = bgPanel.getLocation();
            bgPanel.setLocation(
                    loc.x + getX()- x,
                    loc.y +getY() - y);
        }
    }

    @Override
    public void rotate() {
        super.rotate();
        for (int i = 0; i < getVertices().size(); i++) {
            polygon.xpoints[i] = getVertices().get(i).getX();
            polygon.ypoints[i] = getVertices().get(i).getY();
        }
    }
}
