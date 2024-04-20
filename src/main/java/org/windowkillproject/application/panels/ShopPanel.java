package org.windowkillproject.application.panels;


import org.windowkillproject.application.panels.Panel;
import org.windowkillproject.view.ImgData;

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
        ArrayList<Component> components = new ArrayList<>();
        OptionPanel banish = new OptionPanel("O' Hephaestus، Banish", 100, ImgData.getData().getBanish());
        banish.setBounds(100, 100, 300, 400);
        components.add(banish);
        System.out.println("banish added");
        return components;
    }
}