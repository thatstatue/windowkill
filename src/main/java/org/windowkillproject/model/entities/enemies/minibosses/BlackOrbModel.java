package org.windowkillproject.model.entities.enemies.minibosses;

import org.windowkillproject.application.panels.game.InternalGamePanel;

import org.windowkillproject.model.entities.Circular;
import org.windowkillproject.model.entities.EpsilonModel;
import org.windowkillproject.model.entities.enemies.EnemyModel;
import org.windowkillproject.model.entities.enemies.attackstypes.Hideable;
import org.windowkillproject.model.entities.enemies.attackstypes.LaserOperator;
import org.windowkillproject.model.entities.enemies.attackstypes.NonRotatable;
import org.windowkillproject.model.entities.enemies.attackstypes.Unmovable;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import static org.windowkillproject.application.Config.*;
import static org.windowkillproject.application.panels.game.PanelStatus.isometric;
import static org.windowkillproject.controller.Controller.createEntityView;
import static org.windowkillproject.controller.ElapsedTime.getTotalSeconds;
import static org.windowkillproject.controller.Utils.globalRoutePoint;

public class BlackOrbModel extends EnemyModel implements Hideable, NonRotatable, Unmovable, LaserOperator, Circular {
    public static ArrayList<BlackOrbModel> blackOrbModels = new ArrayList<>();

    public static void setComplete(boolean complete) {
        BlackOrbModel.complete = complete;
    }

    private int[] getXPoints(){
        int[] xPoints = new int[5];
        for (int i = 0; i < 5; i++) {
            double angle = Math.toRadians(90 + (360.0 / 5) * i);
            xPoints[i] = (int) (epsilonLoc.getX() + 1.2* WYRM_DISTANCE * Math.cos(angle));
        }
        return xPoints;
    }
    private int[] getYPoints(){
        int[] yPoints = new int[5];
        for (int i = 0; i < 5; i++) {
            double angle = Math.toRadians(90 + (360.0 / 5) * i);
            yPoints[i] = (int) (epsilonLoc.getY() + 1.2*WYRM_DISTANCE * Math.sin(angle));
        }
        return yPoints;
    }
    private static Point2D epsilonLoc= new Point2D.Double(CENTER_X,CENTER_Y/1.2);
    private int lastSpawnTime;
    private boolean visible;

    public BlackOrbModel(int x, int y) {
        super(null, x, y, ORB_RADIUS, 30, 0, 1, 30);

        move(getXPoints()[blackOrbModels.size()]-x, getYPoints()[blackOrbModels.size()]-y);
        blackOrbModels.add(this);

        setLocalPanel(new InternalGamePanel(getX()-ORB_RADIUS*2, getY()-ORB_RADIUS*2,
                ORB_RADIUS*6, ORB_RADIUS*6, isometric , true
        ));
        createEntityView(getId(), getX(),getY(),getWidth(),getHeight());
        lastSpawnTime = getTotalSeconds();
    }
    private static boolean complete;
    @Override
    public void route() {
        if (blackOrbModels.get(0).equals(this) &&
                getTotalSeconds() - lastSpawnTime > 1 &&
                blackOrbModels.size() < 5 && !complete){
            new BlackOrbModel(x,y);
            lastSpawnTime = getTotalSeconds();
            if (blackOrbModels.size() == 5) {
                for (BlackOrbModel blackOrbModel : blackOrbModels) {
                    blackOrbModel.setVisible(true);
                }
                complete = true;
            }
        }
        if (complete){
            setLaserEnds();

        }
    }
    @Override
    public void destroy(){
        super.destroy();
        blackOrbModels.remove(this);
    }
    @Override
    public Point2D getRoutePoint() {
        return globalRoutePoint(this.getAnchor(),
                EpsilonModel.getINSTANCE().getAnchor());
    }

    @Override
    protected void initVertices() {

    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public boolean getVisible() {
        return visible;
    }
}
