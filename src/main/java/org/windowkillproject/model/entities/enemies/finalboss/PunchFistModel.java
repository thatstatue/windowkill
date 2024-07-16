package org.windowkillproject.model.entities.enemies.finalboss;

import org.windowkillproject.application.panels.game.InternalGamePanel;
import org.windowkillproject.application.panels.game.PanelStatus;
import org.windowkillproject.model.abilities.VertexModel;
import org.windowkillproject.model.entities.EpsilonModel;
import org.windowkillproject.model.entities.enemies.EnemyModel;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.concurrent.atomic.AtomicInteger;


import static org.windowkillproject.application.Application.getGameFrame;
import static org.windowkillproject.application.Config.*;
import static org.windowkillproject.controller.Controller.createEntityView;
import static org.windowkillproject.controller.Utils.globalRoutePoint;
import static org.windowkillproject.controller.Utils.isTransferableInBounds;

public class PunchFistModel extends EnemyModel {
    private static boolean on;
    public PunchFistModel(int x, int y) {
        super(null, x, y, HAND_RADIUS, Integer.MAX_VALUE, 10, 0, 0);
        setLocalPanel(new InternalGamePanel(x, y, HAND_RADIUS*3, HAND_RADIUS*3,
                PanelStatus.isometric , true
        ));

        initVertices();
        initPolygon();
        createEntityView(getId(), getX(),getY(),getWidth(),getHeight());
        on = true;
    }

    public static boolean isOn() {
        return on;
    }

    @Override
    public void route() {
        getLocalPanel().setLocation((int) (getXO()-getRadius()*1.5), (int) (getYO()-getRadius()*1.5));

    }

    @Override
    public Point2D getRoutePoint() {
        return globalRoutePoint(this.getAnchor(),
                EpsilonModel.getINSTANCE().getAnchor());

    }
    public void tightenEpsilonPanel(){
        getLocalPanel().setFlexible(false);
        Timer punch = new Timer(FPS/4, null);
        ActionListener punchListener = e -> {
            AtomicInteger integer = new AtomicInteger();
            if (!isTransferableInBounds(this, EpsilonModel.getINSTANCE().getAllowedArea() , true)) {
                move((int) getRoutePoint().getX(), (int) getRoutePoint().getY());
            }else{
                integer.getAndIncrement();
                if (integer.get()<2){
                    getGameFrame().getMainGamePanel().gotPunched(getAnchor());
                    System.out.println("MANFI MIRAM"); //todo debug
                    move((int) -getRoutePoint().getX(), (int) -getRoutePoint().getY());
                }else if (integer.get()>ATTACK_TIMEOUT){
                    SmileyHeadModel.setPunching(false);
                    SmileyHeadModel.getInstance().setVulnerable(false);
                    getLocalPanel().setFlexible(true);
                    punch.stop();
                }
            }
        };
        punch.addActionListener(punchListener);
        punch.start();
    }

    public void slap(){
        getLocalPanel().setFlexible(true);
        Timer slap = new Timer(FPS/4, null);
        int hp = EpsilonModel.getINSTANCE().getHp();
        ActionListener punchListener = e -> {
            AtomicInteger integer = new AtomicInteger();
            if (EpsilonModel.getINSTANCE().getHp()== hp) {
                move((int) getRoutePoint().getX(), (int) getRoutePoint().getY());
            }else{
                integer.getAndIncrement();
                if (integer.get()<2){
                    System.out.println("MANFI MIRAM");
                    move((int) -getRoutePoint().getX(), (int) -getRoutePoint().getY());

                }else if (integer.get()> ATTACK_TIMEOUT){
                    SmileyHeadModel.setSlapping(false);
                    SmileyHeadModel.getInstance().setVulnerable(false);
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
