package org.windowkillproject.server.model;

import java.awt.*;

public class PanelModel {
    private boolean flexible;

    public boolean isBackground() {
        return background;
    }

    public void setBackground(boolean background) {
        this.background = background;
    }

    private boolean background;
    private Rectangle bounds;

    public PanelModel( Rectangle bounds, boolean flexible, boolean background) {
        this.flexible = flexible;
        this.bounds = bounds;
        this.background = background;
    }

    public boolean isFlexible() {
        return flexible;
    }

    public void setFlexible(boolean flexible) {
        this.flexible = flexible;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }
}
