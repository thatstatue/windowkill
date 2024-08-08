package org.windowkillproject.server.model.panelmodels;

import org.windowkillproject.server.model.globe.GlobeModel;

import java.awt.*;

public class InternalPanelModel extends PanelModel{
    public InternalPanelModel(GlobeModel globeModel, Rectangle bounds, PanelStatus panelStatus, boolean flexible) {
        super(globeModel, bounds, panelStatus, flexible, true);
        globeModel.getGlobeController().createPanelView(getId(),x,y,width,height);

    }
}
