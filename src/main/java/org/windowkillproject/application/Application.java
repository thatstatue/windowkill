package org.windowkillproject.application;

import org.windowkillproject.application.frames.*;
import org.windowkillproject.application.listeners.EpsilonKeyListener;
import org.windowkillproject.application.listeners.ShotgunMouseListener;

import org.windowkillproject.application.panels.etc.SettingsPanel;
import org.windowkillproject.application.panels.shop.ShopPanel;
import org.windowkillproject.application.panels.shop.SkillTreePanel;
import org.windowkillproject.application.panels.etc.TutorialPanel;
import org.windowkillproject.controller.ElapsedTime;
import org.windowkillproject.controller.Update;
import org.windowkillproject.controller.data.GameSaveManager;
import org.windowkillproject.controller.data.GameState;
import org.windowkillproject.model.Wave;
import org.windowkillproject.model.Writ;
import org.windowkillproject.model.abilities.BulletModel;
import org.windowkillproject.model.abilities.ProjectileModel;
import org.windowkillproject.model.entities.EpsilonModel;
import org.windowkillproject.model.entities.enemies.minibosses.BlackOrbModel;
import org.windowkillproject.view.abilities.AbilityView;
import org.windowkillproject.view.entities.EntityView;
import org.windowkillproject.view.entities.enemies.minibosses.BlackOrbView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;

import static org.windowkillproject.application.Config.EPSILON_HP;
import static org.windowkillproject.application.Config.LOCK;
import static org.windowkillproject.application.panels.game.GamePanel.gamePanels;
import static org.windowkillproject.application.panels.game.GamePanel.gamePanelsBounds;
import static org.windowkillproject.controller.Controller.createObjectView;
import static org.windowkillproject.controller.Update.*;
import static org.windowkillproject.model.ObjectModel.objectModels;
import static org.windowkillproject.model.Wave.waveTimer;
import static org.windowkillproject.model.abilities.AbilityModel.abilityModels;
import static org.windowkillproject.model.abilities.CollectableModel.collectableModels;
import static org.windowkillproject.model.entities.EntityModel.entityModels;
import static org.windowkillproject.model.entities.enemies.EnemyModel.*;
import static org.windowkillproject.model.entities.enemies.EnemyModel.getKilledEnemiesTotal;
import static org.windowkillproject.model.entities.enemies.normals.ArchmireModel.archmireModels;
import static org.windowkillproject.model.entities.enemies.normals.OmenoctModel.omenoctModels;
import static org.windowkillproject.model.entities.enemies.minibosses.BarricadosModel.barricadosModels;

public class Application implements Runnable {
    private static PrimaryFrame primaryFrame;
    private static final ShotgunMouseListener shotgunMouseListener = new ShotgunMouseListener();
    private static final EpsilonKeyListener epsilonKeyListener = new EpsilonKeyListener();
    private static GameFrame gameFrame;
    private static SideFrame shopFrame;
    public static ScoreFrame scoreFrame;
    private static SideFrame skillTreeFrame;
    private static SideFrame settingsFrame;
    private static SideFrame tutorialFrame;
    private static GameState gameState;

    public static GameState getGameState() {
        System.out.println(gameState.getObjectModels().size() + "  -   -   "+ gameState.getObjectModels().getLast().getId());
        return gameState;
    }

    public static void setGameState(GameState gameState) {
        Application.gameState = gameState;
    }

    public static SideFrame getSettingsFrame() {
        if (settingsFrame == null) settingsFrame = new SideFrame(SettingsPanel.class);
        return settingsFrame;
    }

    public static SideFrame getTutFrame() {
        if (tutorialFrame == null) tutorialFrame = new SideFrame(TutorialPanel.class);
        return tutorialFrame;
    }


    public static PrimaryFrame getPrimaryFrame() {
        if (primaryFrame == null) primaryFrame = new PrimaryFrame();
        return primaryFrame;
    }

    public static GameFrame getGameFrame() {
        if (gameFrame == null) gameFrame = new GameFrame();
        return gameFrame;
    }

    public static SideFrame getShopFrame() {
        if (shopFrame == null) shopFrame = new SideFrame(ShopPanel.class);
        return shopFrame;
    }

    public static SideFrame getSkillTreeFrame() {
        if (skillTreeFrame == null) skillTreeFrame = new SideFrame(SkillTreePanel.class);
        return skillTreeFrame;
    }


    @Override
    public void run() {
        initPFrame();
    }

    public static void initPFrame() {

        if (gameFrame != null) gameFrame = null;
        if (scoreFrame != null) scoreFrame.dispose();
        if (!EpsilonKeyListener.isStarted()) epsilonKeyListener.startListener();
        if (!ShotgunMouseListener.isStarted()) shotgunMouseListener.startListener();
        scoreFrame = null;

        gameFrame = new GameFrame();
        shopFrame = new SideFrame(ShopPanel.class);

        getSkillTreeFrame().setVisible(false);
        getSettingsFrame().setVisible(false);
        getPrimaryFrame().setVisible(true);
        SoundPlayer.getSoundPlayer();
    }

    public static void initScoreFrame(boolean won) {
        GameSaveManager.deleteSaveFile();
        getGameFrame().setVisible(false);
        scoreFrame = new ScoreFrame(gameFrame.getLabels(), won);
        pauseUpdate();
    }

