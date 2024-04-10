package org.windowkillproject.model.entities;

import org.windowkillproject.application.frames.GamePanel;
import org.windowkillproject.model.abilities.BulletModel;
import org.windowkillproject.model.abilities.Vertex;

import java.util.ArrayList;
import java.util.UUID;

import static org.windowkillproject.controller.Controller.createEntityView;

public abstract class EntityModel {
    protected int x, y, width, height;
    private int hp, attackHp;

    protected ArrayList<Vertex> vertices;

    public String getId() {
        return id;
    }

    public ArrayList<Vertex> getVertices() {
        return vertices;
    }

    private int radius;
    public static ArrayList<EntityModel> entityModels=new ArrayList<>();


    public int getRadius() {
        return radius;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setRadius(int radius) {
        this.radius = radius;
        setHeight(2 * getRadius());
        setWidth(2 * getRadius());
        setXO(x + getRadius());
        setYO(y + getRadius());
    }

    public int getAttackHp() {
        return attackHp;
    }

    public void setAttackHp(int attackHp) {
        this.attackHp = attackHp;
    }

    public void gotHit(EntityModel other) {
        setHp(getHp() - other.getAttackHp());
        if (getHp() <= 0) {
            setHp(0);
            destroy();
        }
    }

    public void gotShoot(BulletModel bulletModel){
        setHp(getHp() - bulletModel.getAttackHp());
        bulletModel.explode();
        if (getHp() <= 0) {
            setHp(0);
            destroy();
        }
    }

    public void destroy() {
        entityModels.remove(this);
    }

    public int getXO() {
        return xO;
    }

    public void setXO(int xO) {
        this.xO = xO;
    }

    public int getYO() {
        return yO;
    }

    public void setYO(int yO) {
        this.yO = yO;
    }

    private int xO, yO;
    private final String id;

    public void rotate() {
        if (getVertices() != null) {
            for (Vertex vertex : getVertices()) {
                vertex.rotate();
            }
        }
    }

    protected EntityModel(int x, int y) {
        this.x = x;
        this.y = y;
        vertices = new ArrayList<>();
        this.id= UUID.randomUUID().toString();
        entityModels.add(this);
        createEntityView(id);
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

    public void moveX(int deltaX) {
        setX(getX() + deltaX);
        setXO(getXO() + deltaX);
        for (Vertex vertex : getVertices()) {
            vertex.setX(vertex.getX() + deltaX);
        }
    }

    public void moveY(int deltaY) {
        setY(getY() + deltaY);
        setYO(getYO() + deltaY);
        for (Vertex vertex : getVertices()) {
            vertex.setY(vertex.getY() + deltaY);
        }
    }

}
