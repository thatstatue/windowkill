package org.windowkillproject.model.entities.enemies.finalboss;

import org.windowkillproject.application.Config;
import org.windowkillproject.application.panels.game.InternalGamePanel;
import org.windowkillproject.application.panels.game.MainGamePanel;
import org.windowkillproject.application.panels.game.PanelStatus;
import org.windowkillproject.model.abilities.ProjectileModel;
import org.windowkillproject.model.abilities.VertexModel;
import org.windowkillproject.model.entities.EpsilonModel;
import org.windowkillproject.model.entities.enemies.EnemyModel;
import org.windowkillproject.model.entities.enemies.attackstypes.NonRotatable;
import org.windowkillproject.model.entities.enemies.attackstypes.ProjectileOperator;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import static org.windowkillproject.application.Application.getGameFrame;
import static org.windowkillproject.application.Config.*;
import static org.windowkillproject.application.panels.game.GamePanel.gamePanelsBounds;
import static org.windowkillproject.controller.Controller.createEntityView;
import static org.windowkillproject.controller.Controller.deleteGamePanel;
import static org.windowkillproject.controller.ElapsedTime.getTotalSeconds;
import static org.windowkillproject.controller.Utils.globalRoutePoint;
import static org.windowkillproject.controller.Utils.magnitude;

public class HandModel extends EnemyModel implements ProjectileOperator, NonRotatable {
    private static ArrayList<HandModel> hands = new ArrayList<>();

    protected HandModel(int x, int y) {
        super(null, x, y, Config.HAND_RADIUS, 100, 10, 10, 10);
        setLocalPanel(new InternalGamePanel(x, y, HAND_RADIUS * 3, HAND_RADIUS * 3,
                PanelStatus.isometric, true
        ));
        setBgPanel((InternalGamePanel) getLocalPanel());
        initVertices();
        initPolygon();
        createEntityView(getId(), getX(), getY(), getWidth(), getHeight());
        hands.add(this);

    }

    public void squeeze() {
        if (hands.size() == 2) {
            setVulnerable(false);
            getLocalPanel().setFlexible(false);
            var main = MainGamePanel.getInstance();
            Point2D panelLeft = new Point2D.Double(main.getX()-2, main.getY()+main.getHeight()/2D);
            Point2D panelRight = new Point2D.Double(main.getX()+main.getWidth()+2, main.getY()+main.getHeight()/2D);

            if(this instanceof RightHandModel && getAnchor().distance(panelRight)> getRadius()+3 ||
                    this instanceof LeftHandModel && getAnchor().distance(panelLeft) > getRadius()+3)
                move((int) getRoutePoint().getX(), (int) getRoutePoint().getY());
//            }
        }
    }

    private long lastShot;

    @Override
    public void route() {


        getLocalPanel().setLocation((int) (getXO() - getRadius() * 1.5), (int) (getYO() - getRadius() * 1.5));
    }

    public void projectile() {
        if (!hands.isEmpty()) {
            setVulnerable(true);
            shoot();
            goRoundEpsilon();
        }
    }

    @Override
    public void shoot() {
        if (getTotalSeconds() - lastShot > PROJECTILE_TIMEOUT) {
            new ProjectileModel(getLocalPanel(), this, 4, true, true,
                    Color.yellow, Color.gray).shoot();
            lastShot = getTotalSeconds();
        }
    }

    private double rotationSpeed = UNIT_DEGREE / 4;


    public void setMinusRotationSpeed() {
        this.rotationSpeed = -rotationSpeed;
    }

    private void goRoundEpsilon() {
        var epsilonModel = EpsilonModel.getINSTANCE();
        double degree = Math.atan2(this.getYO() - epsilonModel.getYO(), this.getXO() - epsilonModel.getXO());
        degree += rotationSpeed;
        int finalX = epsilonModel.getXO() + (int) (WYRM_DISTANCE * 0.8 * Math.cos(degree)) - getRadius();
        int finalY = epsilonModel.getYO() + (int) (WYRM_DISTANCE * 0.8 * Math.sin(degree)) - getRadius();
        move(finalX - getX(), finalY - getY());
    }

    @Override
    public Point2D getRoutePoint() {
        int panelWidth = gamePanelsBounds.get(getBgPanel()).width;
        int panelHeight = gamePanelsBounds.get(getBgPanel()).height;
        var bounds = gamePanelsBounds.get(MainGamePanel.getInstance());
        int goX = bounds.x - panelWidth -15;
        if (this instanceof RightHandModel)
            goX += bounds.width + panelWidth + 15;

        int goY = bounds.y + (bounds.height - panelHeight) / 2;
        return globalRoutePoint(new Point2D.Double(getX(), getY()),
                new Point2D.Double(goX, goY), MAX_ENEMY_SPEED*2);
    }

    @Override
    public void destroy() {
        super.destroy();
        hands.remove(this);
        deleteGamePanel(getLocalPanel());
    }

    private boolean vulnerable;

    @Override
    public void gotHit(int attackHp) {
        if (vulnerable) {
            super.gotHit(attackHp);
        }
    }

    @Override
    public void gotShot(int attackHP) {
        if (vulnerable) {
            super.gotShot(attackHP);
        }
    }

    public boolean isVulnerable() {
        return vulnerable;
    }

    public void setVulnerable(boolean vulnerable) {
        this.vulnerable = vulnerable;
    }


    @Override
    protected void initVertices() {
        int halfSideLength = (int) (getRadius() / Math.sqrt(2) + 14);


        getVertices().add(new VertexModel(getXO() - halfSideLength, getYO() - halfSideLength, this));
        getVertices().add(new VertexModel(getXO() + halfSideLength, getYO() - halfSideLength, this));
        getVertices().add(new VertexModel(getXO() + halfSideLength, getYO() + halfSideLength, this));
        getVertices().add(new VertexModel(getXO() - halfSideLength, getYO() + halfSideLength, this));

    }

}
