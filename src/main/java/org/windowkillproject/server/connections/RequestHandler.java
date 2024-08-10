package org.windowkillproject.server.connections;

import org.windowkillproject.MessageQueue;

import org.windowkillproject.server.Config;
import org.windowkillproject.server.model.abilities.BulletModel;
import org.windowkillproject.server.model.entities.EpsilonModel;
import org.windowkillproject.server.model.entities.enemies.minibosses.BlackOrbModel;
import org.windowkillproject.server.model.globe.GlobeModel;
import org.windowkillproject.server.model.globe.GlobesManager;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Map;

import static org.windowkillproject.Constants.WRIT_COOL_DOWN_SECONDS;
import static org.windowkillproject.Request.*;
import static org.windowkillproject.server.Config.*;
import static org.windowkillproject.server.model.entities.EpsilonModel.queueEpsilonModelMap;

public class RequestHandler implements Runnable{
    private final String request;
    private final MessageQueue messageQueue;
    RequestHandler(MessageQueue messageQueue , String request) {
        this.messageQueue = messageQueue;
        this.request = request;
        epsilonModel = queueEpsilonModelMap.get(messageQueue);
    }
    private EpsilonModel epsilonModel;

    @Override
    public void run() {
        String[] parts = request.split(REGEX_SPLIT);
        switch (parts[0]){
            case REQ_PAUSE_UPDATE -> handlePauseUpdate();
            case REQ_RESUME_UPDATE -> handleResumeUpdate();
            case REQ_START_GAME_LOOP -> epsilonModel.getGlobeModel().getGameLoop().start();
            case REQ_GET_EPSILON_XP -> handleEpsilonXP();
            case REQ_WAVE_RESET -> handleWaveReset();
//            case REQ_NEXT_LEVEL -> handleNextLevel();
            case REQ_RESET_GAME -> handleResetGame();
            case REQ_EPSILON_ANCHOR -> handleEpsilonAnchor();
            case REQ_WRIT_INIT -> handleWritInit();
            case REQ_SENSITIVITY_SET -> handleSensitivitySet(parts[1]);
            case REQ_GET_EPSILON_RADIUS -> handleGetEpsilonRadius();
            case REQ_EPSILON_NEW_INSTANCE -> handleEpsilonNewInstance();
            case REQ_WRIT_CHOSEN -> handleWritChosen();
            case REQ_INCREASE_EPSILON_RADIUS -> handleIncreaseEpsilonRadius();
            case RES_ARE_KEYS_PRESSED -> handleAreKeysPressed(parts);
//            case REQ_REMOVE_EPSILON -> handleRemoveEpsilon(parts[1]);
            case REQ_TOTAL_KILLS -> handleTotalKills();
            case REQ_WAVE_LEVEL -> handleWaveLevel();
            case REQ_SHRINK_FAST -> handleShrinkFast();
            case REQ_SHOOT_BULLET -> handleShootBullet(parts);
            case REQ_DIFFICULTY -> handleDifficulty(parts[1]);
            case REQ_NEW_GAME_SINGLE -> handleNewGameSingle();
            case LEAGUE_REDIRECT -> League.updateLeague(parts, messageQueue);
        }
    }
    private void handleEpsilonNewInstance(){

    }
    private void handleShrinkFast(){
        epsilonModel.getGlobeModel().getGameLoop().start();
        epsilonModel.getGlobeModel().shrinkFast();
    }
    private void handleNewGameSingle(){
        var id = GlobesManager.newGlobe(messageQueue, null,"");
//        messageQueue.setGlobeId(id);
        GlobeModel globeModel = GlobesManager.getGlobeFromId(id);
        epsilonModel = globeModel.getEpsilons().getFirst();
//        globeModel.mainPanelModel = new MainPanelModel(id);
//        globeModel.getGameLoop().start();
//        globeModel.initProperties();
    }
    private void handleShootBullet(String[] parts){
        var globe = epsilonModel.getGlobeModel();
        //if (!globe.getSmileyHeadModel().isAppearing()){ //todo
            int empowerInitSeconds = Integer.parseInt(parts[1]);
            Point2D mouseLoc = new Point2D.Double(Double.parseDouble(parts[2]), Double.parseDouble(parts[3]));
            BulletModel bulletModel = new BulletModel(globe.getId(),
                    epsilonModel.getXO(), epsilonModel.getYO(), mouseLoc, epsilonModel);
            bulletModel.shoot();
            long deltaT = globe.getElapsedTime().getTotalSeconds() - empowerInitSeconds;
            if (deltaT > 0 && deltaT <= 10) { //doesn't allow too many bullets
                globe.getGameManager().empowerBullets(epsilonModel, mouseLoc);
            }
        //}

    }
    private void handleTotalKills(){
        System.out.println(epsilonModel.getGlobeModel().getKilledEnemiesTotal());
        messageQueue.enqueue(RES_TOTAL_KILLS+REGEX_SPLIT+epsilonModel.getGlobeModel().getKilledEnemiesTotal());
    }
    private void handleWaveLevel(){
        messageQueue.enqueue(RES_WAVE_LEVEL+REGEX_SPLIT+epsilonModel.getGlobeModel().getWaveFactory().getLevel());
    }
    private void handleRemoveEpsilon(String epsilonId){
        EpsilonModel epsilon = null;
        for (Map.Entry<MessageQueue, EpsilonModel> entry : queueEpsilonModelMap.entrySet()) {
            EpsilonModel epsilonModel = entry.getValue();
            if (epsilonId.equals(epsilonModel.getId())) {
                epsilon = epsilonModel;
                break;
            }
        }
        if (epsilon!=null){
            var entityModels = epsilon.getGlobeModel().getEntityModels();
//            entityModels.remove(epsilon);
        }

    }
    private void handleIncreaseEpsilonRadius(){
        var epsilon = epsilonModel;
        epsilon.setRadius(epsilon.getRadius()+6);
    }

