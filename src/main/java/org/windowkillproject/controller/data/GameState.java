package org.windowkillproject.controller.data;
import org.windowkillproject.client.ui.panels.game.GamePanel;
import org.windowkillproject.server.model.ObjectModel;

import java.awt.*;
import java.io.Serial;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Map;

import static org.windowkillproject.Request.LOCK;


public class GameState implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final ArrayList<ObjectModel> objectModels;
    private final ArrayList<GamePanel> gamePanels;
    private final Map<GamePanel, Rectangle> gamePanelsBounds;
    private final long timestamp;
    private final int level;
    private final String checksum;
    private final int killedEnemiesInWave;
    private final int killedEnemiesTotal;

    public GameState(ArrayList<ObjectModel> objectModels, ArrayList<GamePanel> gamePanels,
                     Map<GamePanel, Rectangle> gamePanelsBounds, int level, int killedEnemiesInWave, int killedEnemiesTotal) {
        synchronized (LOCK) {
            this.objectModels = objectModels;
            this.gamePanels = gamePanels;
            this.gamePanelsBounds = gamePanelsBounds;
            this.level = level;
            this.killedEnemiesInWave = killedEnemiesInWave;
            this.killedEnemiesTotal = killedEnemiesTotal;
        }
        this.timestamp = System.currentTimeMillis();
        this.checksum = generateChecksum();
    }

    public ArrayList<ObjectModel> getObjectModels() {
        return objectModels;
    }

    public int getLevel() {
        return level;
    }

    public int getKilledEnemiesInWave() {
        return killedEnemiesInWave;
    }

    public int getKilledEnemiesTotal() {
        return killedEnemiesTotal;
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

    private String generateChecksum() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String data = objectModels.toString() + gamePanels.toString() +
                    gamePanelsBounds.toString()+ timestamp;
            byte[] hash = digest.digest(data.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isValidChecksum() {
//        return this.checksum.equals(generateChecksum());
        return true;
    }
}
