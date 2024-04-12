package org.windowkillproject.application.panels;


import org.windowkillproject.application.panels.Panel;

import java.awt.*;
import java.util.ArrayList;

import static org.windowkillproject.application.Config.APP_HEIGHT;
import static org.windowkillproject.application.Config.APP_WIDTH;

public class ShopPanel extends Panel {
    public ShopPanel(){
        super();
        setBackground(Color.decode("#3B2635"));
        setPreferredSize(new Dimension(APP_WIDTH, APP_HEIGHT));
    }

    @Override
    protected ArrayList<Component> initComponents() {
        return null;
    }
}