package org.windowkillproject.view;


import javax.swing.*;
import java.awt.image.BufferedImage;


public abstract class ObjectView extends JLabel implements Viewable {
    protected BufferedImage img;
    String id;
    protected int x, y, width, height;
    private boolean enabled = true;

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


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
