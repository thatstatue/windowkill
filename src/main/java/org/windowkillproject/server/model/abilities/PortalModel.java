package org.windowkillproject.server.model.abilities;

import org.windowkillproject.server.model.globe.GlobeModel;

import static org.windowkillproject.Constants.EPSILON_RADIUS;

public class PortalModel extends AbilityModel{
    public static PortalModel lastPortal;

    public PortalModel(String globeId,int x, int y) {
        super(globeId, null, x, y);
        setLocalPanelModel(getGlobeModel().getMainPanelModel());
        initSeconds = getGlobeModel().getElapsedTime().getTotalSeconds();
        setWidth(EPSILON_RADIUS*5);
        setHeight(EPSILON_RADIUS*5);
        lastPortal = this;
        getGlobeModel().getGlobeController().createAbilityView(getId(), getX(),getY());

    }
    private final long initSeconds;

    public long getInitSeconds() {
        return initSeconds;
    }


}
