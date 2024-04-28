package org.windowkillproject.application.panels;

import org.windowkillproject.model.entities.EpsilonModel;
import org.windowkillproject.view.ImgData;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static org.windowkillproject.application.Application.getGameFrame;
import static org.windowkillproject.application.Application.initPFrame;
import static org.windowkillproject.application.Config.APP_HEIGHT;
import static org.windowkillproject.application.Config.APP_WIDTH;
import static org.windowkillproject.application.panels.OptionPanel.SpecialtyName.*;

public class SkillTreePanel extends Panel{
    public SkillTreePanel(){
        super();
        setBackground(Color.decode("#3B2635"));
        setPreferredSize(new Dimension(APP_WIDTH, APP_HEIGHT));
    }
    public static OptionPanel ares;
    public static OptionPanel aceso;
    public static OptionPanel proteus;
    @Override
    protected ArrayList<Component> initComponents() {
        ArrayList<Component> components = new ArrayList<>();
        ares = new OptionPanel(Ares, 750, ImgData.getData().getAres());
        ares.setBounds(60, 100, 270, 400);
        components.add(ares);

        aceso = new OptionPanel(Aceso, 500, ImgData.getData().getAceso());
        aceso.setBounds(360, 100, 270, 400);
        components.add(aceso);

        proteus = new OptionPanel(Proteus, 1000, ImgData.getData().getProteus());

        proteus.setBounds(660, 100, 270, 400);
        components.add(proteus);

        JLabel xp = jLabelMaker("✦"+EpsilonModel.getINSTANCE().getXp(),250, 20, 200, 50 );
        xp.setForeground(Color.green);
        components.add(xp);

        components.add(jLabelMaker("SKILL TREE", 50, 20, 200, 50));
        components.add(buttonMaker("Menu", 790, 20, e -> initPFrame()));

        return components;
    }
}
