package org.windowkillproject.application.panels.game;

import org.windowkillproject.application.Application;
import org.windowkillproject.application.Config;
import org.windowkillproject.application.panels.Panel;
import org.windowkillproject.model.entities.EntityModel;
import org.windowkillproject.model.entities.EpsilonModel;
import org.windowkillproject.model.entities.enemies.OmenoctModel;
import org.windowkillproject.model.entities.enemies.minibosses.BarricadosModel;
import org.windowkillproject.view.entities.EntityView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.windowkillproject.application.Application.getGameFrame;
import static org.windowkillproject.application.Config.*;
import static org.windowkillproject.application.Config.FRAME_STRETCH_SPEED;
import static org.windowkillproject.application.panels.game.PanelStatus.isometric;
import static org.windowkillproject.controller.GameController.keepEpsilonInBounds;
import static org.windowkillproject.model.entities.EntityModel.entityModels;
import static org.windowkillproject.model.entities.enemies.minibosses.BarricadosModel.barricadosModels;
import static org.windowkillproject.view.entities.EntityView.entityViews;

public abstract class GamePanel extends Panel {

    public PanelStatus getPanelStatus() {
        return panelStatus;
    }

    private boolean isStretching = false;
    protected PanelStatus panelStatus;
    public static ArrayList<GamePanel> gamePanels = new ArrayList<>();
    private boolean exploding = false;
    public static Map<GamePanel, Rectangle> gamePanelsBounds = new HashMap<>();


    public GamePanel(PanelStatus panelStatus) {
        super();
        setBackground(Color.black);
        setFocusable(true);
        requestFocusInWindow();
        gamePanels.add(this);
        gamePanelsBounds.put(this, getBounds());
        this.panelStatus = panelStatus;
//        addComponentListener(new ComponentAdapter() {
//            @Override
//            public void componentResized(ComponentEvent e) {
//                super.componentResized(e);
//                updateEntities();
//            }
//
//            @Override
//            public void componentMoved(ComponentEvent e) {
//                super.componentMoved(e);
//                updateEntities();
//            }
//        });
    }

    @Override
    protected ArrayList<Component> initComponents() {
        return null;
    }

    private void updateEntities() {
        for (EntityModel entityModel : entityModels) {
            if (entityModel.getLocalPanel().equals(this)) {
                System.out.println("okay im doing something");
                entityModel.setLocalPanel(this);
//                keepEpsilonInBounds();
            }
        }
    }

    public void resetCanShrink() {
        canShrinkUp = true;
        canShrinkDown = true;
        canShrinkLeft = true;
        canShrinkRight = true;
    }

    private boolean canShrinkUp = true,
            canShrinkDown = true,
            canShrinkLeft = true,
            canShrinkRight = true;


    public void setCanShrinkUp(boolean canShrinkUp) {
        this.canShrinkUp = canShrinkUp;
    }

    public void setCanShrinkDown(boolean canShrinkDown) {
        this.canShrinkDown = canShrinkDown;
    }

    public void setCanShrinkLeft(boolean canShrinkLeft) {
        this.canShrinkLeft = canShrinkLeft;
    }

    public void setCanShrinkRight(boolean canShrinkRight) {
        this.canShrinkRight = canShrinkRight;
    }

    public void shrink() {
        if (panelStatus.equals(PanelStatus.shrinkable)) {
            int newX = getX();
            if (canShrinkLeft) newX += Config.FRAME_SHRINKAGE_SPEED / 2;
            int newY = getY();
            if (canShrinkUp) newY += Config.FRAME_SHRINKAGE_SPEED / 2;
            int newWidth = getWidth();
            if (canShrinkRight) newWidth -= Config.FRAME_SHRINKAGE_SPEED;
            int newHeight = getHeight();
            if (canShrinkDown) newHeight -= Config.FRAME_SHRINKAGE_SPEED;

//            if (this instanceof MainGamePanel ||
//                    (EpsilonModel.getINSTANCE().getLocalPanel()!= null &&
//                            this.equals(EpsilonModel.getINSTANCE().getLocalPanel()))) {
            if (getWidth() <= Config.GAME_MIN_SIZE) {
                newWidth = Config.GAME_MIN_SIZE;
                newX = getX();
            }
            if (getHeight() <= Config.GAME_MIN_SIZE) {
                newHeight = Config.GAME_MIN_SIZE;
                newY = getY();
            }
//            }
            if (!isStretching) {
                this.setBounds(newX, newY, newWidth, newHeight);
                gamePanelsBounds.put(this, new Rectangle(newX, newY, newWidth, newHeight));
            }
        }
    }

    public void setExploding(boolean exploding) {
        this.exploding = exploding;
    }


