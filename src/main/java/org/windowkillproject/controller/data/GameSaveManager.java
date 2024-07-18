package org.windowkillproject.controller.data;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.windowkillproject.application.panels.game.GamePanel;
import org.windowkillproject.model.ObjectModel;

import java.awt.geom.Area;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameSaveManager {

    private static final String SAVE_FILE = "game_save.dat";
    private static Kryo kryo = new Kryo();

    static {
        // Register classes with Kryo
        kryo.register(GameState.class);
        kryo.register(ObjectModel.class);
        kryo.register(GamePanel.class);
        kryo.register(Area.class);
        kryo.register(ArrayList.class);
        kryo.register(HashMap.class);
        // Add other classes that need to be serialized
    }

    public static void saveGameState(GameState gameState) {
        try (Output output = new Output(new FileOutputStream(SAVE_FILE))) {
            kryo.writeObject(output, gameState);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static GameState loadGameState() {
        try (Input input = new Input(new FileInputStream(SAVE_FILE))) {
            GameState gameState = kryo.readObject(input, GameState.class);
            if (validateSaveFile(gameState)) {
                return gameState;
            } else {
                System.out.println("Save file validation failed.");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void deleteSaveFile() {
        File file = new File(SAVE_FILE);
        if (file.exists()) {
            file.delete();
        }
    }
    private static boolean validateSaveFile(GameState gameState) {
        long currentTime = System.currentTimeMillis();
        // Assume save file is invalid if it's older than 10 minutes
        boolean isRecent = currentTime - gameState.getTimestamp() < 10 * 60 * 1000;
        boolean isChecksumValid = gameState.isValidChecksum();
        return isRecent && isChecksumValid;
    }


}


