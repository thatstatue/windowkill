package org.windowkillproject.model.entities.enemies.minibosses;

import org.windowkillproject.application.panels.game.InternalGamePanel;
import org.windowkillproject.application.panels.game.PanelStatus;
import org.windowkillproject.controller.ElapsedTime;
import org.windowkillproject.model.abilities.VertexModel;
import org.windowkillproject.model.entities.EpsilonModel;
import org.windowkillproject.model.entities.enemies.EnemyModel;
import org.windowkillproject.model.entities.enemies.attackstypes.NonRotatable;
import org.windowkillproject.model.entities.enemies.attackstypes.Unmovable;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import static org.windowkillproject.application.Config.*;
import static org.windowkillproject.application.panels.game.PanelStatus.*;
import static org.windowkillproject.controller.Controller.createEntityView;
import static org.windowkillproject.controller.Controller.deleteGamePanel;
import static org.windowkillproject.controller.GameController.random;
import static org.windowkillproject.controller.Utils.globalRoutePoint;

public class BarricadosModel extends EnemyModel implements NonRotatable, Unmovable {
    private final int beginTime;
    public static ArrayList<BarricadosModel> barricadosModels = new ArrayList<>();
    public BarricadosModel( int x, int y) {
        super(null, x, y, BARRICADOS_RADIUS, Integer.MAX_VALUE, 0, 0, 0);
        PanelStatus panelStatus = isometric;
        if (random.nextInt(2) == 0) panelStatus = shrinkable;
        setLocalPanel(new InternalGamePanel(x, y,
                BARRICADOS_RADIUS*2, BARRICADOS_RADIUS*2,
                panelStatus, false
        ));
        initVertices();
        initPolygon();
        createEntityView(getId(), getX(),getY(),getWidth(),getHeight());
        beginTime = ElapsedTime.getTotalSeconds();
        barricadosModels.add(this);

    }
    @Override
    public void destroy() {
        super.destroy();
        barricadosModels.remove(this);
        if (getLocalPanel().getPanelStatus().equals(isometric)){
            deleteGamePanel(getLocalPanel());
        }
    }

    @Override
    public void route() {
        if (ElapsedTime.getTotalSeconds() - beginTime > 2 * 60) {
            destroy();
        }
    }

    @Override
    public Point2D getRoutePoint() {
        return globalRoutePoint(this.getAnchor(),
                EpsilonModel.getINSTANCE().getAnchor());
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
