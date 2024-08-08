package org.windowkillproject.server.model.entities.enemies;

import org.windowkillproject.Request;
import org.windowkillproject.server.Config;
import org.windowkillproject.server.model.abilities.CollectableModel;
import org.windowkillproject.server.model.entities.EntityModel;
import org.windowkillproject.server.model.entities.EpsilonModel;
import org.windowkillproject.server.model.globe.GlobeModel;
import org.windowkillproject.server.model.panelmodels.InternalPanelModel;
import org.windowkillproject.server.model.panelmodels.PanelModel;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;


import static org.windowkillproject.controller.GameManager.random;

public abstract class EnemyModel extends EntityModel {
    public EnemyModel(GlobeModel globeModel, PanelModel localPanel, int x, int y, int radius, int hp , int meleeAttackHp, int rewardCount, int rewardXps) {
        super(globeModel, localPanel,x, y, radius,hp, meleeAttackHp);
        setReward(rewardCount, rewardXps);
        setySpeed(Config.MAX_ENEMY_SPEED);
        setxSpeed(Config.MAX_ENEMY_SPEED);
//        if (localPanel!= null) globeModel.getGlobeController().createEntityView(getId(), getX(),getY(),getWidth(),getHeight());
        targetEpsilon = chooseRandomTarget(globeModel.getEpsilons());
        System.out.println("seeeee im an enemyyy");

    }
    protected EpsilonModel targetEpsilon;
    private EpsilonModel chooseRandomTarget(ArrayList<EpsilonModel> epsilonModels){
        int rand = random.nextInt(epsilonModels.size());
        return epsilonModels.get(rand);
    }

    public EpsilonModel getTargetEpsilon() {
        return targetEpsilon;
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
    private InternalPanelModel bgPanel;

    public InternalPanelModel getBgPanel() {
        return bgPanel;
    }

    public void setBgPanel(InternalPanelModel bgPanel) {
        this.bgPanel = bgPanel;
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
        for (EpsilonModel epsilonModel : globeModel.getEpsilons()) {
            if (epsilonModel.isChironner())
                epsilonModel.setHp(epsilonModel.getHp() + 3);
        }
    }


        @Override
    public void destroy() {
        super.destroy();
        globeModel.performAction(Request.REQ_PLAY_DESTROY_SOUND);
        globeModel.addKilledEnemiesInWave();
        globeModel.addKilledEnemiesTotal();
        for (int i = 0; i < rewardCount; i++) {
            int x = getXO() + random.nextInt(2 * getRadius()) - getRadius();
            int y = getYO() + random.nextInt(2 * getRadius()) - getRadius();

            new CollectableModel(globeModel,getLocalPanelModel(), x, y, rewardXps);
        }
    }

    protected abstract void initVertices();
    protected void moveBGPanel(int x, int y) {
        var bgPanel = getBgPanel();
        boolean temp = false;
        for (EpsilonModel epsilonModel : globeModel.getEpsilons()){
            var epsilonPanel = epsilonModel.getLocalPanelModel();
            if (epsilonPanel!=null && epsilonPanel.equals(bgPanel)) {
                temp = true;
                break;
            }
        }

        if (bgPanel != null && !temp) {
            var loc = new Point2D.Double(bgPanel.getX(), bgPanel.getY());
            bgPanel.setX((int) (loc.x + getX()- x));
            bgPanel.setY((int) (loc.y +getY() - y));
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
