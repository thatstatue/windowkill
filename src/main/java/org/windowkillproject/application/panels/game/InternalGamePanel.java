package org.windowkillproject.application.panels.game;


import org.windowkillproject.application.Application;

import javax.swing.*;
import java.awt.*;


public class InternalGamePanel extends GamePanel{
    public InternalGamePanel( int x, int y, int width, int height,PanelStatus panelStatus) {
        super(panelStatus);
        setLocation(x,y);
        setPreferredSize(new Dimension(width, height));
        setSize(new Dimension(width, height));

        Application.getGameFrame().getLayeredPane().add(this, JLayeredPane.DEFAULT_LAYER);
    }
}
