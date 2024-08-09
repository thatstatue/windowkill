package org.windowkillproject.client.ui.panels.league;


import org.windowkillproject.client.GameClient;
import org.windowkillproject.client.ui.dummy.DummySquad;
import org.windowkillproject.client.ui.panels.Panel;
import org.windowkillproject.client.ui.panels.shop.SelectButton;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static org.windowkillproject.Constants.OPTION_HEIGHT;
import static org.windowkillproject.Constants.OPTION_WIDTH;

public abstract class SquadPanel extends Panel {
    protected DummySquad dummySquad;

    public DummySquad getDummySquad() {
        return dummySquad;
    }

    public void setDummySquad(DummySquad dummySquad) {
        this.dummySquad = dummySquad;
    }
    private JLabel name, playersCount;

    public SquadPanel(GameClient client, DummySquad dummySquad) {
        super(client);
        this.dummySquad = dummySquad;
        setBackground(Color.decode("#d3ab97"));
        setPreferredSize(new Dimension(350, 350));
        setFocusable(true);
        if (dummySquad!=null) {
            name.setText(dummySquad.getName());
            playersCount.setText("players count : " + dummySquad.getPlayersCount());
        }
    }
    protected JButton stateChangeButton;
    @Override
    protected ArrayList<Component> initComponents() {
        ArrayList<Component> components = new ArrayList<>();
        name = new JLabel();
        name.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        name.setForeground(Color.white);
        name.setBounds(30, 30, OPTION_WIDTH, 20);
        components.add(name);

        playersCount = new JLabel();
        playersCount.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        playersCount.setForeground(Color.black);
        playersCount.setBounds(160, 50, OPTION_WIDTH, 20);
        components.add(playersCount);
        return components;
    }
}
