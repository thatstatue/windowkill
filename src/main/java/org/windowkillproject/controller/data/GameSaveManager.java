package org.windowkillproject.controller.data;

import java.io.*;

public class GameSaveManager {

    private static final String SAVE_FILE = "game_save.dat";

    public static void saveGameState(GameState gameState) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(gameState);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static GameState loadGameState() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
            GameState gameState = (GameState) ois.readObject();
            if (validateSaveFile(gameState)) {
                return gameState;
            } else {
                System.out.println("Save file validation failed.");
                return null;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No unfinished business :)");
            return null;
        }
    }

    private static boolean validateSaveFile(GameState gameState) {
        long currentTime = System.currentTimeMillis();
        // Assume save file is invalid if it's older than 10 minutes
        return gameState.isValidChecksum();
    }

    public static void deleteSaveFile() {
        File file = new File(SAVE_FILE);
        if (file.exists()) {
            file.delete();
        }
    }
}
