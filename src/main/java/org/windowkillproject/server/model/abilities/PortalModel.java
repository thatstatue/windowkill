package org.windowkillproject.server.model.abilities;

import org.windowkillproject.client.ui.panels.game.MainGamePanel;
import org.windowkillproject.controller.ElapsedTime;

import static org.windowkillproject.server.Config.EPSILON_RADIUS;
import static org.windowkillproject.controller.Controller.createAbilityView;

public class PortalModel extends AbilityModel{
    public static PortalModel lastPortal;

    public PortalModel(int x, int y) {
        super(MainGamePanel.getInstance(), x, y);
        initSeconds = ElapsedTime.getTotalSeconds();
        setWidth(EPSILON_RADIUS*5);
        setHeight(EPSILON_RADIUS*5);
        lastPortal = this;
        createAbilityView(getId(), getX(),getY());

    }

    private final long initSeconds;

    public long getInitSeconds() {
        return initSeconds;
    }


}
