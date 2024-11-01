package org.windowkillproject.client.ui.panels.etc;

import org.windowkillproject.client.GameClient;
import org.windowkillproject.client.ui.panels.Panel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.windowkillproject.Constants.*;

public class ScorePanel extends Panel {
    private final JLabel clock = new JLabel("Elapsed Time  ->     ");
    private final JLabel xp = new JLabel(   "Total XP      ->     ");
    private final JLabel enemy = new JLabel("Enemies Killed->     ");
    private final JLabel wave = new JLabel( "Last Wave     ->     ");
    private final JLabel score = new JLabel("   << GAME OVER! >>  ");

    private JLabel[] labels = new JLabel[]{
            score,clock,xp,enemy,wave,new JLabel()
    };

    public ScorePanel(GameClient client, JLabel[] jLabels, boolean won ) {
        super(client);
        setBackground(Color.black);
        setPreferredSize(new Dimension((int) (GAME_WIDTH*0.7), (int) (GAME_HEIGHT*0.7)));
        setLabels(jLabels);
        if (won) score.setText("    << WINNER! >>   ");
        labels = new JLabel[]{score,clock,xp,enemy,wave, new JLabel()};
        ArrayList<Component> components =new ArrayList<>(List.of(labels));
        components.add(buttonMaker("Menu", 150, 320, e -> client.getApp().initPFrame()));
        setComponents(components);
        addComponentsToPanel();

    }
    private void setLabels(JLabel[] labels){
        for (int i = 0 ; i< labels.length ; i++){
            JLabel jLabel = this.labels[i];
            jLabel.setText(jLabel.getText()+labels[i].getText());
            jLabel.setForeground(Color.white);
            jLabel.setFont(TEXT_FONT);
            jLabel.setBounds(130, 70+i*50, 500, 30);
        }

    }

    @Override
    protected ArrayList<Component> initComponents() {
        if (labels == null) return null;
        return new ArrayList<>(Arrays.asList(labels));
    }
}
