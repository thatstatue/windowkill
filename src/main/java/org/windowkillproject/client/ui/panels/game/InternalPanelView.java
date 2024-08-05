package org.windowkillproject.client.ui.panels.game;


import org.windowkillproject.client.GameClient;

import javax.swing.*;
import java.awt.*;


public class InternalPanelView extends PanelView {
    public InternalPanelView(GameClient client, String id, int x, int y, int width, int height) {
        super(id, client);
        setLocation(x,y);
        setPreferredSize(new Dimension(width, height));
        setSize(new Dimension(width, height));
        client.getApp().getGameFrame().getLayeredPane().add(this, JLayeredPane.DEFAULT_LAYER);
    }
}
