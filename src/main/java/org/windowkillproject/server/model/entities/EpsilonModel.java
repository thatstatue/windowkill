package org.windowkillproject.server.model.entities;


import org.windowkillproject.server.ClientHandlerTeam;
import org.windowkillproject.server.ClientHandler;
import org.windowkillproject.server.Config;
import org.windowkillproject.client.ui.panels.game.MainGamePanel;
import org.windowkillproject.server.model.abilities.VertexModel;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

import static org.windowkillproject.Request.REQ_ARE_KEYS_PRESSED;
import static org.windowkillproject.server.Config.*;
import static org.windowkillproject.controller.Controller.createEntityView;

public class EpsilonModel extends EntityModel {
    public static Map<ClientHandler, EpsilonModel> clientEpsilonModelMap = new HashMap<>();
    private boolean astrapper, melame, chironner;
    private final ClientHandler clientHandler;

    public boolean isChironner() {
        return chironner;
    }

    public void setChironner(boolean chironner) {
        this.chironner = chironner;
    }

    public boolean isMelame() {
        return melame;
    }

    public void setMelame(boolean melame) {
        this.melame = melame;
    }

    public boolean isAstrapper() {
        return astrapper;
    }

    public void setAstrapper(boolean astrapper) {
        this.astrapper = astrapper;
    }

    public EpsilonModel getINSTANCE() {
        return clientEpsilonModelMap.get(getTeam().first());
    }

    public static void newINSTANCE(ClientHandlerTeam team) {
        clientEpsilonModelMap.put(team.first(),  new EpsilonModel(team, EPSILON_HP, 0));
        clientEpsilonModelMap.put(team.second(),  new EpsilonModel(team, EPSILON_HP, 0));
    }

    private int xp;


    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public void setLeftPressed(boolean leftPressed) {
        isLeftPressed = leftPressed;
    }

    public void setRightPressed(boolean rightPressed) {
        isRightPressed = rightPressed;
    }

    public void setDownPressed(boolean downPressed) {
        isDownPressed = downPressed;
    }

    public void setUpPressed(boolean upPressed) {
        isUpPressed = upPressed;
    }

    private boolean isLeftPressed, isRightPressed,isDownPressed, isUpPressed;

    public void route() {
        EpsilonModel eM = getINSTANCE();
        clientHandler.sendMessage(REQ_ARE_KEYS_PRESSED);

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
    private EpsilonModel(ClientHandlerTeam team, int hp, int xp) {
        super(team, MainGamePanel.getInstance(),
                CENTER_X-EPSILON_RADIUS,
                CENTER_Y- EPSILON_RADIUS,
                EPSILON_RADIUS, hp, 10);
        clientHandler = team.first();
        setXp(xp);
        createEntityView(getId(), getX(),getY(),getWidth(),getHeight());
    }

    public void spawnVertex() {
        vertices.add(new VertexModel(getXO(), getYO() - getRadius(), this));
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
        initScoreFrame(false);
    }

    public void collected(int rewardXp) {
        setXp(getXp() + rewardXp);
        getGameFrame().setXpAmount(getXp());
    }
}
