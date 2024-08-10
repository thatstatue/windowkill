package org.windowkillproject.client.ui;

import org.windowkillproject.client.ui.frames.*;
import org.windowkillproject.client.ui.listeners.EpsilonKeyListener;
import org.windowkillproject.client.ui.listeners.ShotgunMouseListener;

import org.windowkillproject.client.ui.panels.etc.SettingsPanel;
import org.windowkillproject.client.ui.panels.league.Alert;
import org.windowkillproject.client.ui.panels.shop.ShopPanel;
import org.windowkillproject.client.ui.panels.shop.SkillTreePanel;
import org.windowkillproject.client.ui.panels.etc.TutorialPanel;
import org.windowkillproject.client.GameClient;

import org.windowkillproject.client.ui.sounds.SoundPlayer;
import org.windowkillproject.client.view.ViewsRenderer;
import org.windowkillproject.client.view.entities.EntityView;
import org.windowkillproject.controller.data.GameSaveManager;
import org.windowkillproject.controller.data.GameState;
import org.windowkillproject.client.view.abilities.AbilityView;
import org.windowkillproject.client.view.entities.enemies.minibosses.BlackOrbView;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import static org.windowkillproject.Request.*;
import static org.windowkillproject.client.ui.panels.game.PanelView.panelViews;
import static org.windowkillproject.client.ui.panels.game.PanelView.setPanelViews;
import static org.windowkillproject.client.view.abilities.AbilityView.abilityViews;
import static org.windowkillproject.client.view.abilities.AbilityView.setAbilityViews;
import static org.windowkillproject.client.view.entities.EntityView.entityViews;

public class App implements Runnable {
    private PrimaryFrame primaryFrame;

    public SoundPlayer getSoundPlayer() {
        return soundPlayer;
    }

    private final ShotgunMouseListener shotgunMouseListener;
    private final EpsilonKeyListener epsilonKeyListener;
    private GameFrame gameFrame;
    private SideFrame shopFrame;
    private ScoreFrame scoreFrame;
    private SideFrame skillTreeFrame;
    private SideFrame settingsFrame;
    private LeagueFrame leagueFrame;

    public void setEmpowerInitSeconds(long empowerInitSeconds) {
        shotgunMouseListener.setEmpowerInitSeconds(empowerInitSeconds);
    }

    private SideFrame tutorialFrame;
    private GameState gameState;//todo move to server side
    private final GameClient client;
    private String globeId; //todo every game that starts, server aligns the globe id to its clients
    private final SoundPlayer soundPlayer;

    public App(GameClient client) {
        this.client = client;
        shotgunMouseListener = new ShotgunMouseListener(client);
        epsilonKeyListener = new EpsilonKeyListener(client);
        soundPlayer = new SoundPlayer();
    }

