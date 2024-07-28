package org.windowkillproject.client.ui.panels.game;


import org.windowkillproject.client.GameClient;
import org.windowkillproject.client.ui.App;

import javax.swing.*;
import java.awt.*;


public class InternalGamePanel extends GamePanel{
    public InternalGamePanel( int x, int y, int width, int height,PanelStatus panelStatus, boolean flexible, GameClient client) {
        super(panelStatus, flexible, client);
        setLocation(x,y);
        setPreferredSize(new Dimension(width, height));
        setSize(new Dimension(width, height));
        if (!flexible){
            setBorder(BorderFactory.createLineBorder(Color.white));
        }
        client.getApp().getGameFrame().getLayeredPane().add(this, JLayeredPane.DEFAULT_LAYER);
    }
}
