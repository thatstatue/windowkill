package org.windowkillproject.client.ui;

import org.windowkillproject.client.ui.frames.GameFrame;
import org.windowkillproject.client.ui.frames.SideFrame;
import org.windowkillproject.client.ui.listeners.EpsilonKeyListener;
import org.windowkillproject.client.ui.listeners.ShotgunMouseListener;

import org.windowkillproject.client.ui.panels.etc.SettingsPanel;
import org.windowkillproject.client.ui.panels.shop.ShopPanel;
import org.windowkillproject.client.ui.panels.shop.SkillTreePanel;
import org.windowkillproject.client.ui.panels.etc.TutorialPanel;
import org.windowkillproject.client.GameClient;
import org.windowkillproject.client.ui.frames.PrimaryFrame;
import org.windowkillproject.client.ui.frames.ScoreFrame;

import org.windowkillproject.controller.data.GameSaveManager;
import org.windowkillproject.controller.data.GameState;
import org.windowkillproject.client.view.abilities.AbilityView;
import org.windowkillproject.client.view.entities.EntityView;
import org.windowkillproject.client.view.entities.enemies.minibosses.BlackOrbView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;

import static org.windowkillproject.Request.*;
import static org.windowkillproject.client.ui.panels.game.GamePanel.gamePanels;
import static org.windowkillproject.client.ui.panels.game.GamePanel.gamePanelsBounds;

public class App implements Runnable {
    private PrimaryFrame primaryFrame;
    private final ShotgunMouseListener shotgunMouseListener;
    private final EpsilonKeyListener epsilonKeyListener;
    private GameFrame gameFrame;
    private SideFrame shopFrame;
    private ScoreFrame scoreFrame;
    private SideFrame skillTreeFrame;
    private SideFrame settingsFrame;
    private SideFrame tutorialFrame;
    private GameState gameState;
    private final GameClient client ;
    private final SoundPlayer soundPlayer;

