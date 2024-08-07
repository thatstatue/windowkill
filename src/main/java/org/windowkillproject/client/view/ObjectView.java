package org.windowkillproject.client.view;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.windowkillproject.json.BufferedImageSerializer;

import javax.swing.*;
import java.awt.image.BufferedImage;

@JsonIgnoreProperties({"accessibleContext", "ui", "inputVerifier", "actionMap", "border",
        "graphics", "graphicsConfiguration", "raster", "propertyChangeListeners", "treeLock", "toolkit", "locationOnScreen"})

public abstract class ObjectView extends JLabel implements Viewable {
    @JsonSerialize(using = BufferedImageSerializer.class)
    protected BufferedImage img;
    String id;
    protected int x, y, width, height;
    private boolean enabled = true;

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    public boolean getEnabled(){return enabled;}


    public ObjectView(String id) {
        this.id = id;

    }

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
