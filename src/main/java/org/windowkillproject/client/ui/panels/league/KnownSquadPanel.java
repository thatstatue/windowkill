package org.windowkillproject.client.ui.panels.league;

import org.windowkillproject.client.GameClient;
import org.windowkillproject.client.ui.dummy.DummySquad;
import org.windowkillproject.client.ui.listeners.EpsilonKeyListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static org.windowkillproject.Request.*;
import static org.windowkillproject.client.GameClient.usernameMap;

public class KnownSquadPanel extends SquadPanel {
    private boolean isOpponent;
    public KnownSquadPanel(GameClient client, DummySquad dummySquad, boolean isOpponent) {
        super(client, dummySquad);
        this.isOpponent = isOpponent;
        ActionListener details = e -> {
            detailsPartial = showPartial();
        };
        stateChangeButton = buttonMaker("DETAILS", 100, 250, details);
        components.add(stateChangeButton);

    }
    private JDialog detailsPartial;
    public JDialog showPartial() {
        JDialog dialog = new JDialog(client.getApp().getLeagueFrame());
        dialog.getContentPane().setBackground(Color.decode("#7C4F63"));
        dialog.setLocationRelativeTo(null);
        dialog.setFocusable(true);
        dialog.setSize(500, 800);
        int size= dummySquad.getPlayers().size();
        for (int i = 0 ; i < size; i++){
            var player = dummySquad.getPlayers().get(i);
            JLabel message = new JLabel(player.getDetails());
            message.setBounds(20, i*110 + 50, 80, 400);
            message.setForeground(Color.white);
            message.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
            dialog.add(message);
            if(isOpponent){
                ActionListener actionListener = e ->{
                    client.sendMessage(LEAGUE_REDIRECT+
                            REGEX_SPLIT+ REQ_NEW_GAME_MONOMACHIA +
                            REGEX_SPLIT+ client.getUsername()
                            + REGEX_SPLIT+ player.getName());
                };
                JButton button = buttonMaker("MONOMACHIA",
                        350 , i*(110)+20, actionListener);
                dialog.add(button);
                ActionListener actionListener1 = e ->{
                    client.sendMessage(LEAGUE_REDIRECT+
                            REGEX_SPLIT+ REQ_NEW_GAME_COLOSSEUM +
                            REGEX_SPLIT+ client.getUsername()
                            + REGEX_SPLIT+ player.getName());
                };
                JButton button1 = buttonMaker("COLOSSEUM",
                        350 , i*(500/size)+90, actionListener1);
                dialog.add(button1);
            }
        }
        if (!isOpponent){
            ActionListener leave = e -> {
                client.sendMessage(LEAGUE_REDIRECT + REGEX_SPLIT + REQ_LEAVE_SQUAD + REGEX_SPLIT +
                        dummySquad.getName() + REGEX_SPLIT + client.getUsername());
            };
            JButton button = buttonMaker("LEAVE",
                    50 , 720, leave);
            dialog.add(button);
//            ActionListener add = e -> {
//                client.sendMessage(LEAGUE_REDIRECT + REGEX_SPLIT + REQ_ADD_TO_VAULT + REGEX_SPLIT +
//                        dummySquad.getName() + REGEX_SPLIT + client.getUsername() + REGEX_SPLIT +);
//            };
//            JButton button = buttonMaker("LEAVE",
//                    50 , 720, leave); todo pay
        }
        return dialog;
    }
}
