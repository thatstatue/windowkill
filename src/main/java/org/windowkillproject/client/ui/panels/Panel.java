package org.windowkillproject.client.ui.panels;

import org.windowkillproject.client.GameClient;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;

import static org.windowkillproject.Constants.*;

public abstract class Panel extends JPanel {
    protected ArrayList<Component> components;
    protected final GameClient client;

    public GameClient getClient() {
        return client;
    }

    public Panel(GameClient client) {
        super();
        this.client =client;
        requestFocus();
        setLayout(null);
        components = initComponents();
        addComponentsToPanel();
    }

    protected abstract ArrayList<Component> initComponents();

    public void setComponents(ArrayList<Component> components) {
        this.components = components;
    }

    protected final JButton buttonMaker(String name, int x, int y, ActionListener actionListener) {
        JButton jButton = new JButton(name);
        jButton.setBounds(x, y, BUTTON_WIDTH, BUTTON_HEIGHT);
        jButton.setFont(BUTTON_FONT);
        jButton.setFocusPainted(false);
        jButton.setBackground(BUTTON_BG_COLOR);
        jButton.setForeground(BUTTON_FG_COLOR);
        jButton.addActionListener(actionListener);
        return jButton;
    }

    protected final JLabel jLabelMaker(String name, int x, int y, int width, int height) {
        JLabel jLabel = new JLabel(name);
        jLabel.setBounds(x, y, width, height);
        jLabel.setFont(TEXT_FONT);
        jLabel.setForeground(BUTTON_FG_COLOR);
        return jLabel;
    }

    protected final JSlider sliderMaker(ChangeListener changeListener, int x, int y) {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 2, 1);
        slider.setMajorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setOpaque(false);
        slider.setPaintLabels(true);
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        labelTable.put(0, new JLabel("LOW"));
        labelTable.put(1, new JLabel("NORMAL"));
        labelTable.put(2, new JLabel("HIGH"));
        slider.setLabelTable(labelTable);
        slider.addChangeListener(changeListener);
        slider.setBounds(x, y, 300, 50);
        return slider;
    }

    protected void addComponentsToPanel() {
        if (components != null) {
            for (Component component : components) {
                this.add(component);
            }
        }
    }
}
