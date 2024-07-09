package org.windowkillproject.model;

import org.windowkillproject.application.panels.game.GamePanel;

import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.UUID;

import static org.windowkillproject.application.panels.game.GamePanel.gamePanels;

public abstract class ObjectModel implements Drawable {
    protected int x, y;
    protected String id;


    protected Point2D anchor;


    private Area allowedArea = new Area();

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
        for (int i = 0 ; i < gamePanels.size(); i++){
            GamePanel gamePanel = gamePanels.get(i);
            if (localPanel.equals(gamePanel))
                return gamePanel;
        }
        System.out.println("equals none of panels!!!!!!!!!!!!!!");
        return localPanel;
    }

    public void setLocalPanel(GamePanel localPanel) {
        this.localPanel = localPanel;
    }


    protected ObjectModel(GamePanel localPanel,int x, int y) {
        this.x = x;
        this.y = y;
        this.localPanel = localPanel;

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
}
