package org.windowkillproject.server.model.entities.enemies.finalboss;

import org.windowkillproject.server.model.globe.GlobeModel;
import org.windowkillproject.server.model.panelmodels.InternalPanelModel;
import org.windowkillproject.server.model.panelmodels.PanelStatus;
import org.windowkillproject.controller.Utils;
import org.windowkillproject.server.model.abilities.VertexModel;
import org.windowkillproject.server.model.entities.enemies.EnemyModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.concurrent.atomic.AtomicInteger;


import static org.windowkillproject.Constants.ATTACK_TIMEOUT;
import static org.windowkillproject.Constants.FPS;
import static org.windowkillproject.server.Config.*;
import static org.windowkillproject.controller.Utils.globalRoutePoint;


public class PunchFistModel extends EnemyModel {
    private boolean on;
    public PunchFistModel(GlobeModel globeModel, int x, int y) {
        super(globeModel, null, x, y, HAND_RADIUS, Integer.MAX_VALUE, 10, 0, 0);
        setLocalPanelModel(new InternalPanelModel(globeModel, new Rectangle(x, y, HAND_RADIUS*3, HAND_RADIUS*3),
                PanelStatus.isometric , true
        ));
        targetEpsilon = globeModel.getSmileyHeadModel().getTargetEpsilon();
        initVertices();
        initPolygon();
//        globeModel.getGlobeController().createEntityView(getId(), getX(),getY(),getWidth(),getHeight());
        on = true;
    }

    public boolean isOn() {
        return on;
    }

    @Override
    public void route() {
        getLocalPanelModel().setX((int) (getXO()-getRadius()*1.5));
        getLocalPanelModel().setY((int) (getYO()-getRadius()*1.5));

    }

    @Override
    public Point2D getRoutePoint() {
        return globalRoutePoint(this.getAnchor(),
                targetEpsilon.getAnchor());

    }
    public void tightenEpsilonPanel(){
        getLocalPanelModel().setFlexible(false);
        Timer punch = new Timer(FPS/4, null);
        AtomicInteger integer = new AtomicInteger();
        ActionListener punchListener = e -> {

            if (!Utils.isTransferableInBounds(this,targetEpsilon.getAllowedArea() , true)) {
                move((int) getRoutePoint().getX(), (int) getRoutePoint().getY());
            }else{
                integer.getAndIncrement();
                if (integer.get()<4){
                    globeModel.getMainPanelModel().gotPunched(getAnchor());
                    move((int) -getRoutePoint().getX(), (int) -getRoutePoint().getY());
                }else if (integer.get()>ATTACK_TIMEOUT){
                    globeModel.getSmileyHeadModel().setPunching(false);
                    globeModel.getSmileyHeadModel().setVulnerable(false);
                    getLocalPanelModel().setFlexible(true);
                    punch.stop();
                }
            }
        };
        punch.addActionListener(punchListener);
        punch.start();
    }

    public void slap(){
        getLocalPanelModel().setFlexible(true);
        Timer slap = new Timer(FPS/4, null);
        int hp = targetEpsilon.getHp();
        AtomicInteger integer = new AtomicInteger();

        ActionListener punchListener = e -> {
            if (targetEpsilon.getHp()== hp) {
                move((int) getRoutePoint().getX(), (int) getRoutePoint().getY());
            }else{
                integer.getAndIncrement();
                if (integer.get()<2){
                    move((int) -getRoutePoint().getX(), (int) -getRoutePoint().getY());

                }else if (integer.get()> ATTACK_TIMEOUT){
                    globeModel.getSmileyHeadModel().setSlapping(false);
                    globeModel.getSmileyHeadModel().setVulnerable(false);
                    slap.stop();
                }
            }
        };
        slap.addActionListener(punchListener);
        slap.start();
    }

    @Override
    protected void initVertices() {
        int halfSideLength = (int) (getRadius() / Math.sqrt(2)+14);


        getVertices().add(new VertexModel(getXO() - halfSideLength, getYO() - halfSideLength, this));
        getVertices().add(new VertexModel(getXO() + halfSideLength, getYO() - halfSideLength, this));
        getVertices().add(new VertexModel(getXO() + halfSideLength, getYO() + halfSideLength, this));
        getVertices().add(new VertexModel(getXO() - halfSideLength, getYO() + halfSideLength, this));

    }
}
