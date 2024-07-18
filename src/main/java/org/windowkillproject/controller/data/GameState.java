package org.windowkillproject.controller.data;
import org.windowkillproject.application.panels.game.GamePanel;
import org.windowkillproject.model.ObjectModel;

import java.awt.Rectangle;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Map;

public class GameState {
    private ArrayList<ObjectModel> objectModels;
    private ArrayList<GamePanel> gamePanels;
    private Map<GamePanel, Rectangle> gamePanelsBounds;
    private long timestamp;
    private String checksum;
    private String writSomeField;
    private int writAnotherField;

    public GameState(ArrayList<ObjectModel> objectModels, ArrayList<GamePanel> gamePanels,
                     Map<GamePanel, Rectangle> gamePanelsBounds) {
        this.objectModels = objectModels;
        this.gamePanels = gamePanels;
        this.gamePanelsBounds = gamePanelsBounds;
        this.timestamp = System.currentTimeMillis();
        this.checksum = generateChecksum();
    }

    public ArrayList<ObjectModel> getObjectModels() {
        return objectModels;
    }

    public ArrayList<GamePanel> getGamePanels() {
        return gamePanels;
    }

    public Map<GamePanel, Rectangle> getGamePanelsBounds() {
        return gamePanelsBounds;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getChecksum() {
        return checksum;
    }

    public String getWritSomeField() {
        return writSomeField;
    }

    public int getWritAnotherField() {
        return writAnotherField;
    }


    private String generateChecksum() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String data = objectModels.toString() + gamePanels.toString() +
                    gamePanelsBounds.toString() + timestamp;
            byte[] hash = digest.digest(data.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isValidChecksum() {
        return this.checksum.equals(generateChecksum());
    }
}