    public String getGlobeId() {
        return globeId;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public SideFrame getSettingsFrame() {
        if (settingsFrame == null) settingsFrame = new SideFrame(SettingsPanel.class, client);
        return settingsFrame;
    }

    public SideFrame getTutFrame() {
        if (tutorialFrame == null) tutorialFrame = new SideFrame(TutorialPanel.class, client);
        return tutorialFrame;
    }

    public LeagueFrame getLeagueFrame() {
        if (leagueFrame == null) {
            leagueFrame = new LeagueFrame(client);
//            leagueFrame.getUsername(client);
        }
        return leagueFrame;
    }

    public void showLeague() {
        getLeagueFrame().setVisible(true);
        getPrimaryFrame().setVisible(false);
        client.sendMessage(LEAGUE_REDIRECT + REGEX_SPLIT+REQ_SQUADS_LIST);

        if (!getLeagueFrame().isUsernamed()) {
            getLeagueFrame().getUsername(client);
        }

    }


    public PrimaryFrame getPrimaryFrame() {
        if (primaryFrame == null) primaryFrame = new PrimaryFrame(client);
        return primaryFrame;
    }

    public GameFrame getGameFrame() {
        if (gameFrame == null) gameFrame = new GameFrame(globeId, client);
        return gameFrame;
    }

    public SideFrame getShopFrame() {
        if (shopFrame == null) shopFrame = new SideFrame(ShopPanel.class, client);
        return shopFrame;
    }

    public SideFrame getSkillTreeFrame() {
        if (skillTreeFrame == null) skillTreeFrame = new SideFrame(SkillTreePanel.class, client);
        return skillTreeFrame;
    }


    @Override
    public void run() {
        initPFrame();
    }

    public void initPFrame() {

        if (gameFrame != null) gameFrame = null;
        if (scoreFrame != null) scoreFrame.dispose();
        if (leagueFrame!=null) leagueFrame.setVisible(false);
        if (!EpsilonKeyListener.isStarted()) epsilonKeyListener.startListener();
        if (!ShotgunMouseListener.isStarted()) shotgunMouseListener.startListener();
        scoreFrame = null;

        gameFrame = new GameFrame(globeId, client);
        shopFrame = new SideFrame(ShopPanel.class, client);

        getSkillTreeFrame().setVisible(false);
        getSettingsFrame().setVisible(false);
        getPrimaryFrame().setVisible(true);
        if (!(client.getAlert().equals(Alert.noAlerts))){
            JOptionPane.showMessageDialog(null, client.getAlert().getMessage());
        }
    }

    public void initScoreFrame(boolean won) {
        GameSaveManager.deleteSaveFile();
        getGameFrame().setVisible(false);
        pauseUpdate();
        scoreFrame = new ScoreFrame(client, gameFrame.getLabels(), won);

        //nextLevel();
    }

    public void showTut() {
        getPrimaryFrame().setVisible(false);
        getTutFrame().setVisible(true);
    }


    public void startGame(boolean isBattle) {
        getPrimaryFrame().setVisible(false);
        getShopFrame().setVisible(false);
        //minimize tabs
        minimize();
        if (!isBattle) {
            loadOrNewGame();
            setPanelViews(new ArrayList<>());
            client.sendMessage(REQ_NEW_GAME_SINGLE);
        }
        resetGame();
        initGFrame();//todo
    }

    private void minimize() {
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_WINDOWS);
            robot.keyPress(KeyEvent.VK_D);
            robot.keyRelease(KeyEvent.VK_D);
            robot.keyRelease(KeyEvent.VK_WINDOWS);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public void startGame(int t) {//doesn't reset time
        initGFrame();
        nextLevel();
    }

    public void initGFrame() {//todo add battle mode and stuff
        gameFrame = new GameFrame(globeId, client);
        gameFrame.setVisible(true);
//        getGameFrame().initLabels();
//        client.sendMessage(REQ_SHRINK_FAST);

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
//        globeId = "";
//        nextLevel();
        waveReset();
        client.sendMessage(REQ_RESET_GAME);
    }

    private void waveReset() {
        client.sendMessage(REQ_WAVE_RESET);
    }

    public void nextLevel() {

        setAbilityViews(new ArrayList<>());
        entityViews = new ArrayList<>();
        BlackOrbView.resetOrbViews();
        client.sendMessage(REQ_NEXT_LEVEL);
//        client.sendMessage(REQ_EPSILON_NEW_INSTANCE);

    }

    public void checkpointSave() {
        GameSaveManager.saveGameState(gameState);
    }

    public boolean loadOrNewGame() {
        GameState savedState = GameSaveManager.loadGameState(); //todo
//        if (savedState != null) {
        return promptUserToContinue();
//            if (continueGame) {
//                globeId = savedId;
//                gameState = savedState;
//                pauseOnGameState(savedState);
//            } else {
//                GameSaveManager.deleteSaveFile();
//                gameState = new GameState(objectModels, panelViews, gamePanelsBounds, Wave.getLevel(),
//                        getKilledEnemiesInWave(), getKilledEnemiesTotal());
//            }
//        } else {
//           gameState = new GameState(objectModels, panelViews, gamePanelsBounds, Wave.getLevel(),
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
//            panelViews = gameState.getGamePanels();
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
//            for (int i = 0 ; i< panelViews.size(); i++){
//                var panelView = panelViews.get(i);
//                panelView.setBounds(gamePanelsBounds.get(panelView));
//                getGameFrame().getLayeredPane().add(panelView);
//            }
//        }
//        GameLoop.updateView();

    }

    public void updateGame(String message) {/*
    todo any thing that needs to be changed in client
    will be sent as a message from server to client and
    it will be decided what to do with it here
    */
        String[] parts = message.split(REGEX_SPLIT);
        switch (parts[0]) {
            case REQ_SET_WAVE_LEVEl -> gameFrame.setWaveLevel(Integer.parseInt(parts[1]));
            case REQ_PLAY_END_WAVE_SOUND -> soundPlayer.playEndWaveSound();
            case REQ_PLAY_CREATE_SOUND -> soundPlayer.playCreateSound();
            case REQ_INIT_SCORE_FRAME -> initScoreFrame(Boolean.parseBoolean(parts[1]));
            case RES_SET_EPSILON_XP -> handleEpsilonXp(Integer.parseInt(parts[1]));
            case RES_WRIT_CHOSEN -> gameFrame.getMainPanelView().setWrit(parts[1]);
            case REQ_PLAY_HIT_SOUND -> soundPlayer.playHitSound();
            case REQ_PLAY_BULLET_SOUND -> soundPlayer.playBulletSound();
            case RES_TOTAL_KILLS -> gameFrame.getMainPanelView().setTotalKills(parts[1]);
            case RES_WAVE_LEVEL -> gameFrame.getMainPanelView().setWaveLevel(parts[1]);
            case RES_EPSILON_HP -> gameFrame.getMainPanelView().setHpAmount(Integer.parseInt(parts[1]));
            case REQ_START_GAME_1 -> startGame(1);
            case REQ_PLAY_DESTROY_SOUND -> soundPlayer.playDestroySound();
            case REQ_SET_CLOCK -> gameFrame.getMainPanelView().setClockTime(parts[1]);
            case REQ_REPAINT_GAME_FRAME -> handleRepaint();
            case RES_GLOBE_ID -> handleGlobeId(parts[1]);
            case RES_NEW_ONLINE_PLAYER -> getLeagueFrame().setUsernamed(Boolean.parseBoolean(parts[1]));
            case BROADCAST_REDIRECT -> ViewsRenderer.updateViews(parts);
            case REQ_PAUSE_UPDATE -> client.sendMessage(REQ_PAUSE_UPDATE);
            case RES_SQUAD_NAMES -> client.getApp().getLeagueFrame().setSquadNames(parts);
            case RES_OCCUPANTS -> client.getApp().getLeagueFrame().setOccupants(parts);
            case REQ_BEGIN_GAME -> client.getApp().startGame(true);

        }
        handleKeysPressed();
    }

    private void handleGlobeId(String id) {
        globeId = id;
//        gameFrame.getMainPanelView().setId(id);
    }


    private void handleRepaint() {
        gameFrame.revalidate();

        int t = 0;
        for (int i =0 ; i< panelViews.size(); i++){
            var panelView = panelViews.get(i);
            if(panelView.getClient().getApp().getGlobeId().equals(globeId)) {
                panelView.revalidate();
                panelView.repaint();
            }
        }

        for (int i = 0; i < abilityViews.size(); i++) {
            AbilityView abilityView = abilityViews.get(i);
            if(abilityView.getGlobeId().equals(globeId)) {
                abilityView.revalidate();
                abilityView.repaint();
            }
        }

        for (int i = 0; i < entityViews.size(); i++) {
            EntityView entityView = entityViews.get(i);
            if(entityView.getGlobeId().equals(globeId)) {
                entityView.revalidate();
                entityView.repaint();
            }
        }

        gameFrame.repaint();
    }

    private void handleEpsilonXp(int xp) {
        gameFrame.setXpAmount(xp);

    }

    private void handleKeysPressed() {
        client.sendMessage(RES_ARE_KEYS_PRESSED + REGEX_SPLIT +
                EpsilonKeyListener.isLeftPressed + REGEX_SPLIT +
                EpsilonKeyListener.isRightPressed + REGEX_SPLIT +
                EpsilonKeyListener.isUpPressed + REGEX_SPLIT +
                EpsilonKeyListener.isDownPressed);
    }

}
