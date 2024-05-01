package org.windowkillproject.view.abilities;

import org.windowkillproject.view.Viewable;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class AbilityView extends JLabel implements Viewable {
    public AbilityView(String id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
        abilityViews.add(this);
        enabled = true;
    }

    public static ArrayList<AbilityView> abilityViews = new ArrayList<>();


    protected int x, y;
    private boolean enabled = true;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    protected BufferedImage img;

    @Override
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public BufferedImage getImg() {
        return img;
    }

    public void setImg(BufferedImage img) {
        this.img = img;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void set(int x, int y, int width, int height) {
        setX(x);
        setY(y);
    }
}
