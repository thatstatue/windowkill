package org.windowkillproject.server.model;

import org.windowkillproject.server.model.globe.GlobeModel;
import org.windowkillproject.server.model.panelmodels.PanelModel;

import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.io.*;
import java.util.ArrayList;
import java.util.UUID;

import static org.windowkillproject.Request.*;
import static org.windowkillproject.server.model.globe.GlobesManager.getGlobeFromId;

public abstract class ObjectModel implements Drawable, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    protected int x, y, width, height;
    protected final String id;
    protected Point2D anchor;
    protected String globeId;

    public String getGlobeId() {
        return globeId;
    }

    public GlobeModel getGlobeModel() {
        return getGlobeFromId(globeId);
    }

    private transient Area allowedArea = new Area();
    private PanelModel localPanelModel;


    protected ObjectModel(String globeId, PanelModel localPanelModel, int x, int y) {
        this.x = x;
        this.y = y;
        this.localPanelModel = localPanelModel;
        this.globeId = globeId;
       // initGlobeModel(); //todo ??
        this.id = UUID.randomUUID().toString();
        anchor = new Point2D.Double(x, y);
    }

    private void initGlobeModel() {
        synchronized (LOCK) {
            if (getGlobeModel().getObjectModels() == null) getGlobeModel().setObjectModels(new ArrayList<>());
            getGlobeModel().getObjectModels().add(this);
        }
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject(); // Serialize the serializable fields
    }
    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject(); // Deserialize the serializable fields
    }

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
    public PanelModel getLocalPanelModel() {
        return localPanelModel;
    }

    public void setLocalPanelModel(PanelModel localPanelModel) {
        this.localPanelModel = localPanelModel;
    }


    public String getId() {
        return id;
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
    public void destroy(){
        synchronized (LOCK) {
            getGlobeModel().broadcast(REQ_REMOVE_OBJECT+ REGEX_SPLIT + id);
            getGlobeModel().getObjectModels().remove(this);
        }
    }


}
