package org.windowkillproject.server.model.entities.enemies.minibosses;

import org.windowkillproject.server.model.globe.GlobeModel;
import org.windowkillproject.server.model.panelmodels.InternalPanelModel;
import org.windowkillproject.server.model.panelmodels.PanelStatus;
import org.windowkillproject.server.model.abilities.VertexModel;
import org.windowkillproject.server.model.entities.enemies.EnemyModel;
import org.windowkillproject.server.model.entities.enemies.attackstypes.NonRotatable;
import org.windowkillproject.server.model.entities.enemies.attackstypes.Unmovable;

import java.awt.*;
import java.awt.geom.Point2D;

import static org.windowkillproject.server.Config.*;

import static org.windowkillproject.controller.GameManager.random;
import static org.windowkillproject.controller.Utils.globalRoutePoint;
import static org.windowkillproject.server.model.panelmodels.PanelStatus.isometric;
import static org.windowkillproject.server.model.panelmodels.PanelStatus.shrinkable;

public class BarricadosModel extends EnemyModel implements NonRotatable, Unmovable {
    private final int beginTime;
    public BarricadosModel(String globeId, int x, int y) {
        super(globeId,null, x, y, BARRICADOS_RADIUS, Integer.MAX_VALUE, 0, 0, 0);
        PanelStatus panelStatus = isometric;
        if (random.nextInt(2) == 0) panelStatus = shrinkable;
        setLocalPanelModel(new InternalPanelModel(globeId, new Rectangle(x, y,
                BARRICADOS_RADIUS*2, BARRICADOS_RADIUS*2),
                panelStatus, false
        ));
        initVertices();
        initPolygon();
//        globeModel.getGlobeController().createEntityView(getId(), getX(),getY(),getWidth(),getHeight());
        beginTime = getGlobeModel().getElapsedTime().getTotalSeconds();
        getGlobeModel().getBarricadosModels().add(this);

    }
    @Override
    public void destroy() {
        super.destroy();
        getGlobeModel().getBarricadosModels().remove(this);
        if (getLocalPanelModel().getPanelStatus().equals(isometric)){
            getGlobeModel().getGameManager().deleteGamePanel(getLocalPanelModel());
        }
    }

    @Override
    public void route() {
        if (getGlobeModel().getElapsedTime().getTotalSeconds() - beginTime > 2 * 60) {
            destroy();
        }
    }

    @Override
    public Point2D getRoutePoint() {
        return globalRoutePoint(this.getAnchor(),
                targetEpsilon.getAnchor());
    }

    @Override
    protected void initVertices() {
        int halfSideLength = (int) (getRadius() / Math.sqrt(2)+14);


        getVertices().add(new VertexModel(getXO() - halfSideLength, getYO() - halfSideLength, this));
        getVertices().add(new VertexModel(getXO() + halfSideLength, getYO() - halfSideLength, this));
        getVertices().add(new VertexModel(getXO() + halfSideLength, getYO() + halfSideLength, this));
        getVertices().add(new VertexModel(getXO() - halfSideLength, getYO() + halfSideLength, this));

    }
}
