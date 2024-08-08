package org.windowkillproject.server.model.panelmodels;

import java.awt.*;

public class InternalPanelModel extends PanelModel{
    public InternalPanelModel(String globeId, Rectangle bounds, PanelStatus panelStatus, boolean flexible) {
        super(globeId, bounds, panelStatus, flexible, true);
        getGlobeModel().getGlobeController().createPanelView(getId(),x,y,width,height);

    }
}
