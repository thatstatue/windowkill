package org.windowkillproject.client.ui.frames;

import org.windowkillproject.client.GameClient;
import org.windowkillproject.client.ui.panels.etc.LeaguePanel;

import javax.swing.*;
import java.awt.*;

import static org.windowkillproject.Constants.*;
import static org.windowkillproject.Request.*;
import static org.windowkillproject.Request.REGEX_SPLIT;

public class LeagueFrame extends JFrame {
    public LeagueFrame(GameClient client) {
        super();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(new Dimension(APP_WIDTH, APP_HEIGHT));
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(false);
        setTitle(APP_TITLE);
        this.setLayout(null);
        setContentPane(new LeaguePanel(client));
    }
    private boolean usernamed = false;

    public void  setUsernamed(boolean usernamed){
        this.usernamed = usernamed;
    }
    public void getUsername(GameClient client) {
        String username = null;
        if (client.getUsername() == null) {
            while (!usernamed) {
                username = JOptionPane.showInputDialog(null, "Enter a username:");
                if (username == null || username.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Username cannot be empty.");
                } else {
                    client.sendMessage(LEAGUE_REDIRECT + REGEX_SPLIT + REQ_NEW_ONLINE_PLAYER + REGEX_SPLIT + username);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            client.setUsername(username);
        }
    }

    public boolean isUsernamed() {
        return usernamed;
    }
}
