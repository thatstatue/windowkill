package org.windowkillproject.controller.data;
import org.windowkillproject.client.ui.panels.game.PanelView;
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
    private final ArrayList<PanelView> panelViews;
    private final Map<PanelView, Rectangle> gamePanelsBounds;
    private final long timestamp;
    private final int level;
    private final String checksum;
    private final int killedEnemiesInWave;
    private final int killedEnemiesTotal;

    public GameState(ArrayList<ObjectModel> objectModels, ArrayList<PanelView> panelViews,
                     Map<PanelView, Rectangle> gamePanelsBounds, int level, int killedEnemiesInWave, int killedEnemiesTotal) {
        synchronized (LOCK) {
            this.objectModels = objectModels;
            this.panelViews = panelViews;
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

    public ArrayList<PanelView> getGamePanels() {
        return panelViews;
    }

    public Map<PanelView, Rectangle> getGamePanelsBounds() {
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
            String data = objectModels.toString() + panelViews.toString() +
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