    private void handleWritChosen() {
        messageQueue.enqueue(RES_WRIT_CHOSEN+ REGEX_SPLIT+
                epsilonModel.getWrit().getChosenSkill());
    }

    private void handleEpsilonXp() {
        messageQueue.enqueue(RES_SET_EPSILON_XP + REGEX_SPLIT +
                epsilonModel.getXp());
    }

    private void handleGetEpsilonRadius() {
        messageQueue.enqueue(RES_GET_EPSILON_RADIUS+ REGEX_SPLIT+
                epsilonModel.getRadius());
    }

    private void handleEpsilonAnchor(){
        int xo = epsilonModel.getXO();
        int yo = epsilonModel.getYO();
        messageQueue.enqueue(RES_EPSILON_ANCHOR + REGEX_SPLIT+
                xo +REGEX_SPLIT + yo);
    }
    private void handleWritInit(){
        var writ = epsilonModel.getWrit();
        if (writ.getChosenSkill() != null) {
            long deltaT = epsilonModel.getGlobeModel().getElapsedTime().getTotalSeconds() - writ.getInitSeconds();
            if (deltaT <= 0 || deltaT >= WRIT_COOL_DOWN_SECONDS) {
                writ.setInitSeconds();
                writ.acceptedClicksAddIncrement();
            }
        }
    }

    private void handleResetGame(){
        handleTotalKills();
        epsilonModel.setHp(EPSILON_HP);
        epsilonModel.getWrit().resetInitSeconds();
        Config.GAME_MIN_SIZE = 250;
        var globe = epsilonModel.getGlobeModel();
        globe.getElapsedTime().resetTime();
        globe.resetKilledEnemiesInWave();
        globe.resetKilledEnemiesTotal();
    }
    private void handleAreKeysPressed(String[] parts){
        boolean isLeftPressed = Boolean.parseBoolean(parts[1]);
        boolean isRightPressed = Boolean.parseBoolean(parts[2]);
        boolean isUpPressed = Boolean.parseBoolean(parts[3]);
        boolean isDownPressed = Boolean.parseBoolean(parts[4]);
        if (epsilonModel!=null) {
            epsilonModel.setLeftPressed(isLeftPressed);
            epsilonModel.setRightPressed(isRightPressed);
            epsilonModel.setUpPressed(isUpPressed);
            epsilonModel.setDownPressed(isDownPressed);
        }
    }
    private void handleEpsilonXP() {
        int xp = 0;
        if (epsilonModel != null) xp = epsilonModel.getXp();
        messageQueue.enqueue(RES_SET_EPSILON_XP +REGEX_SPLIT+xp);
    }

    public void handlePauseUpdate() {
        var globe = epsilonModel.getGlobeModel();
        globe.pause(false);
    }
    private void handleResumeUpdate() {
        var globe = epsilonModel.getGlobeModel();
        globe.getElapsedTime().resume();
        globe.getGameLoop().start();
        var waves = globe.getWaveFactory();
        if(!waves.isForceStop()) waves.waveTimer.start();
    }
    private void handleWaveReset(){
        var waveFactory = epsilonModel.getGlobeModel().getWaveFactory();
        waveFactory.setForceStop(false);
        waveFactory.getWaves().clear();
        waveFactory.setLevel(0);
        waveFactory.setStartNewWave(true);
        waveFactory.setBetweenWaves(false);
    }
    private void handleNextLevel(){
        var globe = epsilonModel.getGlobeModel();
        globe.setCollectableModels(new ArrayList<>());
        globe.setAbilityModels(new ArrayList<>());
        globe.setBulletModels(new ArrayList<>());
        globe.setProjectileModels(new ArrayList<>());
        globe.setEntityModels(new ArrayList<>());
        globe.setBlackOrbModels(new ArrayList<>());
        BlackOrbModel.setComplete(false);
        globe.setBarricadosModels(new ArrayList<>());
        globe.setArchmireModels(new ArrayList<>());
        globe.setOmenoctModels(new ArrayList<>());
    }
    private static void handleSensitivitySet (String value){
        switch (value) {
            case "0" -> SENSITIVITY_RATE = 100;
            case "1" -> SENSITIVITY_RATE = 50;
            case "2" -> SENSITIVITY_RATE = 0;
            default -> System.out.println("error: undefined sensitivity rate");
        }
    }
    private static void handleDifficulty(String value){
        switch (value){
            case "0" -> {
                Config.ENEMY_RADIUS = 25;
                BOUND = 4;
                MAX_ENEMY_SPEED = 3;
            }
            case "1" -> {
                ENEMY_RADIUS = 20;
                BOUND = 5;
                MAX_ENEMY_SPEED = 4;
            }
            case "2" -> {
                ENEMY_RADIUS = 15;
                BOUND = 6;
                MAX_ENEMY_SPEED = 4;
            }
            default -> System.out.println("didn't get any rate for difficulty");
        }
    }

}
