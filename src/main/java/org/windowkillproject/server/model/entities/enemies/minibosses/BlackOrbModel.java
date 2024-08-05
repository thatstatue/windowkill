package org.windowkillproject.server.model.entities.enemies.minibosses;

import org.windowkillproject.server.model.entities.Circular;
import org.windowkillproject.server.model.entities.enemies.EnemyModel;
import org.windowkillproject.server.model.entities.enemies.attackstypes.Hideable;
import org.windowkillproject.server.model.entities.enemies.attackstypes.LaserOperator;
import org.windowkillproject.server.model.entities.enemies.attackstypes.NonRotatable;
import org.windowkillproject.server.model.entities.enemies.attackstypes.Unmovable;
import org.windowkillproject.server.model.globe.GlobeModel;
import org.windowkillproject.server.model.panelmodels.InternalPanelModel;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import static org.windowkillproject.Constants.*;
import static org.windowkillproject.server.Config.*;
import static org.windowkillproject.server.model.panelmodels.PanelStatus.isometric;
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

    public BlackOrbModel(GlobeModel globeModel, int x, int y) {
        super(globeModel,null, x, y, ORB_RADIUS, 30, 0, 1, 30);

        move(getXPoints()[blackOrbModels.size()]-x, getYPoints()[blackOrbModels.size()]-y);
        blackOrbModels.add(this);

        setLocalPanelModel(new InternalPanelModel(globeModel, new Rectangle(getX()-ORB_RADIUS*2, getY()-ORB_RADIUS*2,
                ORB_RADIUS*6, ORB_RADIUS*6), isometric , true
        ));
        globeModel.getGlobeController().createEntityView(getId(), getX(),getY(),getWidth(),getHeight());
        lastSpawnTime = globeModel.getElapsedTime().getTotalSeconds();
    }
    private static boolean complete;
    @Override
    public void route() {
        if (blackOrbModels.get(0).equals(this) &&
                globeModel.getElapsedTime().getTotalSeconds() - lastSpawnTime > 1 &&
                blackOrbModels.size() < 5 && !complete){
            new BlackOrbModel(globeModel,x,y);
            lastSpawnTime = globeModel.getElapsedTime().getTotalSeconds();
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
               targetEpsilon.getAnchor());
    }

    @Override
    protected void initVertices() {

    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }
}
