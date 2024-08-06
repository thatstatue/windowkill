package org.windowkillproject.server.model.panelmodels;

import org.windowkillproject.server.model.Drawable;
import org.windowkillproject.server.model.entities.EpsilonModel;
import org.windowkillproject.server.model.globe.GlobeModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.windowkillproject.Constants.*;
import static org.windowkillproject.Constants.FRAME_SHRINKAGE_SPEED;
import static org.windowkillproject.Request.REQ_START_GAME_1;


public abstract class PanelModel implements Drawable {
    private final GlobeModel globeModel;
    private boolean flexible;
    protected int x, y, width, height;
    private final PanelStatus panelStatus;

    public void setFlexible(boolean flexible) {
        this.flexible = flexible;
    }

    public PanelStatus getPanelStatus() {
        return panelStatus;
    }

    private boolean isStretching = false;
    private boolean exploding = false;
    private int GAME_MIN_SIZE = 250;

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

    public boolean isBackground() {
        return background;
    }

    public void setBackground(boolean background) {
        this.background = background;
    }
    private final String id = UUID.randomUUID().toString();

    private boolean background;

    public String getId() {
        return id;
    }

    public PanelModel(GlobeModel globeModel, Rectangle bounds, PanelStatus panelStatus, boolean flexible, boolean background) {
        this.globeModel = globeModel;
        this.flexible = flexible;
        this.panelStatus = panelStatus;
        setBounds(bounds);
        this.background = background;
        this.globeModel.getPanelModels().add(this);
        globeModel.getGlobeController().createPanelView(id,x,y,width,height);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void setBounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void setBounds(Rectangle bounds) {
        setBounds(bounds.x, bounds.y,
                bounds.width, bounds.height);
    }

    public void resetCanShrink() {
        boolean can = !panelStatus.equals(PanelStatus.isometric);
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
        if (!(this instanceof MainPanelModel && ((MainPanelModel) this).isPunched())) {

            if (flexible) {
                if (canShrinkLeft) newX += FRAME_SHRINKAGE_SPEED / 2;
                if (canShrinkUp) newY += FRAME_SHRINKAGE_SPEED / 2;
                if (canShrinkRight) newWidth -= FRAME_SHRINKAGE_SPEED;
                if (canShrinkDown) newHeight -= FRAME_SHRINKAGE_SPEED;
            }

            if (panelStatus.equals(PanelStatus.shrinkable)
//                && targetEpsilon.getLocalPanel() != null
//                && this.equals(targetEpsilon.getLocalPanel())
//                todo: if min size is only for epsilon's panel will uncomment
            ) {
                int minSize = GAME_MIN_SIZE; //>>>>>>>>>>>>>>>?
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
                    if (panelStatus.equals(PanelStatus.isometric)) {
                        newWidth = getWidth();
                        newHeight = getHeight();
                    }
                    if (!isStoppedByRigidPanels(code, newX, newY, newWidth, newHeight)) {
                        setBounds(newX, newY, newWidth, newHeight);
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
        var gamePanels = globeModel.getPanelModels();
        for (int i = 0; i < gamePanels.size(); i++) {
            var panel = gamePanels.get(i);
            if (panel.isFlexible()) {
                continue;
            }
            double BX = panel.getX();
            double BY = panel.getY();
            double BW = panel.getWidth();
            double BH = panel.getHeight();

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
            for(EpsilonModel epsilonModel: globeModel.getEpsilons())
                globeModel.getEntityModels().remove(epsilonModel);
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
            } else {
                if (exploding) {
                    globeModel.getPanelModels().remove(this);
                    GAME_MIN_SIZE = 250;
                    globeModel.performAction(REQ_START_GAME_1);
                }
                shrinkFastTimer.stop();
            }
        });
        shrinkFastTimer.start();

    }

    public void endingScene() {
        Timer endingTimer = new Timer(10, null);

        ActionListener actionListener = e -> {
            boolean temp = false;
            for (EpsilonModel epsilonModel : globeModel.getEpsilons()) {
                var mainPanel = globeModel.getMainPanelModel();
                if (epsilonModel.getRadius() < mainPanel.getWidth() / 2 ||
                        epsilonModel.getRadius() < mainPanel.getHeight() / 2) {
                    epsilonModel.setRadius(epsilonModel.getRadius() + 6);
                    temp = true;
                }
            }

            if (!temp) {
                setExploding(true);
                shrinkFast();
                endingTimer.stop();
            }

        };
        endingTimer.addActionListener(actionListener);
        endingTimer.start();

    }
}
