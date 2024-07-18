package org.windowkillproject.model.entities;

import org.windowkillproject.application.SoundPlayer;
import org.windowkillproject.application.panels.game.GamePanel;
import org.windowkillproject.model.ObjectModel;
import org.windowkillproject.model.Transferable;
import org.windowkillproject.model.abilities.BulletModel;
import org.windowkillproject.model.abilities.VertexModel;
import org.windowkillproject.model.entities.enemies.attackstypes.NonRotatable;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import static org.windowkillproject.application.Config.UNIT_DEGREE;
import static org.windowkillproject.application.panels.game.GamePanel.gamePanelsBounds;


public abstract class EntityModel extends ObjectModel implements Transferable {

    private boolean isImpact = false;

    public boolean isImpact() {
        return isImpact;
    }

    public void setImpact(boolean impact) {
        isImpact = impact;
    }

    private int hp, meleeAttackHp;
    private double theta;

    protected ArrayList<VertexModel> vertices;

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
    private ArrayList<GamePanel> allowedPanels = new ArrayList<>();
    private int radius, reduceCount, width, height;

    public ArrayList<GamePanel> getAllowedPanels() {
        return allowedPanels;
    }

    public void setAllowedPanels(ArrayList<GamePanel> allowedPanels) {
        this.allowedPanels = allowedPanels;
    }

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

    public void addToAllowedArea(GamePanel panel){
        allowedPanels.add(panel);
        Area area = new Area(gamePanelsBounds.get(panel));
        getAllowedArea().add(area); //todo daijovah?
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
    public Area getArea(){
        return new Area(new Rectangle(getX(),getY(),getWidth(),getHeight()));
    }
    public void gotShot(){
        setHp(getHp() - BulletModel.getAttackHp());
        if (getHp() <= 0) {
            setHp(0);
            destroy();
        }
    }

    public void destroy() {
        objectModels.remove(this);
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

    public void rotate() {
        rotate(theta);
    }

    public void rotate(double theta){
        if (!(this instanceof  NonRotatable || getVertices() == null)) {
            for (VertexModel vertexModel : getVertices()) {
                vertexModel.rotate(theta);
            }
        }
    }

    protected EntityModel(GamePanel localPanel, int x, int y, int radius, int hp, int meleeAttackHp) {
        super(localPanel, x, y );

        this.hp = hp;
        this.meleeAttackHp = meleeAttackHp;
        vertices = new ArrayList<>();
        entityModels.add(this);
        setRadius(radius);
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
