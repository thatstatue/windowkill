package org.windowkillproject.application.panels.shop;

import org.windowkillproject.application.panels.Panel;
import org.windowkillproject.view.ImgData;

import java.awt.*;
import java.util.ArrayList;

import static org.windowkillproject.application.Application.initPFrame;
import static org.windowkillproject.application.Config.APP_HEIGHT;
import static org.windowkillproject.application.Config.APP_WIDTH;
import static org.windowkillproject.application.panels.shop.OptionPanel.SpecialtyName.*;

public class SkillTreePanel extends Panel {
    public SkillTreePanel() {
        super();
        setBackground(Color.decode("#3B2635"));
        setPreferredSize(new Dimension(APP_WIDTH, APP_HEIGHT));
    }

    public static OptionPanel ares;
    public static OptionPanel aceso;
    public static OptionPanel proteus;
    public static OptionPanel astrape;
    public static OptionPanel cerberus;
    public static OptionPanel melampus;
    public static OptionPanel chiron;
    public static OptionPanel empusa;
    public static OptionPanel dolus;
    public static OptionPanel[] skills;


    @Override
    protected ArrayList<Component> initComponents() {
       // ArrayList<Component>
        components = new ArrayList<>();
        initAttackSkills();

        initDefenceSkills();

        initMorphSkills();

         skills = new OptionPanel[]{
                ares, astrape, cerberus,
                aceso , melampus, chiron,
                proteus,empusa,dolus
         };

        components.add(jLabelMaker("SKILL TREE", 50, 20, 200, 50));
        components.add(buttonMaker("Menu", 790, 20, e -> initPFrame()));

        return components;
    }

    private void initMorphSkills() {
        proteus = new OptionPanel(Proteus, 1000, ImgData.getData().getProteus());
        proteus.setBounds(660, 100, 270, 150);
        components.add(proteus);

        empusa = new OptionPanel(Empusa, 1000, ImgData.getData().getProteus());
        empusa.setBounds(660, 260, 270, 150);
        components.add(empusa);

        dolus = new OptionPanel(Dolus, 1000, ImgData.getData().getProteus());
        dolus.setBounds(660, 420, 270, 150);
        components.add(dolus);
    }

    private void initDefenceSkills() {
        aceso = new OptionPanel(Aceso, 500, ImgData.getData().getAceso());
        aceso.setBounds(360, 100, 270, 150);
        components.add(aceso);

        melampus = new OptionPanel(Melampus, 750, ImgData.getData().getAceso());
        melampus.setBounds(360, 260, 270, 150);
        components.add(melampus);

        chiron = new OptionPanel(Chiron, 900, ImgData.getData().getAceso());
        chiron.setBounds(360, 420, 270, 150);
        components.add(chiron);
    }

    private void initAttackSkills() {
        ares = new OptionPanel(Ares, 750, ImgData.getData().getAres());
        ares.setBounds(60, 100, 270, 150);
        components.add(ares);

        astrape = new OptionPanel(Astrape, 1000, ImgData.getData().getAres());//todo
        astrape.setBounds(60, 260, 270, 150);
        components.add(astrape);

        cerberus = new OptionPanel(Cerberus, 1500 , ImgData.getData().getAres());
        cerberus.setBounds(60, 420, 270, 150);
        components.add(cerberus);
    }
}
