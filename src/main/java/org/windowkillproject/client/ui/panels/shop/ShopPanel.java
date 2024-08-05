package org.windowkillproject.client.ui.panels.shop;


import org.windowkillproject.SpecialityName;
import org.windowkillproject.client.GameClient;
import org.windowkillproject.client.ui.panels.Panel;
import org.windowkillproject.client.view.ImgData;

import java.awt.*;
import java.util.ArrayList;

import static org.windowkillproject.Constants.APP_HEIGHT;
import static org.windowkillproject.Constants.APP_WIDTH;

public class ShopPanel extends Panel {

    public ShopPanel(GameClient client) {
        super(client);
        setBackground(Color.decode("#3B2635"));
        setPreferredSize(new Dimension(APP_WIDTH, APP_HEIGHT));
    }

    public static OptionPanel banish;
    public static OptionPanel empower;
    public static OptionPanel heal;
    public static OptionPanel dismay;
    public static OptionPanel slumber;
    public static OptionPanel slaughter;
    public void setWritChosen(boolean specialityName){
      //  banish.setWritChosen();
    }



    @Override
    protected ArrayList<Component> initComponents() {
        ArrayList<Component> components = new ArrayList<>();
        banish = new OptionPanel(SpecialityName.Banish, 100, ImgData.getData().getBanish(),client);
        banish.setBounds(60, 100, 270, 200);
        components.add(banish);

        empower = new OptionPanel(SpecialityName.Empower, 75, ImgData.getData().getEmpower(), client);
        empower.setBounds(360, 100, 270, 200);
        components.add(empower);

        heal = new OptionPanel(SpecialityName.Heal, 50, ImgData.getData().getHeal(),client);
        heal.setBounds(660, 100, 270, 200);
        components.add(heal);

        dismay = new OptionPanel(SpecialityName.Dismay, 120, ImgData.getData().getHeal(), client);
        dismay.setBounds(60, 320, 270, 200);
        components.add(dismay);

        slumber = new OptionPanel(SpecialityName.Slumber, 150, ImgData.getData().getHeal(), client);
        slumber.setBounds(360, 320, 270, 200);
        components.add(slumber);

        slaughter = new OptionPanel(SpecialityName.Slaughter, 200, ImgData.getData().getHeal(), client);
        slaughter.setBounds(660, 320, 270, 200);
        components.add(slaughter);


        return components;
    }
}