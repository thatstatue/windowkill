package org.windowkillproject.client.view.entities;

import org.windowkillproject.client.view.ObjectView;

import java.util.ArrayList;

public abstract class EntityView extends ObjectView {


    public static ArrayList<EntityView> entityViews = new ArrayList<>();
    private boolean hovering;

    public static ArrayList<EntityView> getEntityViews() {
        return entityViews;
    }

    public static void setEntityViews(ArrayList<EntityView> entityViews) {
        EntityView.entityViews = entityViews;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    private boolean visible = true;

    public void setHovering(boolean hovering) {
        this.hovering = hovering;
    }

    public boolean isHovering() {
        return hovering;
    }

    public EntityView(String id) {
        super(id);
        getEntityViews().add(this);
    }
    public void set(int x, int y, int width, int height) {
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);

    }
}