    public static void showTut() {
        getPrimaryFrame().setVisible(false);
        getTutFrame().setVisible(true);
    }


    public static void startGame() {
        getPrimaryFrame().setVisible(false);
        getShopFrame().setVisible(false);
        //minimize tabs
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_WINDOWS);
            robot.keyPress(KeyEvent.VK_D);
            robot.keyRelease(KeyEvent.VK_D);
            robot.keyRelease(KeyEvent.VK_WINDOWS);
        } catch (AWTException e) {
            e.printStackTrace();
        }
        resetGame();
        loadOrNewGame();
        initGFrame();
        new Update();
    }

    public static void startGame(int t) {//doesn't reset time
        initGFrame();
        nextLevel();
    }

    public static void initGFrame() {

        gameFrame = new GameFrame();
        gameFrame.setVisible(true);
        getGameFrame().initLabels();
        getGameFrame().shrinkFast();

    }

    public static void initShFrame() {
        pauseUpdate();
        getGameFrame().setVisible(false);
        getShopFrame().setVisible(true);

    }

    public static void initSTFrame() {
        getPrimaryFrame().setVisible(false);
        getSkillTreeFrame().setVisible(true);

    }

    public static void pauseUpdate() {
        modelUpdateTimer.stop();
        frameUpdateTimer.stop();
        emptyPanelEraser.stop();
        waveTimer.stop();
        ElapsedTime.pause();

    }

    public static void hideShFrame() {
        getShopFrame().setVisible(false);
        getGameFrame().setVisible(true);

        resumeUpdate();

    }

    public static void resumeUpdate() {
        ElapsedTime.resume();
        modelUpdateTimer.start();
        frameUpdateTimer.start();
        emptyPanelEraser.start();
        waveTimer.start();
    }

    public static void showSettings() {
        getPrimaryFrame().setVisible(false);
        getSettingsFrame().setVisible(true);
    }

    public static void resetGame() {
        setKilledEnemiesInWave(0);
        setKilledEnemiesTotal(0);
        nextLevel();
        EpsilonModel.getINSTANCE().setHp(EPSILON_HP);
        Writ.resetInitSeconds();

        Config.GAME_MIN_SIZE = 250;
        gamePanelsBounds = new HashMap<>();
        gamePanels = new ArrayList<>();

        waveReset();
        ElapsedTime.resetTime();

    }

    private static void waveReset() {
        Wave.waves.clear();
        Wave.setLevel(0);
        Wave.setStartNewWave(false);
        Wave.setBetweenWaves(true);
    }

    public static void nextLevel() {
        collectableModels = new ArrayList<>();
        abilityModels = new ArrayList<>();
        BulletModel.bulletModels = new ArrayList<>();
        ProjectileModel.projectileModels = new ArrayList<>();
        entityModels = new ArrayList<>();
        BlackOrbModel.blackOrbModels = new ArrayList<>();
        BlackOrbModel.setComplete(false);
        barricadosModels = new ArrayList<>();
        archmireModels = new ArrayList<>();
        omenoctModels = new ArrayList<>();

        AbilityView.abilityViews = new ArrayList<>();
        EntityView.entityViews = new ArrayList<>();

        BlackOrbView.resetOrbViews();
        EpsilonModel.newINSTANCE();
        getGameFrame().setXpAmount(EpsilonModel.getINSTANCE().getXp());

    }

    public static void checkpointSave() {
        GameSaveManager.saveGameState(gameState);
    }

    public static void loadOrNewGame() {
        GameState savedState = GameSaveManager.loadGameState();
        if (savedState != null) {
            boolean continueGame = promptUserToContinue();
            if (continueGame) {
                gameState = savedState;
                pauseOnGameState(gameState);
            } else {
                GameSaveManager.deleteSaveFile();
                gameState = new GameState(objectModels, gamePanels, gamePanelsBounds, Wave.getLevel(),
                        getKilledEnemiesInWave(), getKilledEnemiesTotal());
            }
        } else {
           gameState = new GameState(objectModels, gamePanels, gamePanelsBounds, Wave.getLevel(),
                   getKilledEnemiesInWave(), getKilledEnemiesTotal());
        }
    }

    private static boolean promptUserToContinue() {
        return JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(null,
                "you have a pre-saved game, wanna continue that?"
        );
    }

    private static void pauseOnGameState(GameState gameState) {
        synchronized (LOCK) {
            objectModels = gameState.getObjectModels();
            gamePanels = gameState.getGamePanels();
            gamePanelsBounds = gameState.getGamePanelsBounds();
            Wave.setLevel(gameState.getLevel());
            setKilledEnemiesInWave(gameState.getKilledEnemiesInWave());
            setKilledEnemiesTotal(gameState.getKilledEnemiesTotal());
            for (int i = 0; i < objectModels.size() ; i++) {
                var objectModel = objectModels.get(i);
                createObjectView(objectModel.getId(),
                        objectModel.getX(), objectModel.getY(),
                        objectModel.getWidth(), objectModel.getHeight());
            }
            for (int i = 0 ; i< gamePanels.size(); i++){
                var gamePanel = gamePanels.get(i);
                gamePanel.setBounds(gamePanelsBounds.get(gamePanel));
                getGameFrame().getLayeredPane().add(gamePanel);
            }
        }
        Update.updateView();

    }

}
