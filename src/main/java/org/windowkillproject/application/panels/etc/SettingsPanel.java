package org.windowkillproject.application.panels.etc;

import org.windowkillproject.application.Application;
import org.windowkillproject.application.Config;
import org.windowkillproject.application.Setter;
import org.windowkillproject.application.SoundPlayer;
import org.windowkillproject.application.listeners.EpsilonKeyListener;
import org.windowkillproject.application.panels.Panel;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;

import static org.windowkillproject.application.Application.initPFrame;
import static org.windowkillproject.application.Config.*;
import static org.windowkillproject.application.listeners.EpsilonKeyListener.*;

public class SettingsPanel extends Panel {
    public SettingsPanel() {
        super();
        setBackground(Color.decode("#d3ab97"));
        setPreferredSize(new Dimension(Config.APP_WIDTH, Config.APP_HEIGHT));
    }
    public static JDialog monoDialog = new JDialog();

    @Override
    protected ArrayList<Component> initComponents() {
        ArrayList<Component> componentArrayList = new ArrayList<>();
        {
            ChangeListener changeListener = e -> {
                JSlider source = (JSlider) e.getSource();
                switch (source.getValue()) {
                    case 0 -> Config.SENSITIVITY_RATE = 100;
                    case 1 -> Config.SENSITIVITY_RATE = 50;
                    case 2 -> Config.SENSITIVITY_RATE = 0;

                }
            };
            JSlider slider = sliderMaker(changeListener, 200, 250);
            JLabel label = jLabelMaker("Sensitivity", 50, 240, 100, 50);
            componentArrayList.add(label);
            componentArrayList.add(slider);
        }
        {
            ChangeListener changeListener = e -> {
                JSlider source = (JSlider) e.getSource();
                switch (source.getValue()) {
                    case 0 -> Setter.setDifficulty("LOW");
                    case 1 -> Setter.setDifficulty("MEDIUM");
                    case 2 -> Setter.setDifficulty("HIGH");
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
                switch (source.getValue()) {
                    case 0 -> SoundPlayer.setSoundVolume("LOW");
                    case 1 -> SoundPlayer.setSoundVolume("MEDIUM");
                    case 2 -> SoundPlayer.setSoundVolume("HIGH");
                }
            };
            JSlider slider = sliderMaker(changeListener, 200, 450);
            JLabel label = jLabelMaker("Volume", 50, 440, 100, 50);
            componentArrayList.add(label);
            componentArrayList.add(slider);
        }

        JLabel name = jLabelMaker("SETTINGS", 50, 20, 200, 50);
        name.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 35));
        name.setForeground(Config.BUTTON_BG_COLOR);
        componentArrayList.add(name);

        componentArrayList.add(buttonMaker("Menu", 790, 20, e -> initPFrame()));

        componentArrayList.add(buttonMaker("Menu", 790, 20, e -> initPFrame()));

        componentArrayList.add(buttonMaker("UP KEY", 600, 150, e -> {
            Setter.key = UP_CODE;
            monoDialog.dispose();
            monoDialog = showKeyPopup("UP KEY", UP_KEY_NAME);
            monoDialog.setVisible(true);

        }));
        componentArrayList.add(buttonMaker("DOWN KEY", 600, 450, e -> {
            Setter.key = DOWN_CODE;
            monoDialog.dispose();
            monoDialog =  showKeyPopup("DOWN KEY", DOWN_KEY_NAME);
            monoDialog.setVisible(true);
        }));
        componentArrayList.add(buttonMaker("LEFT KEY", 600, 250, e -> {
            Setter.key = LEFT_CODE;
            monoDialog.dispose();
            monoDialog =  showKeyPopup("LEFT KEY", LEFT_KEY_NAME);
            monoDialog.setVisible(true);
        }));
        componentArrayList.add(buttonMaker("RIGHT KEY", 600, 350, e -> {
            Setter.key = RIGHT_CODE;
            monoDialog.dispose();
            monoDialog = showKeyPopup("RIGHT KEY", RIGHT_KEY_NAME);
            monoDialog.setVisible(true);
        }));


        return componentArrayList;
    }

    public JDialog showKeyPopup(String keyName, String keyNameB4) {
        JDialog dialog = new JDialog(Application.getSettingsFrame());
        dialog.getContentPane().setBackground(Color.decode("#7C4F63"));
        dialog.setLocationRelativeTo(null);
        dialog.setFocusable(true);
        dialog.setSize(350, 220);
        JLabel message = new JLabel("your " + keyName + " is currently set to " + keyNameB4);
        JLabel label = new JLabel("    press your new "+ keyName );
        message.setBounds(40, 10, 350, 90);
        message.setForeground(Color.white);
        label.setBounds(30, 100, 360, 100);
        label.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        label.setForeground(Color.white);
        dialog.add(message);
        dialog.add(label);
        EpsilonKeyListener.changingButtons = true;
        return dialog;
    }

}
