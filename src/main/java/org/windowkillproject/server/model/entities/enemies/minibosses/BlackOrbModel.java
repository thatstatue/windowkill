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

import static org.windowkillproject.Constants.*;
import static org.windowkillproject.server.Config.*;
import static org.windowkillproject.server.model.panelmodels.PanelStatus.isometric;
import static org.windowkillproject.controller.Utils.globalRoutePoint;

public class BlackOrbModel extends EnemyModel implements Hideable, NonRotatable, Unmovable, LaserOperator, Circular {

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

    public BlackOrbModel(String globeId, int x, int y) {
        super(globeId,null, x, y, ORB_RADIUS, 30, 0, 1, 30);

        move(getXPoints()[getGlobeModel().getBlackOrbModels().size()]-x, getYPoints()[getGlobeModel().getBlackOrbModels().size()]-y);
        getGlobeModel().getBlackOrbModels().add(this);

        setLocalPanelModel(new InternalPanelModel(globeId, new Rectangle(getX()-ORB_RADIUS*2, getY()-ORB_RADIUS*2,
                ORB_RADIUS*6, ORB_RADIUS*6), isometric , true
        ));
//        globeModel.getGlobeController().createEntityView(getId(), getX(),getY(),getWidth(),getHeight());
        lastSpawnTime = getGlobeModel().getElapsedTime().getTotalSeconds();
    }
    private static boolean complete;
    @Override
    public void route() {
        if (getGlobeModel().getBlackOrbModels().get(0).equals(this) &&
                getGlobeModel().getElapsedTime().getTotalSeconds() - lastSpawnTime > 1 &&
                getGlobeModel().getBlackOrbModels().size() < 5 && !complete){
            new BlackOrbModel(globeId,x,y);
            lastSpawnTime = getGlobeModel().getElapsedTime().getTotalSeconds();
            if (getGlobeModel().getBlackOrbModels().size() == 5) {
                for (BlackOrbModel blackOrbModel : getGlobeModel().getBlackOrbModels()) {
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
        getGlobeModel().getBlackOrbModels().remove(this);
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
