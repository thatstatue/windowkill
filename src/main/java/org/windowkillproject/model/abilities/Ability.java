package org.windowkillproject.model.abilities;

import javax.swing.*;
import java.awt.image.BufferedImage;

public abstract class Ability extends JLabel {
    protected int x, y;
    protected BufferedImage img;
    protected Ability(int x, int y) {
        this.x = x;
        this.y = y;
    }

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
}
//todo: add collectable and bullet