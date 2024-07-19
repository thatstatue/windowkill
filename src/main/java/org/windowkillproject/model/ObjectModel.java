package org.windowkillproject.model;

import org.windowkillproject.application.panels.game.GamePanel;

import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

import static org.windowkillproject.application.Config.LOCK;

public abstract class ObjectModel implements Drawable, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    protected int x, y, width, height;
    protected String id;
    public static ArrayList<ObjectModel> objectModels = new ArrayList<>();

    protected Point2D anchor;
    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject(); // Serialize the serializable fields
    }
    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject(); // Deserialize the serializable fields
    }

    private transient Area allowedArea = new Area();

    public Area getAllowedArea() {
        return allowedArea;
    }

    public void setAllowedArea(Area allowedArea) {
        this.allowedArea = allowedArea;
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
    private GamePanel localPanel;

    public GamePanel getLocalPanel() {
        return localPanel;
    }

    public void setLocalPanel(GamePanel localPanel) {
        this.localPanel = localPanel;
    }


    protected ObjectModel(GamePanel localPanel,int x, int y) {
        this.x = x;
        this.y = y;
        this.localPanel = localPanel;
        synchronized (LOCK) {
            objectModels.add(this);
        }
        this.id = UUID.randomUUID().toString();
        anchor = new Point2D.Double(x, y);
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
