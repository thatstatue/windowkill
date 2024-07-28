package org.windowkillproject.server.model;

import org.windowkillproject.server.ClientHandlerTeam;
import org.windowkillproject.server.MessageQueue;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.windowkillproject.Request.LOCK;

public abstract class ObjectModel implements Drawable, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    protected int x, y, width, height;
    protected String id;
    public static Map<ClientHandlerTeam, ArrayList<ObjectModel>> objectModels = new HashMap<>();

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
    private PanelModel localPanelModel;
    private final ClientHandlerTeam team;

    public ClientHandlerTeam getTeam() {
        return team;
    }

    public PanelModel getLocalPanelModel() {
        return localPanelModel;
    }

    public void setLocalPanelModel(PanelModel localPanelModel) {
        this.localPanelModel = localPanelModel;
    }


    protected ObjectModel(ClientHandlerTeam team, PanelModel localPanelModel, int x, int y) {
        this.x = x;
        this.y = y;
        this.localPanelModel = localPanelModel;

        this.team = team;
        var objModels = objectModels.get(team);
        synchronized (LOCK) {
            if (objModels == null) objModels = new ArrayList<>();
            objModels.add(this);
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
    private MessageQueue messageQueue;


    public void performAction(String message) {
        // Some game logic
        messageQueue.enqueue(message);
    }
}
