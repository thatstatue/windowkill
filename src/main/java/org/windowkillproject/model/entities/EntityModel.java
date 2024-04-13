package org.windowkillproject.model.entities;

import org.windowkillproject.model.abilities.BulletModel;
import org.windowkillproject.model.abilities.Vertex;
import org.windowkillproject.model.Drawable;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.UUID;

import static org.windowkillproject.controller.Controller.createEntityView;

public abstract class EntityModel implements Drawable {
    protected int x, y, width, height;

    private int hp, attackHp;

    protected ArrayList<Vertex> vertices;

    public String getId() {
        return id;
    }

    public ArrayList<Vertex> getVertices() {
        return vertices;
    }
    public ArrayList<Point2D> getPointVertices() {
        ArrayList<Point2D> v = new ArrayList<>();
        for (Vertex vertex : vertices){
            v.add(vertex.getAnchor());
        }
        return v;
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
        setAnchor(x + getRadius(), y + getRadius());
    }

    public Point2D getAnchor() {
        return anchor;
    }

    public void setAnchor(Point2D anchor) {
        this.anchor = anchor;
    }
    public void setAnchor (double x, double y){
        anchor.setLocation(x , y);
    }


    public int getAttackHp() {
        return attackHp;
    }

    public void setAttackHp(int attackHp) {
        this.attackHp = attackHp;
    }

    public void gotHit(int attackHp) {
        setHp(getHp() - attackHp);
        if (getHp() <= 0) {
            setHp(0);
            destroy();
        }
    }

    public void gotShoot(BulletModel bulletModel){
        setHp(getHp() - bulletModel.getAttackHp());
        if (getHp() <= 0) {
            setHp(0);
            destroy();
        }
    }

    public void destroy() {
        entityModels.remove(this);
    }

    public int getXO() {
        return (int) anchor.getX();
    }

    public void setXO(int xO) {
        setAnchor(xO, getAnchor().getY());
    }

    public int getYO() {
        return (int) anchor.getY();
    }

    public void setYO(int yO) {
        setAnchor(getAnchor().getX(), yO);
    }

    private Point2D anchor = new Point2D.Double(0,0);
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
        this.id = UUID.randomUUID().toString();
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

    public void move(int deltaX, int deltaY){
        moveX(deltaX);
        moveY(deltaY);
    }

    private void moveX(int deltaX) {
        setX(getX() + deltaX);
        setXO(getXO() + deltaX);
        for (Vertex vertex : getVertices()) {
            vertex.setX(vertex.getX() + deltaX);
        }
    }

    private void moveY(int deltaY) {
        setY(getY() + deltaY);
        setYO(getYO() + deltaY);
        for (Vertex vertex : getVertices()) {
            vertex.setY(vertex.getY() + deltaY);
        }
    }

}
