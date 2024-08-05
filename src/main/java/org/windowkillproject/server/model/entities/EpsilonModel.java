package org.windowkillproject.server.model.entities;


import org.windowkillproject.MessageQueue;
import org.windowkillproject.server.model.globe.GlobeModel;
import org.windowkillproject.server.model.panelmodels.PanelModel;
import org.windowkillproject.server.model.Writ;
import org.windowkillproject.server.model.abilities.VertexModel;
import org.windowkillproject.server.model.globe.GlobesManager;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

import static org.windowkillproject.Constants.*;
import static org.windowkillproject.Request.*;
import static org.windowkillproject.server.Config.*;

public class EpsilonModel extends EntityModel {
    public static Map<MessageQueue, EpsilonModel> queueEpsilonModelMap = new HashMap<>();
    private boolean astrapper, melame, chironner;
    protected MessageQueue messageQueue;

    public MessageQueue getMessageQueue() {
        return messageQueue;
    }

    public static void newINSTANCE(MessageQueue messageQueue ) {
        var globe = GlobesManager.getGlobeFromId(messageQueue.getGlobeId());
        queueEpsilonModelMap.put(messageQueue,  new EpsilonModel(messageQueue, globe, null, EPSILON_HP, 0));//todo is it dangerous to set the local to null?
    }
    private int xp;
    public int panelWidth=GAME_WIDTH, panelHeight=GAME_HEIGHT;
    private boolean isLeftPressed, isRightPressed,isDownPressed, isUpPressed;
    private final Writ writ;
    public void route() {
        performAction(REQ_ARE_KEYS_PRESSED);

        int endX = getWidth() + getX();
        int endY = getHeight() + getY();
        if (!((isLeftPressed && isRightPressed) || (isDownPressed && isUpPressed))) {
            if (isUpPressed && getY() - EPSILON_SPEED >= 0)
                move(0, - EPSILON_SPEED);
            else if (isDownPressed && endY + EPSILON_SPEED <= panelHeight)
                move(0, EPSILON_SPEED);
            if (isLeftPressed && getX() - EPSILON_SPEED >= 0)
                move(-EPSILON_SPEED, 0);
            else if (isRightPressed && endX + EPSILON_SPEED <= panelWidth)
                move(EPSILON_SPEED, 0);

        }
    }
    private EpsilonModel(MessageQueue messageQueue, GlobeModel globeModel, PanelModel localModel, int hp, int xp) {
        super( globeModel, localModel,
                CENTER_X-EPSILON_RADIUS,
                CENTER_Y- EPSILON_RADIUS,
                EPSILON_RADIUS, hp, 10);
        this.messageQueue = messageQueue;
        writ = new Writ(messageQueue);
        setXp(xp);
        globeModel.getGlobeController().createEntityView(getId(), getX(),getY(),getWidth(),getHeight());
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
        performAction(REQ_INIT_SCORE_FRAME + REGEX_SPLIT + false);
    }

    public void collected(int rewardXp) {
        setXp(getXp() + rewardXp);
        performAction(RES_SET_EPSILON_XP + REGEX_SPLIT + getXp());
    }

    public void performAction(String message) {
        messageQueue.enqueue(message);
    }

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
    public Writ getWrit() {
        return writ;
    }

}
