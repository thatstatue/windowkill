package org.windowkillproject.server.model;


import org.windowkillproject.server.model.panelmodels.PanelModel;

import java.util.ArrayList;

public interface Transferable {
    int getX();
    int getY();
    int getWidth();
    int getHeight();
    void addToAllowedArea(PanelModel panel);
    void setLocalPanelModel(PanelModel gamePanel);
    PanelModel getLocalPanelModel();
    void setAllowedPanelModels(ArrayList<PanelModel> allowedPanels);
}
