package org.windowkillproject.application.panels.game;


import org.windowkillproject.application.Application;

import javax.swing.*;
import java.awt.*;


public class InternalGamePanel extends GamePanel{
    public InternalGamePanel( int x, int y, int width, int height,PanelStatus panelStatus, boolean flexible) {
        super(panelStatus, flexible);
        setLocation(x,y);
        setPreferredSize(new Dimension(width, height));
        setSize(new Dimension(width, height));
        if (!flexible){
            setBorder(BorderFactory.createLineBorder(Color.white));
        }
        Application.getGameFrame().getLayeredPane().add(this, JLayeredPane.DEFAULT_LAYER);
    }
}
