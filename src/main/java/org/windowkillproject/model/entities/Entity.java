package org.windowkillproject.model.entities;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class Entity extends JLabel {
    protected int x, y, width, height;
    protected double theta;
    protected BufferedImage img;
    protected ArrayList<Vertex> vertices;

    public ArrayList<Vertex> getVertices() {
        return vertices;
    }
    private int radius;

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getxO() {
        return xO;
    }

    public void setxO(int xO) {
        this.xO = xO;
    }

    public int getyO() {
        return yO;
    }

    public void setyO(int yO) {
        this.yO = yO;
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
    public void setVertices(ArrayList<Vertex> vertices) {
        this.vertices = vertices;
    }

    public double getTheta() {
        return theta;
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }
    public void rotate(){
        for (Vertex vertex : getVertices()){
            vertex.rotate();
        }
    }

    protected Entity(int x, int y) {
        this.x = x;
        this.y = y;
        vertices = new ArrayList<>();
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

    @Override
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public BufferedImage getImg() {
        return img;
    }

    public void setImg(BufferedImage img) {
        this.img = img;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
