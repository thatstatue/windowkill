package org.windowkillproject.application.panels.game;

import org.windowkillproject.application.Application;
import org.windowkillproject.application.Config;
import org.windowkillproject.application.panels.Panel;
import org.windowkillproject.model.entities.EntityModel;
import org.windowkillproject.model.entities.EpsilonModel;
import org.windowkillproject.model.entities.enemies.minibosses.BarricadosModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Math.*;
import static org.windowkillproject.application.Application.getGameFrame;
import static org.windowkillproject.application.Application.nextLevel;
import static org.windowkillproject.application.Config.*;
import static org.windowkillproject.application.Config.FRAME_STRETCH_SPEED;
import static org.windowkillproject.application.panels.game.PanelStatus.isometric;
import static org.windowkillproject.application.panels.game.PanelStatus.shrinkable;
import static org.windowkillproject.controller.GameController.keepInPanel;
import static org.windowkillproject.controller.Utils.getSign;
import static org.windowkillproject.model.entities.EntityModel.entityModels;
import static org.windowkillproject.model.entities.enemies.minibosses.BarricadosModel.barricadosModels;

public abstract class GamePanel extends Panel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public void setFlexible(boolean flexible) {
        this.flexible = flexible;
    }

    public PanelStatus getPanelStatus() {
        return panelStatus;
    }

    private boolean isStretching = false;
    private boolean flexible = true;
    protected PanelStatus panelStatus;
    public static ArrayList<GamePanel> gamePanels = new ArrayList<>();
    private boolean exploding = false;
    public static Map<GamePanel, Rectangle> gamePanelsBounds = new HashMap<>();


    public GamePanel(PanelStatus panelStatus, boolean flexible) {
        super();
        setBackground(Color.black);
        setFocusable(true);
        requestFocusInWindow();
        gamePanels.add(this);
        gamePanelsBounds.put(this, getBounds());
        this.panelStatus = panelStatus;
        this.flexible = flexible;

    }

    @Override
    protected ArrayList<Component> initComponents() {
        return null;
    }


    public void resetCanShrink() {
        boolean can = !panelStatus.equals(isometric);
        canShrinkUp = can;
        canShrinkDown = can;
        canShrinkLeft = can;
        canShrinkRight = can;
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
        int newX = getX();
        int newY = getY();
        int newWidth = getWidth();
        int newHeight = getHeight();
        if (!(this instanceof MainGamePanel && MainGamePanel.getInstance().isPunched())) {

            if (flexible) {
                if (canShrinkLeft) newX += FRAME_SHRINKAGE_SPEED / 2;
                if (canShrinkUp) newY += FRAME_SHRINKAGE_SPEED / 2;
                if (canShrinkRight) newWidth -= FRAME_SHRINKAGE_SPEED;
                if (canShrinkDown) newHeight -= FRAME_SHRINKAGE_SPEED;
            }

            if (panelStatus.equals(shrinkable)
//                && EpsilonModel.getINSTANCE().getLocalPanel() != null
//                && this.equals(EpsilonModel.getINSTANCE().getLocalPanel())
//                todo: if min size is only for epsilon's panel will uncomment
            ) {
                int minSize = GAME_MIN_SIZE;
                if (getWidth() <= minSize) {
                    newWidth = minSize;
                    newX = getX();
                }
                if (getHeight() <= minSize) {
                    newHeight = minSize;
                    newY = getY();
                }


            }
        }
//            }
        if (!isStretching) {
            this.setBounds(newX, newY, newWidth, newHeight);
            gamePanelsBounds.put(this, new Rectangle(newX, newY, newWidth, newHeight));
        }
    }

    public void setExploding(boolean exploding) {
        this.exploding = exploding;
    }

    public boolean isFlexible() {
        return flexible;
    }

    public void stretch(int code) {
        if (isFlexible()) {
            AtomicInteger count = new AtomicInteger();
            Timer stretchTimer = new Timer(FPS, null);
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
                    if (panelStatus.equals(isometric)) {
                        newWidth = getWidth();
                        newHeight = getHeight();
                    }
                    if (!isStoppedByRigidPanels(code, newX, newY, newWidth, newHeight)) {
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

    public void setForSmiley(boolean forSmiley) {
        this.forSmiley = forSmiley;
    }

    private boolean forSmiley;

    private boolean isStoppedByRigidPanels(int code, int newX, int newY, int newWidth, int newHeight) {
        if (forSmiley) return false;
        for (int i = 0; i < gamePanels.size(); i++) {
            var panel = gamePanels.get(i);
            var panelRectangle = gamePanelsBounds.get(panel);
            if (panel.isFlexible()) {
                continue;
            }
            double BX = panelRectangle.getX();
            double BY = panelRectangle.getY();
            double BW = panelRectangle.getWidth();
            double BH = panelRectangle.getHeight();

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

    public boolean isExploding() {
        return exploding;
    }

    public void shrinkFast() {
        if (exploding) {
            entityModels.remove(EpsilonModel.getINSTANCE());
            GAME_MIN_SIZE = 10;
        }
        Timer shrinkFastTimer = new Timer(1, null);
        shrinkFastTimer.addActionListener(e -> {
            int newX = getX() + FRAME_SHRINKAGE_SPEED * 3 / 2;
            int newY = getY() + FRAME_SHRINKAGE_SPEED * 3 / 2;
            int newWidth = getWidth() - FRAME_SHRINKAGE_SPEED * 3;
            int newHeight = getHeight() - FRAME_SHRINKAGE_SPEED * 3;
            boolean stoppedX = false, stoppedY = false;
            if (getWidth() <= GAME_MIN_SIZE) {
                newWidth = GAME_MIN_SIZE;
                newX = getX();
                stoppedX = true;
            }
            if (getHeight() <= GAME_MIN_SIZE) {
                newHeight = GAME_MIN_SIZE;
                newY = getY();
                stoppedY = true;
            }
            if (!(stoppedX && stoppedY)) {
                setBounds(newX, newY, newWidth, newHeight);
                gamePanelsBounds.put(this, new Rectangle(newX, newY, newWidth, newHeight));
            } else {
                if (exploding) {
                    setVisible(false);
                    GAME_MIN_SIZE = 250;
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
            if (epsilonModel.getRadius() < getGameFrame().getMainPanelWidth() / 2 ||
                    epsilonModel.getRadius() < getGameFrame().getMainPanelHeight() / 2) {
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