    public void stretch(int code) {
        if (panelStatus.equals(PanelStatus.shrinkable)) {

            AtomicInteger count = new AtomicInteger();
            Timer stretchTimer = new Timer(Config.FPS, null);
            stretchTimer.addActionListener(e -> {
                int newX = getX();
                int newY = getY();
                int newWidth = getWidth();
                int newHeight = getHeight();
                switch (code) {
                    case DOWN_CODE -> {
                        newY += FRAME_STRETCH_SPEED / 2;
                        newHeight += FRAME_STRETCH_SPEED;
                    }
                    case LEFT_CODE -> {
                        newX -= FRAME_STRETCH_SPEED;
                        newWidth += FRAME_STRETCH_SPEED;
                    }
                    case RIGHT_CODE -> {
                        newX += FRAME_STRETCH_SPEED / 2;
                        newWidth += FRAME_STRETCH_SPEED;
                    }
                    case UP_CODE -> {
                        newY -= FRAME_STRETCH_SPEED;
                        newHeight += FRAME_STRETCH_SPEED;
                    }
                    default -> throw new IllegalStateException("Unexpected value: " + code);
                }
                if (count.get() < 7) {
                    isStretching = true;
                    if (!isStoppedByBarricados(code, newX,newY,newWidth,newHeight)) {
//                        fixEntityPositionsInShotPanel(code); todo ?
                        setBounds(newX, newY, newWidth, newHeight);
                        gamePanelsBounds.put(this, new Rectangle(newX, newY, newWidth, newHeight));
                    }
                    count.getAndIncrement();
                } else {
                    isStretching = false;
                    stretchTimer.stop();
                }
            });
            stretchTimer.start();
        }
    }
    private boolean isStoppedByBarricados(int code, int newX, int newY, int newWidth, int newHeight) {
        for (int i = 0; i < barricadosModels.size(); i++) {
            var barricadosModel = barricadosModels.get(i);
            if (!barricadosModel.getLocalPanel().getPanelStatus().equals(isometric)) {
                continue;
            }
            int BX = barricadosModel.getX();
            int BY = barricadosModel.getY();
            int BW = barricadosModel.getWidth();
            int BH = barricadosModel.getHeight();

            boolean xOverlap = newX < BX + BW && newX + newWidth > BX;
            boolean yOverlap = newY < BY + BH && newY + newHeight > BY;

            switch (code) {
                case DOWN_CODE -> {
                    if (xOverlap && newY + newHeight > BY) {
                        return true;
                    }
                }
                case LEFT_CODE -> {
                    if (yOverlap && newX < BX + BW) {
                        return true;
                    }
                }
                case RIGHT_CODE -> {
                    if (yOverlap && newX + newWidth > BX) {
                        return true;
                    }
                }
                case UP_CODE -> {
                    if (xOverlap && newY < BY + BH) {
                        return true;
                    }
                }
                default -> throw new IllegalStateException("Unexpected value: " + code);
            }
        }
        return false;
    }


    private void fixEntityPositionsInShotPanel(int code) {
        if (code == LEFT_CODE || code == UP_CODE) {
            for (EntityModel entityModel : entityModels) {
                if (/*!(entityModel instanceof OmenoctModel ||*/
                        !(entityModel instanceof BarricadosModel) &&
                        entityModel.getLocalPanel() != null &&
                        entityModel.getLocalPanel().equals(this)) {
                    if (code == LEFT_CODE) {
                        entityModel.move(FRAME_STRETCH_SPEED, 0);
                    }
                    if (code == UP_CODE) {
                        entityModel.move(0, FRAME_STRETCH_SPEED);
                    }
                }
            }
        }
    }

    public boolean isExploding() {
        return exploding;
    }

    public void shrinkFast() {
        if (exploding) {
            EpsilonModel.getINSTANCE().setRadius(0);
            Config.GAME_MIN_SIZE = 10;
        }
        Timer shrinkFastTimer = new Timer(1, null);
        shrinkFastTimer.addActionListener(e -> {
            int newX = getX() + Config.FRAME_SHRINKAGE_SPEED * 3 / 2;
            int newY = getY() + Config.FRAME_SHRINKAGE_SPEED * 3 / 2;
            int newWidth = getWidth() - Config.FRAME_SHRINKAGE_SPEED * 3;
            int newHeight = getHeight() - Config.FRAME_SHRINKAGE_SPEED * 3;
            boolean stoppedX = false, stoppedY = false;
            if (getWidth() <= Config.GAME_MIN_SIZE) {
                newWidth = Config.GAME_MIN_SIZE;
                newX = getX();
                stoppedX = true;
            }
            if (getHeight() <= Config.GAME_MIN_SIZE) {
                newHeight = Config.GAME_MIN_SIZE;
                newY = getY();
                stoppedY = true;
            }
            //keep epsilon in the middle
            if (!(stoppedX && stoppedY)) {
                setBounds(newX, newY, newWidth, newHeight);
                gamePanelsBounds.put(this, new Rectangle(newX, newY, newWidth, newHeight));

//                EpsilonModel epsilonModel = EpsilonModel.getINSTANCE();
//                int deltaX = newWidth / 2 - epsilonModel.getXO() - epsilonModel.getRadius();
//                int deltaY = newHeight / 2 - epsilonModel.getYO() - epsilonModel.getRadius();
//                epsilonModel.move(deltaX, deltaY);
            } else {
                if (exploding) {
                    setVisible(false);
//                    initScoreFrame();
                    EpsilonModel.getINSTANCE().setRadius(Config.EPSILON_RADIUS);
                    Config.GAME_MIN_SIZE = 250;
                    Application.startGame(1);
                }
                shrinkFastTimer.stop();
            }
        });
        shrinkFastTimer.start();

    }

    public void endingScene() {
        Timer endingTimer = new Timer(10, null);

        ActionListener actionListener = e -> {
            EpsilonModel epsilonModel = EpsilonModel.getINSTANCE();
            if (epsilonModel.getRadius() < getGameFrame().getWidth() / 2 ||
                    epsilonModel.getRadius() < getGameFrame().getHeight() / 2) {
                epsilonModel.setRadius(epsilonModel.getRadius() + 6);
            } else {

                setExploding(true);
                shrinkFast();
                endingTimer.stop();
            }
        };
        endingTimer.addActionListener(actionListener);
        endingTimer.start();

    }
}
