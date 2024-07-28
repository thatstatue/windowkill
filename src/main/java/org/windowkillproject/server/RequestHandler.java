package org.windowkillproject.server;

import org.windowkillproject.controller.ElapsedTime;
import org.windowkillproject.controller.Update;
import org.windowkillproject.server.model.Wave;
import org.windowkillproject.server.model.Writ;
import org.windowkillproject.server.model.abilities.AbilityModel;
import org.windowkillproject.server.model.abilities.BulletModel;
import org.windowkillproject.server.model.abilities.CollectableModel;
import org.windowkillproject.server.model.abilities.ProjectileModel;
import org.windowkillproject.server.model.entities.EntityModel;
import org.windowkillproject.server.model.entities.EpsilonModel;
import org.windowkillproject.server.model.entities.enemies.minibosses.BarricadosModel;
import org.windowkillproject.server.model.entities.enemies.minibosses.BlackOrbModel;
import org.windowkillproject.server.model.entities.enemies.normals.ArchmireModel;
import org.windowkillproject.server.model.entities.enemies.normals.OmenoctModel;

import java.util.ArrayList;

import static org.windowkillproject.Constants.WRIT_COOL_DOWN_SECONDS;
import static org.windowkillproject.Request.*;
import static org.windowkillproject.controller.ElapsedTime.getTotalSeconds;
import static org.windowkillproject.controller.Update.*;
import static org.windowkillproject.server.Config.EPSILON_HP;
import static org.windowkillproject.server.model.Wave.waveTimer;
import static org.windowkillproject.server.model.entities.EpsilonModel.clientEpsilonModelMap;
import static org.windowkillproject.server.model.entities.enemies.EnemyModel.setKilledEnemiesInWave;
import static org.windowkillproject.server.model.entities.enemies.EnemyModel.setKilledEnemiesTotal;

public class RequestHandler implements Runnable{
    private final String request;
    private final ClientHandler clientHandler;
    public RequestHandler(ClientHandler clientHandler , String request) {
        this.clientHandler = clientHandler;
        this.request = request;

    }

    @Override
    public void run() {
        String[] parts = request.split(REGEX_SPLIT);
        switch (parts[0]){
            case REQ_PAUSE_UPDATE -> handlePauseUpdate();
            case REQ_RESUME_UPDATE -> handleResumeUpdate();
            case REQ_NEW_UPDATE -> new Update();
            case REQ_EPSILON_NEW_INSTANCE -> handleEpsilonXP();
            case REQ_WAVE_RESET -> handleWaveReset();
            case REQ_NEXT_LEVEL -> handleNextLevel();
            case REQ_RESET_GAME -> handleResetGame();
            case REQ_EPSILON_ANCHOR -> handleEpsilonAnchor();
            case REQ_WRIT_INIT -> handleWritInit();
            case REQ_SENSITIVITY_SET -> handleSensitivitySet(parts[1]);
            case REQ_GET_EPSILON_RADIUS -> handleGetEpsilonRadius();
            case REQ_EPSILON_XP -> handleEpsilonXp();
            case REQ_WRIT_CHOSEN -> handleWritChosen();
            case REQ_INCREASE_EPSILON_RADIUS -> handleIncreaseEpsilonRadius();

            case RES_ARE_KEYS_PRESSED -> handleAreKeysPressed();


        }
    }
    private void handleIncreaseEpsilonRadius(){
        var epsilon = clientEpsilonModelMap.get(clientHandler);
        epsilon.setRadius(epsilon.getRadius()+6);
    }

    private void handleWritChosen() {
        clientHandler.sendMessage(RES_WRIT_CHOSEN+ REGEX_SPLIT+ clientHandler.getWrit().getChosenSkill());
    }

    private void handleEpsilonXp() {
        clientHandler.sendMessage(RES_EPSILON_XP
                + REGEX_SPLIT + clientEpsilonModelMap.get(clientHandler).getXp());
    }

