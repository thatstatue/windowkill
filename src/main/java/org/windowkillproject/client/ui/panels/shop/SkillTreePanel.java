package org.windowkillproject.client.ui.panels.shop;

import org.windowkillproject.client.GameClient;
import org.windowkillproject.client.ui.panels.Panel;
import org.windowkillproject.client.view.ImgData;

import java.awt.*;
import java.util.ArrayList;

import static org.windowkillproject.Constants.APP_HEIGHT;
import static org.windowkillproject.Constants.APP_WIDTH;
import static org.windowkillproject.SpecialityName.*;

public class SkillTreePanel extends Panel {
    public SkillTreePanel(GameClient client) {
        super(client);
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
        components.add(buttonMaker("Menu", 790, 20, e -> client.getApp().initPFrame()));

        return components;
    }

    private void initMorphSkills() {
        proteus = new OptionPanel(Proteus, 1000, ImgData.getData().getProteus(), client);
        proteus.setBounds(660, 100, 270, 150);
        components.add(proteus);

        empusa = new OptionPanel(Empusa, 1000, ImgData.getData().getProteus(), client);
        empusa.setBounds(660, 260, 270, 150);
        components.add(empusa);

        dolus = new OptionPanel(Dolus, 1000, ImgData.getData().getProteus(), client);
        dolus.setBounds(660, 420, 270, 150);
        components.add(dolus);
    }

    private void initDefenceSkills() {
        aceso = new OptionPanel(Aceso, 500, ImgData.getData().getAceso(), client);
        aceso.setBounds(360, 100, 270, 150);
        components.add(aceso);

        melampus = new OptionPanel(Melampus, 750, ImgData.getData().getAceso(), client);
        melampus.setBounds(360, 260, 270, 150);
        components.add(melampus);

        chiron = new OptionPanel(Chiron, 900, ImgData.getData().getAceso(),client);
        chiron.setBounds(360, 420, 270, 150);
        components.add(chiron);
    }

    private void initAttackSkills() {
        ares = new OptionPanel(Ares, 750, ImgData.getData().getAres(),client);
        ares.setBounds(60, 100, 270, 150);
        components.add(ares);

        astrape = new OptionPanel(Astrape, 1000, ImgData.getData().getAres(), client);
        astrape.setBounds(60, 260, 270, 150);
        components.add(astrape);

        cerberus = new OptionPanel(Cerberus, 1500 , ImgData.getData().getAres(),client);
        cerberus.setBounds(60, 420, 270, 150);
        components.add(cerberus);
    }
}
