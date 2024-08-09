package org.windowkillproject.client.view;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.windowkillproject.controller.json.serializers.BufferedImageSerializer;

import javax.swing.*;
import java.awt.image.BufferedImage;

@JsonIgnoreProperties({"accessibleContext", "ui", "inputVerifier", "actionMap", "border",
        "graphics", "graphicsConfiguration", "raster", "propertyChangeListeners", "treeLock", "toolkit", "locationOnScreen"})

public abstract class ObjectView extends JLabel implements Viewable {
    public String getGlobeId() {
        return globeId;
    }

    public void setGlobeId(String globeId) {
        this.globeId = globeId;
    }

    @JsonSerialize(using = BufferedImageSerializer.class)
    protected BufferedImage img;
    private final String id;
    private String globeId;
    protected int x, y, width, height;

    public ObjectView( String globeId, String id) {
        this.globeId = globeId;
        this.id = id;

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

    public String getId() {
        return id;
    }


    public BufferedImage getImg() {
        return img;
    }

    public void setImg(BufferedImage img) {
        this.img = img;
    }


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
