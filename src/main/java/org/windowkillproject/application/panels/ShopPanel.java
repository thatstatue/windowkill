package org.windowkillproject.application.panels;


import org.windowkillproject.view.ImgData;

import java.awt.*;
import java.util.ArrayList;

import static org.windowkillproject.application.Config.APP_HEIGHT;
import static org.windowkillproject.application.Config.APP_WIDTH;
import static org.windowkillproject.application.panels.OptionPanel.SpecialtyName.*;

public class ShopPanel extends Panel {

    public ShopPanel() {
        super();
        setBackground(Color.decode("#3B2635"));
        setPreferredSize(new Dimension(APP_WIDTH, APP_HEIGHT));
    }

    public static OptionPanel banish;
    public static OptionPanel empower;
    public static OptionPanel heal;

    @Override
    protected ArrayList<Component> initComponents() {
        ArrayList<Component> components = new ArrayList<>();
        banish = new OptionPanel(Banish, 100, ImgData.getData().getBanish());
        banish.setBounds(60, 100, 270, 400);
        components.add(banish);

        empower = new OptionPanel(Empower, 75, ImgData.getData().getEmpower());
        empower.setBounds(360, 100, 270, 400);
        components.add(empower);

        heal = new OptionPanel(Heal, 50, ImgData.getData().getHeal());
        heal.setBounds(660, 100, 270, 400);
        components.add(heal);

        return components;
    }
}