    private void handleGetEpsilonRadius() {
        clientHandler.sendMessage(RES_GET_EPSILON_RADIUS+ REGEX_SPLIT+
                clientEpsilonModelMap.get(clientHandler).getRadius());
    }

    private void handleEpsilonAnchor(){
        var epsilonModel = clientEpsilonModelMap.get(clientHandler);
        int xo = epsilonModel.getXO();
        int yo = epsilonModel.getYO();
        clientHandler.sendMessage(RES_EPSILON_ANCHOR + REGEX_SPLIT+
                xo +REGEX_SPLIT + yo);
    }
    private void handleWritInit(){
        var writ = clientHandler.getWrit();
        if (writ.getChosenSkill() != null) {
            long deltaT = getTotalSeconds() - writ.getInitSeconds();
            if (deltaT <= 0 || deltaT >= WRIT_COOL_DOWN_SECONDS) {
                writ.setInitSeconds();
                writ.acceptedClicksAddIncrement();
            }
        }
    }

    private void handleResetGame(){
        var epsilonModel = clientEpsilonModelMap.get(clientHandler);
        epsilonModel.setHp(EPSILON_HP);

        clientHandler.getWrit().resetInitSeconds();
        Config.GAME_MIN_SIZE = 250;

        ElapsedTime.resetTime();
        setKilledEnemiesInWave(0);
        setKilledEnemiesTotal(0);
    }
    private void handleAreKeysPressed(){
        String[] parts = request.split(REGEX_SPLIT);
        var epsilonModel = clientEpsilonModelMap.get(clientHandler);

        epsilonModel.setLeftPressed(Boolean.parseBoolean(parts[1]));
        epsilonModel.setRightPressed(Boolean.parseBoolean(parts[2]));
        epsilonModel.setUpPressed(Boolean.parseBoolean(parts[3]));
        epsilonModel.setDownPressed(Boolean.parseBoolean(parts[5]));


    }
    private void handleEpsilonXP() {
        var epsilonModel = clientEpsilonModelMap.get(clientHandler);
        int xp = 0;
        if (epsilonModel != null) xp = epsilonModel.getXp();
        clientHandler.sendMessage(RES_SET_EPSILON_XP+REGEX_SPLIT+xp);
    }

    private static void handlePauseUpdate() {
        modelUpdateTimer.stop();
        frameUpdateTimer.stop();
        emptyPanelEraser.stop();
        waveTimer.stop();
        ElapsedTime.pause();
    }
    private static void handleResumeUpdate() {
        ElapsedTime.resume();
        modelUpdateTimer.start();
        frameUpdateTimer.start();
        emptyPanelEraser.start();
        waveTimer.start();
    }
    private static void handleWaveReset(){
        Wave.waves.clear();
        Wave.setLevel(0);
        Wave.setStartNewWave(false);
        Wave.setBetweenWaves(true);
    }
    private static void handleNextLevel(){
        CollectableModel.collectableModels = new ArrayList<>();
        AbilityModel.abilityModels = new ArrayList<>();
        BulletModel.bulletModels = new ArrayList<>();
        ProjectileModel.projectileModels = new ArrayList<>();
        EntityModel.entityModels = new ArrayList<>();
        BlackOrbModel.blackOrbModels = new ArrayList<>();
        BlackOrbModel.setComplete(false);
        BarricadosModel.barricadosModels = new ArrayList<>();
        ArchmireModel.archmireModels = new ArrayList<>();
        OmenoctModel.omenoctModels = new ArrayList<>();
    }
    private static void handleSensitivitySet (String value){
        switch (value) {
            case "0" -> Config.SENSITIVITY_RATE = 100;
            case "1" -> Config.SENSITIVITY_RATE = 50;
            case "2" -> Config.SENSITIVITY_RATE = 0;
            default -> System.out.println("error: undefined sensitivity rate");
        }
    }
}
