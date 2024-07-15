package org.windowkillproject.model.entities;


import org.windowkillproject.application.Config;
import org.windowkillproject.model.abilities.VertexModel;

import java.awt.geom.Point2D;

import static org.windowkillproject.application.Application.getGameFrame;
import static org.windowkillproject.application.Application.initScoreFrame;
import static org.windowkillproject.application.Config.*;
import static org.windowkillproject.application.listeners.EpsilonKeyListener.*;
import static org.windowkillproject.controller.Controller.createEntityView;

public class EpsilonModel extends EntityModel {
    private static EpsilonModel INSTANCE;

    public static EpsilonModel getINSTANCE() {

        if (INSTANCE == null) INSTANCE = new EpsilonModel(CENTER_X / 2, CENTER_Y / 2, 0);
        return INSTANCE;
    }

    public static void newINSTANCE() {
        int xp = 0;
        if (INSTANCE != null) xp = INSTANCE.getXp();
        INSTANCE = new EpsilonModel(CENTER_X -5 , CENTER_Y- 5, xp);
        INSTANCE.setRadius(EPSILON_RADIUS);
    }

    private int xp;


    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public void route() {
        EpsilonModel eM = getINSTANCE();//todo getGameFrame().getWidth()

        int endX = eM.getWidth() + eM.getX();
        int endY = eM.getHeight() + eM.getY();
        if (!((isLeftPressed && isRightPressed) || (isDownPressed && isUpPressed))) {
            if (isUpPressed && eM.getY() - Config.EPSILON_SPEED >= 0)
                eM.move(0, -Config.EPSILON_SPEED);
            else if (isDownPressed && endY + Config.EPSILON_SPEED <= getGameFrame().getHeight())
                eM.move(0, Config.EPSILON_SPEED);
            if (isLeftPressed && eM.getX() - Config.EPSILON_SPEED >= 0)
                eM.move(-Config.EPSILON_SPEED, 0);
            else if (isRightPressed && endX + Config.EPSILON_SPEED <= getGameFrame().getWidth())
                eM.move(Config.EPSILON_SPEED, 0);

        }
    }

    private EpsilonModel(int x, int y, int xp) {
        super(getGameFrame().getMainGamePanel(), x, y, EPSILON_RADIUS, 1000, 10);
        setXp(xp);
        createEntityView(getId(), getX(),getY(),getWidth(),getHeight());
    }

    public void spawnVertex() {
        vertices.add(new VertexModel(getXO(), getYO() - getRadius(), this));
        System.out.println(vertices.size());
        double theta = Math.PI * 2 / (vertices.size() * 1D);
        for (int i = 0; i < vertices.size(); i++) {
            VertexModel vertexModel = vertices.get(i);
            vertexModel.setX(getXO());
            vertexModel.setY(getYO() - getRadius());
            vertexModel.rotate(i * theta);
        }
    }

    @Override
    public Point2D getRoutePoint() {
        Point2D point2D = new Point2D.Double(0, 0);
        if (!((isLeftPressed && isRightPressed) || (isDownPressed && isUpPressed))) {
            if (isUpPressed)
                point2D.setLocation(point2D.getX(), -2 * EPSILON_SPEED);
            if (isDownPressed)
                point2D.setLocation(point2D.getX(), 2 * EPSILON_SPEED);
            if (isLeftPressed)
                point2D.setLocation(-2 * EPSILON_SPEED, point2D.getY());
            if (isRightPressed)
                point2D.setLocation(2 * EPSILON_SPEED, point2D.getY());
        }
        return point2D;
    }

    @Override
    public void gotHit(int attackHp) {
        super.gotHit(attackHp);
    }

    @Override
    public void destroy() {
        super.destroy();
        initScoreFrame();
    }

    public void collected(int rewardXp) {
        setXp(getXp() + rewardXp);
        getGameFrame().setXpAmount(getXp());
    }
}
