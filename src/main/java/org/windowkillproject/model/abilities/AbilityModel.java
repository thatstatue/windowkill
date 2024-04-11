package org.windowkillproject.model.abilities;

import org.windowkillproject.model.Drawable;

import java.util.UUID;

public abstract class AbilityModel implements Drawable {
    protected int x, y;
    String id;


    protected AbilityModel(int x, int y) {
        this.x = x;
        this.y = y;
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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


}
//todo: add collectable