    public App(GameClient client) {
        this.client = client;
        shotgunMouseListener = new ShotgunMouseListener(client);
        epsilonKeyListener = new EpsilonKeyListener(client);
        soundPlayer = new SoundPlayer();
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public SideFrame getSettingsFrame() {
        if (settingsFrame == null) settingsFrame = new SideFrame(SettingsPanel.class);
        return settingsFrame;
    }

    public SideFrame getTutFrame() {
        if (tutorialFrame == null) tutorialFrame = new SideFrame(TutorialPanel.class);
        return tutorialFrame;
    }


    public PrimaryFrame getPrimaryFrame() {
        if (primaryFrame == null) primaryFrame = new PrimaryFrame();
        return primaryFrame;
    }

    public GameFrame getGameFrame() {
        if (gameFrame == null) gameFrame = new GameFrame(client);
        return gameFrame;
    }

    public SideFrame getShopFrame() {
        if (shopFrame == null) shopFrame = new SideFrame(ShopPanel.class);
        return shopFrame;
    }

    public SideFrame getSkillTreeFrame() {
        if (skillTreeFrame == null) skillTreeFrame = new SideFrame(SkillTreePanel.class);
        return skillTreeFrame;
    }


    @Override
    public void run() {
        initPFrame();
    }

    public void initPFrame() {

        if (gameFrame != null) gameFrame = null;
        if (scoreFrame != null) scoreFrame.dispose();
        if (!EpsilonKeyListener.isStarted()) epsilonKeyListener.startListener();
        if (!ShotgunMouseListener.isStarted()) shotgunMouseListener.startListener();
        scoreFrame = null;

        gameFrame = new GameFrame(client);
        shopFrame = new SideFrame(ShopPanel.class);

        getSkillTreeFrame().setVisible(false);
        getSettingsFrame().setVisible(false);
        getPrimaryFrame().setVisible(true);
    }

    public void initScoreFrame(boolean won) {
        GameSaveManager.deleteSaveFile();
        getGameFrame().setVisible(false);
        scoreFrame = new ScoreFrame(gameFrame.getLabels(), won);
        pauseUpdate();
    }

    public void showTut() {
        getPrimaryFrame().setVisible(false);
        getTutFrame().setVisible(true);
    }


    public void startGame() {
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
        client.sendMessage(REQ_NEW_UPDATE);
    }

    public void startGame(int t) {//doesn't reset time
        initGFrame();
        nextLevel();
    }

    public void initGFrame() {

        gameFrame = new GameFrame(client);
        gameFrame.setVisible(true);
        getGameFrame().initLabels();
        getGameFrame().shrinkFast();

    }

    public void initShFrame() {
        pauseUpdate();
        getGameFrame().setVisible(false);
        getShopFrame().setVisible(true);

    }

    public void initSTFrame() {
        getPrimaryFrame().setVisible(false);
        getSkillTreeFrame().setVisible(true);

    }

    public void pauseUpdate() {
        client.sendMessage(REQ_PAUSE_UPDATE);
    }

    public void hideShFrame() {
        getShopFrame().setVisible(false);
        getGameFrame().setVisible(true);

        resumeUpdate();

    }

    public void resumeUpdate() {
        client.sendMessage(REQ_RESUME_UPDATE);
    }

    public void showSettings() {
        getPrimaryFrame().setVisible(false);
        getSettingsFrame().setVisible(true);
    }

    public void resetGame() {

        nextLevel();
        waveReset();
        gamePanelsBounds = new HashMap<>();
        gamePanels = new ArrayList<>();
        client.sendMessage(REQ_RESET_GAME);
    }

    private void waveReset() {
        client.sendMessage(REQ_WAVE_RESET);
    }

    public void nextLevel() {
        client.sendMessage(REQ_NEXT_LEVEL);
        AbilityView.abilityViews = new ArrayList<>();
        EntityView.entityViews = new ArrayList<>();

        BlackOrbView.resetOrbViews();
        client.sendMessage(REQ_EPSILON_NEW_INSTANCE);
//        getGameFrame().setXpAmount(EpsilonModel.getINSTANCE().getXp()); todo check if is on time

    }

    public void checkpointSave() {
        GameSaveManager.saveGameState(gameState);
    }

    public void loadOrNewGame() {
//        GameState savedState = GameSaveManager.loadGameState(); todo
//        if (savedState != null) {
//            boolean continueGame = promptUserToContinue();
//            if (continueGame) {
//                gameState = savedState;
//                pauseOnGameState(savedState);
//            } else {
//                GameSaveManager.deleteSaveFile();
//                gameState = new GameState(objectModels, gamePanels, gamePanelsBounds, Wave.getLevel(),
//                        getKilledEnemiesInWave(), getKilledEnemiesTotal());
//            }
//        } else {
//           gameState = new GameState(objectModels, gamePanels, gamePanelsBounds, Wave.getLevel(),
//                   getKilledEnemiesInWave(), getKilledEnemiesTotal());
//        }
    }

    private boolean promptUserToContinue() {
        return JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(null,
                "you have a pre-saved game, wanna continue that?"
        );
    }

    private void pauseOnGameState(GameState gameState) {
//        synchronized (LOCK) { todo
//            System.out.println(gameState.getObjectModels().size());
//            objectModels = gameState.getObjectModels();
//            gamePanels = gameState.getGamePanels();
//            gamePanelsBounds = gameState.getGamePanelsBounds();
//            Wave.setLevel(gameState.getLevel());
//            setKilledEnemiesInWave(gameState.getKilledEnemiesInWave());
//            setKilledEnemiesTotal(gameState.getKilledEnemiesTotal());
//            for (int i = 0; i < objectModels.size() ; i++) {
//                var objectModel = objectModels.get(i);
//                createObjectView(objectModel.getId(),
//                        objectModel.getX(), objectModel.getY(),
//                        objectModel.getWidth(), objectModel.getHeight());
//            }
//            for (int i = 0 ; i< gamePanels.size(); i++){
//                var gamePanel = gamePanels.get(i);
//                gamePanel.setBounds(gamePanelsBounds.get(gamePanel));
//                getGameFrame().getLayeredPane().add(gamePanel);
//            }
//        }
//        Update.updateView();

    }
    public void updateGame(String message){/*
    todo any thing that needs to be changed in client
    will be sent as a message from server to client and
    it will be decided what to do with it here
    */
        String[] parts = message.split(REGEX_SPLIT);
        switch (parts[0]){
            case RES_SET_EPSILON_XP -> gameFrame.setXpAmount(Integer.parseInt(parts[1]));
            case REQ_ARE_KEYS_PRESSED -> handleKeysPressed();
            case RES_EPSILON_ANCHOR -> handleEpsilonAnchor(parts);
            case REQ_SET_WAVE_LEVEl -> gameFrame.setWaveLevel(Integer.parseInt(parts[1]));
            case REQ_PLAY_END_WAVE_SOUND -> soundPlayer.playEndWaveSound();
            case REQ_ENDING_SCENE -> gameFrame.endingScene();
            case REQ_PLAY_CREATE_SOUND -> soundPlayer.playCreateSound();
            case REQ_INIT_SCORE_FRAME -> initScoreFrame(Boolean.parseBoolean(parts[1]));
            case RES_EPSILON_XP -> gameFrame.getMainGamePanel().setXpAmount(Integer.parseInt(parts[1]));
            case RES_WRIT_CHOSEN -> gameFrame.getMainGamePanel().setWrit(parts[1]);
        }
    }

    private void handleEpsilonAnchor(String[] parts){
        var anchor = new Point2D.Double(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
        epsilonKeyListener.setEpsilonAnchor(anchor);
        shotgunMouseListener.setEpsilonAnchor(anchor);
    }
    private void handleKeysPressed(){
        client.sendMessage(RES_ARE_KEYS_PRESSED + REGEX_SPLIT+
                EpsilonKeyListener.isLeftPressed + REGEX_SPLIT+
                EpsilonKeyListener.isRightPressed + REGEX_SPLIT+
                EpsilonKeyListener.isUpPressed + REGEX_SPLIT+
                EpsilonKeyListener.isDownPressed);
    }

}
