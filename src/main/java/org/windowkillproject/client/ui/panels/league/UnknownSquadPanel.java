package org.windowkillproject.client.ui.panels.league;

import org.windowkillproject.client.GameClient;
import org.windowkillproject.client.ui.dummy.DummySquad;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static org.windowkillproject.Request.*;
import static org.windowkillproject.Request.REGEX_SPLIT;

public class UnknownSquadPanel extends SquadPanel {

    public UnknownSquadPanel(GameClient client, DummySquad dummySquad) {
        super(client, dummySquad);
        ActionListener join = e -> {
            client.sendMessage(LEAGUE_REDIRECT + REGEX_SPLIT + REQ_JOIN_SQUAD + REGEX_SPLIT +
                    dummySquad.getName() + REGEX_SPLIT + client.getUsername());
        };
        stateChangeButton = buttonMaker("JOIN", 100, 250, join);
        components.add(stateChangeButton);
    }

    @Override
    protected ArrayList<Component> initComponents() {
        return null;
    }
}
