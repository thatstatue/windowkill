package org.windowkillproject.application.panels.game;


import org.windowkillproject.application.Application;

import javax.swing.*;
import java.awt.*;


public class InternalGamePanel extends GamePanel{
    public InternalGamePanel(PanelStatus panelStatus, int width, int height, int x, int y) {
        super(panelStatus);
        setPreferredSize(new Dimension(width, height));
        setSize(new Dimension(width, height));

        setLocation(x,y);
        Application.getGameFrame().getLayeredPane().add(this, JLayeredPane.DEFAULT_LAYER);
    }
}
