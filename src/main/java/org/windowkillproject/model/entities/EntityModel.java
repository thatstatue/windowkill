package org.windowkillproject.model.entities;

import org.windowkillproject.application.SoundPlayer;
import org.windowkillproject.application.panels.game.GamePanel;
import org.windowkillproject.model.abilities.BulletModel;
import org.windowkillproject.model.abilities.VertexModel;
import org.windowkillproject.model.Drawable;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.UUID;

import static org.windowkillproject.controller.Controller.createEntityView;
import static org.windowkillproject.model.abilities.VertexModel.UNIT_DEGREE;

public abstract class EntityModel implements Drawable {
    protected int x, y, width, height;
    private boolean isImpact = false;

    public boolean isImpact() {
        return isImpact;
    }

    public void setImpact(boolean impact) {
        isImpact = impact;
    }

    private int hp, meleeAttackHp;
    private double theta;
    private GamePanel localPanel;

    public GamePanel getLocalPanel() {
        return localPanel;
    }

    public void setLocalPanel(GamePanel localPanel) {
        this.localPanel = localPanel;
    }

    protected ArrayList<VertexModel> vertices;

    public String getId() {
        return id;
    }

    public void setVertices(ArrayList<VertexModel> vertices) {
        this.vertices = vertices;
    }

    public ArrayList<VertexModel> getVertices() {
        return vertices;
    }
    public ArrayList<Point2D> getPointVertices() {
        ArrayList<Point2D> v = new ArrayList<>();
        for (VertexModel vertexModel : vertices){
            v.add(new Point2D.Double(vertexModel.getX(), vertexModel.getY()));
        }
        return v;
    }

    private int radius, reduceCount;
    public static ArrayList<EntityModel> entityModels=new ArrayList<>();

    public abstract void route(); //if entity is local, implement is also local

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
    public abstract Point2D getRoutePoint();

    public Point2D getAnchor() {
        return anchor;
    }

    public void setAnchor(Point2D anchor) {
        this.anchor = anchor;
    }
    public void setAnchor (double x, double y){
        anchor.setLocation(x , y);
    }


    public int getMeleeAttackHp() {
        return meleeAttackHp;
    }

    public void setMeleeAttackHp(int meleeAttackHp) {
        this.meleeAttackHp = meleeAttackHp;
    }

    public void gotHit(int attackHp) {
        setHp(getHp() - attackHp);
        if (getHp() <= 0) {
            setHp(0);
            destroy();
        }
        SoundPlayer.playHitSound();
    }

    public void gotShoot(){
        setHp(getHp() - BulletModel.getAttackHp());
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
        rotate(theta);
    }

    public void rotate(double theta){
        if (getVertices() != null) {
            for (VertexModel vertexModel : getVertices()) {
                vertexModel.rotate(theta);
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
        reduceTheta();
    }
    public void reduceTheta(){
        reduceCount++;
        if (reduceCount%5==0) {
            int i = 1;
            if (theta > 0) i = -1;
            if (theta != 0) theta += i * UNIT_DEGREE;
            if (theta * i > 0) theta = 0;
        }
    }

    public double getTheta() {
        return theta;
    }

    public void setTheta(double theta){
        this.theta = theta;
    }

    private void moveX(int deltaX) {
        setX(getX() + deltaX);
        setXO(getXO() + deltaX);
        for (VertexModel vertexModel : getVertices()) {
            vertexModel.setX(vertexModel.getX() + deltaX);
        }
    }

    private void moveY(int deltaY) {
        setY(getY() + deltaY);
        setYO(getYO() + deltaY);
        for (VertexModel vertexModel : getVertices()) {
            vertexModel.setY(vertexModel.getY() + deltaY);
        }
    }

}
