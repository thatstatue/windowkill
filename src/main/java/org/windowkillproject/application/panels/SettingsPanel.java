package org.windowkillproject.application.panels;

import org.windowkillproject.application.Config;
import org.windowkillproject.application.DifficultySetter;
import org.windowkillproject.application.SoundPlayer;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;

import static org.windowkillproject.application.Application.initPFrame;

public class SettingsPanel extends Panel {
    public SettingsPanel(){
        super();
        setBackground(Color.decode("#d3ab97"));
        setPreferredSize(new Dimension(Config.APP_WIDTH, Config.APP_HEIGHT));
    }
    @Override
    protected ArrayList<Component> initComponents() {
        ArrayList<Component> componentArrayList = new ArrayList<>();
        {
            ChangeListener changeListener = e -> {
                JSlider source = (JSlider) e.getSource();
//                switch (source.getValue()){
//                    case 0 -> Config.SENSITIVITY_RATE = 10;
//                    case 1 -> Config.SENSITIVITY_RATE = 5;
//                    case 2 -> Config.SENSITIVITY_RATE = 0;
//
//                }
            };
            JSlider slider = sliderMaker(changeListener, 200, 250);
            JLabel label = jLabelMaker("Sensitivity", 50, 240, 100, 50);
            componentArrayList.add(label);
            componentArrayList.add(slider);
        }
        {
            ChangeListener changeListener = e -> {
                JSlider source = (JSlider) e.getSource();
                switch (source.getValue()){
                    case 0 -> DifficultySetter.setDifficulty("LOW");
                    case 1 -> DifficultySetter.setDifficulty("MEDIUM");
                    case 2 -> DifficultySetter.setDifficulty("HIGH");
                }
            };
            JSlider slider = sliderMaker(changeListener, 200, 350);
            JLabel label = jLabelMaker("Difficulty", 50, 340, 100, 50);
            componentArrayList.add(label);
            componentArrayList.add(slider);
        }
        {
            ChangeListener changeListener = e -> {
                JSlider source = (JSlider) e.getSource();
                switch (source.getValue()){
                    case 0 -> SoundPlayer.setSound("LOW");
                    case 1 -> SoundPlayer.setSound("MEDIUM");
                    case 2 -> SoundPlayer.setSound("HIGH");
                }
            };
            JSlider slider = sliderMaker(changeListener, 200, 450);
            JLabel label = jLabelMaker("Volume", 50, 440, 100, 50);
            componentArrayList.add(label);
            componentArrayList.add(slider);
        }

        JLabel name = jLabelMaker("SETTINGS", 50, 20, 200, 50);
        name.setFont(new Font(Font.SANS_SERIF,Font.BOLD,35));
        name.setForeground(Config.BUTTON_BG_COLOR);
        componentArrayList.add(name);

        componentArrayList.add(buttonMaker("Menu", 790, 20, e -> initPFrame()));

        return componentArrayList;
    }

}
