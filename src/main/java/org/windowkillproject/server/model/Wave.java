package org.windowkillproject.server.model;

import org.windowkillproject.server.Config;
import org.windowkillproject.server.model.abilities.PortalModel;
import org.windowkillproject.server.model.entities.EntityModel;
import org.windowkillproject.server.model.entities.EpsilonModel;
import org.windowkillproject.server.model.entities.enemies.EnemyModel;
import org.windowkillproject.server.model.entities.enemies.minibosses.BarricadosModel;
import org.windowkillproject.server.model.entities.enemies.minibosses.BlackOrbModel;
import org.windowkillproject.server.model.entities.enemies.normals.*;
import org.windowkillproject.server.model.globe.GlobeModel;
import org.windowkillproject.server.model.globe.multiplayer.MonomachiaGlobe;
import org.windowkillproject.server.model.panelmodels.InternalPanelModel;
import org.windowkillproject.server.model.panelmodels.MainPanelModel;
import org.windowkillproject.server.model.panelmodels.PanelModel;
import org.windowkillproject.server.model.panelmodels.PanelStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Math.abs;
import static org.windowkillproject.Constants.*;
import static org.windowkillproject.Request.*;
import static org.windowkillproject.server.Config.*;

import static org.windowkillproject.controller.GameManager.*;
import static org.windowkillproject.controller.Utils.getSign;
import static org.windowkillproject.controller.Utils.isOccupied;
import static org.windowkillproject.server.model.globe.GlobesManager.getGlobeFromId;

public class Wave {
    private final String globeId;

    public Wave(String globeId) {
        this.globeId = globeId;
        setStartNewWave(false);
    }

    public GlobeModel getGlobeModel() {
        return getGlobeFromId(globeId);
    }

    private void startWaveTimer() {
        var waves = getGlobeModel().getWaveFactory();
        if (!waves.isForceStop()) waves.waveTimer.start();
    }
    private void stopWaveTimer() {
        var waves = getGlobeModel().getWaveFactory();
        if (!waves.isForceStop()) waves.waveTimer.stop();
    }


    private int getLevel() {
        return getGlobeModel().getWaveFactory().getLevel();
    }

    private void setBetweenWaves(boolean betweenWaves) {
        getGlobeModel().getWaveFactory().setBetweenWaves(betweenWaves);
    }

    private void setStartNewWave(boolean startNewWave) {
        getGlobeModel().getWaveFactory().setStartNewWave(startNewWave);
    }

    public void spawnWave() {
        AtomicInteger count = new AtomicInteger();
        getGlobeModel().getWaveFactory().waveTimer = new Timer((int) (WAVE_LOOP * (1 - 0.05 * getLevel())), null);
        getGlobeModel().getWaveFactory().waveTimer.addActionListener(e -> {
            int bound = getLevel() * 2 + Config.BOUND;
            if (count.get() < bound) {
                //doesn't allow too many enemies
                if (count.get() - getGlobeModel().getKilledEnemiesInWave() < MAX_ENEMIES) {
                    createRandomLocalEnemy();
                    count.getAndIncrement();
                }
            } else if (count.get() == bound) {
                //waits until u kill these enemies
                if (getGlobeModel().getKilledEnemiesInWave() == bound) {
                    getGlobeModel().performAction(REQ_PLAY_END_WAVE_SOUND);
                    setBetweenWaves(true);
                    count.getAndIncrement();
                }
            } else {
                //10 secs between waves
                if (count.get() < bound + 2) {
                    count.getAndIncrement();
                } else {
                    //time to go to the next level !
                    setStartNewWave(true);
                    setBetweenWaves(false);
                    if (getLevel() < END_OF_NORMAL) {
                        getGlobeModel().resetKilledEnemiesInWave();
                    } else {
                        getGlobeModel().endingScene();
                    }
                    stopWaveTimer();
                }
            }
        });
        startWaveTimer();

    }

    private void createRandomLocalEnemy() {
        Direction direction = Direction.values()[random.nextInt(4)];
        int dX = random.nextInt(CENTER_X * 2) - CENTER_X;
        int dY = random.nextInt(CENTER_Y * 2) - CENTER_Y;
        EnemyModel enemyModel1, enemyModel2;
        if (getGlobeModel() instanceof MonomachiaGlobe){

        }
        switch (direction) {
            case TopRight -> {
                enemyModel1 = new TrigorathModel(globeId, CENTER_X + dX,
                        CENTER_Y - dY, getGlobeModel().getMainPanelModel()
                );
                if (getGlobeModel() instanceof MonomachiaGlobe) {
                    enemyModel1.setTargetEpsilon(getGlobeModel().getEpsilons().get(0));
                    enemyModel2 = new TrigorathModel(globeId, -dX, -dY,
                            getGlobeModel().getMainPanelModel());
                    enemyModel2.setTargetEpsilon(getGlobeModel().getEpsilons().get(1));
                }
            }
            case TopLeft -> {
                enemyModel1 = new TrigorathModel(globeId, -dX, -dY,
                        getGlobeModel().getMainPanelModel());
                if (getGlobeModel() instanceof MonomachiaGlobe) {
                    enemyModel1.setTargetEpsilon(getGlobeModel().getEpsilons().get(0));
                    enemyModel2 = new TrigorathModel(globeId, CENTER_X + dX,
                            CENTER_Y - dY, getGlobeModel().getMainPanelModel()
                    );
                    enemyModel2.setTargetEpsilon(getGlobeModel().getEpsilons().get(1));
                }
            }
            case BottomLeft -> {
                enemyModel1 = new SquarantineModel(globeId, -dX, CENTER_Y + dY,
                        getGlobeModel().getMainPanelModel());
                if (getGlobeModel() instanceof MonomachiaGlobe) {
                    enemyModel1.setTargetEpsilon(getGlobeModel().getEpsilons().get(0));
                    enemyModel2 = new SquarantineModel(globeId, -dX, -dY,
                            getGlobeModel().getMainPanelModel());
                    enemyModel2.setTargetEpsilon(getGlobeModel().getEpsilons().get(1));
                }
            }
            case BottomRight -> {
                enemyModel1 = new TrigorathModel(globeId, CENTER_X + dX,
                        CENTER_Y + dY, getGlobeModel().getMainPanelModel());
                if (getGlobeModel() instanceof MonomachiaGlobe) {
                    enemyModel1.setTargetEpsilon(getGlobeModel().getEpsilons().get(0));
                    enemyModel2 = new TrigorathModel(globeId, CENTER_X + dX,
                            CENTER_Y - dY, getGlobeModel().getMainPanelModel()
                    );
                    enemyModel2.setTargetEpsilon(getGlobeModel().getEpsilons().get(1));
                }
            }
        }

        getGlobeModel().performAction(REQ_PLAY_CREATE_SOUND);
    }

    void spawnMiniBossWave() {
        AtomicInteger spawnedEnemies = new AtomicInteger();
        AtomicInteger wait = new AtomicInteger();
        AtomicInteger count = new AtomicInteger();
        int boundKill = getLevel() * 2 + Config.BOUND;


        getGlobeModel().getWaveFactory().waveTimer = new Timer((int) (WAVE_LOOP * (1 - 0.05 * getLevel())), null);
        getGlobeModel().getWaveFactory().waveTimer.addActionListener(e -> {
            count.getAndIncrement();
            if (getGlobeModel().getKilledEnemiesInWave() < boundKill) {
                createRandomEnemy();
                spawnedEnemies.getAndIncrement();
            } else {
                //wait for remaining ones to be killed
                if (spawnedEnemies.get() <= getGlobeModel().getKilledEnemiesInWave()) {
                    getGlobeModel().performAction(REQ_PLAY_END_WAVE_SOUND);
                    setStartNewWave(false);
                    setBetweenWaves(true);
                    if (wait.get() < 3) {
                        wait.getAndIncrement();
                    } else {
                        setStartNewWave(true);
                        setBetweenWaves(false);
                        if (getLevel() < END_OF_MINIBOSS) {
                            getGlobeModel().resetKilledEnemiesInWave();
                        }
                        stopWaveTimer();
                    }
                } else {
                    System.out.println("Waiting for remaining enemies to be killed.");
                }
            }
            if (count.get() == 5) {
                var mainPanel = getGlobeModel().getMainPanelModel();
                new PortalModel(globeId, mainPanel.getX() + 40,
                        mainPanel.getY() + 40);
                //   setCheckPointOn(true); //todo
            }
        });
        startWaveTimer();

    }

    public void spawnBossWave() {

        welcomeBossScene();
        getGlobeModel().getSmileyHeadModel().appear();
        Timer timer = new Timer(5000, null);
        ActionListener defeatChecker = e -> {
            for (EpsilonModel epsilonModel : getGlobeModel().getEpsilons())
                keepInPanel(epsilonModel, getGlobeModel().getMainPanelModel());
            if (getGlobeModel().getSmileyHeadModel().isDefeated()) {
                getGlobeModel().performAction(REQ_INIT_SCORE_FRAME + REGEX_SPLIT + true);
                timer.stop();
            }
        };
        timer.addActionListener(defeatChecker);
        timer.start();

    }


    private void createRandomEnemy() {
        int chanceOfMinyBoss = getLevel() / 2;

        int randNum = random.nextInt(10);

        int randX;
        int randY;

        randX = 50 + random.nextInt(CENTER_X * 2 - 50);
        randY = 50 + random.nextInt(CENTER_Y * 2 - 50);
        if (randNum > 10 - chanceOfMinyBoss) {
            spawnMiniBoss(randX, randY);
        } else {
            randNum = random.nextInt(5);

            switch (randNum) {
                case 0 -> createRandomLocalEnemy();
                case 1 -> {
                    // var gamePanel = targetEpsilon.getLocalPanelModel();
                    // boolean temp = false;
                    //  if (random.nextInt(2) == 0 || gamePanel == null) {
                    var gamePanel = spawnInternalGamePanel(randX, randY, PanelStatus.shrinkable, true);
                    //      temp = true;
                    // }
                    var omenoctModel = new OmenoctModel(globeId, randX, randY, gamePanel);
                    //if (temp) omenoctModel.setBgPanel((InternalGamePanel) gamePanel);
                }
                case 2 -> {
//                    var gamePanel = targetEpsilon.getLocalPanelModel();
//                    boolean temp = false;
//                    if (random.nextInt(2) == 0 || gamePanel == null) {
//                    var gamePanel = spawnInternalGamePanel(randX, randY, PanelStatus.isometric, true);
//                        temp = true;
//                    }
//                    var archmireModel = new ArchmireModel(globeId, gamePanel, randX, randY);
//                    if (temp) archmireModel.setBgPanel((InternalGamePanel) gamePanel);
                     createRandomLocalEnemy();
                }
                case 3 -> {
                    if (WyrmModel.getCount() < 1) new WyrmModel(globeId, randX, randY);
                    else createRandomLocalEnemy();
                }
                case 4 -> {
                    new NecropickModel(globeId, randX, randY);
                }
            }
        }
    }

    private InternalPanelModel spawnInternalGamePanel(int randX, int randY, PanelStatus panelStatus, boolean flexible) {
        int randWidth = GAME_MIN_SIZE / 2 + random.nextInt(GAME_MIN_SIZE);
        int randHeight = GAME_MIN_SIZE / 2 + random.nextInt(GAME_MIN_SIZE);
        return new InternalPanelModel(globeId, new Rectangle(randX - randWidth / 2, randY - randHeight / 2,
                randWidth, randHeight), panelStatus, flexible);
    }


    private void spawnMiniBoss(int randX, int randY) {
        int randNum = random.nextInt(3);
        if (randNum == 0 && getGlobeModel().getBlackOrbModels().isEmpty()) {
            new BlackOrbModel(globeId, randX, randY);
        } else {
            if (!isEntityThere(randX, randY))
                new BarricadosModel(globeId, randX, randY);
            else createRandomLocalEnemy();
        }
    }

    private boolean isEntityThere(int randLocX, int randLocY) {
        for (int i = 0; i < getGlobeModel().getEntityModels().size(); i++) {
            EntityModel entityModel = getGlobeModel().getEntityModels().get(i);
            if (isOccupied(randLocX, entityModel.getX(), entityModel.getRadius())
                    && isOccupied(randLocY, entityModel.getY(), entityModel.getRadius())) {
                return true;
            }
        }
        return false;
    }


    private enum Direction {
        TopLeft, TopRight, BottomLeft, BottomRight
    }

    public void welcomeBossScene() {
        Timer welcome = new Timer(FPS, null);
        //take epsilon to main panel
        for (EpsilonModel epsilonModel : getGlobeModel().getEpsilons()) {
            getGlobeModel().getEntityModels().remove(epsilonModel);
        }
        cleanOutOtherObjects();
        getGlobeModel().performAction(REQ_NEXT_LEVEL);

        var mainPanel = getGlobeModel().getMainPanelModel();
        for (EpsilonModel epsilonModel : getGlobeModel().getEpsilons()) {
            epsilonModel.setLocalPanelModel(mainPanel);
            keepInPanel(epsilonModel, epsilonModel.getLocalPanelModel());
        }


        ActionListener actionListener = getMoveToCenter(mainPanel, welcome);
        welcome.addActionListener(actionListener);
        welcome.start();
    }

    private ActionListener getMoveToCenter(MainPanelModel mainPanel, Timer welcome) {
        return e -> {
            var rect = mainPanel.getBounds();
            int deltaX = CENTER_X - GAME_MIN_SIZE - rect.x;
            int deltaY = CENTER_Y - rect.y;
            int dX = 6 * getSign(deltaX);
            int dY = 6 * getSign(deltaY);

            if (abs(deltaX) > 10 || abs(deltaY) > 10) {
                if (abs(deltaX) > 8) {
                    mainPanel.setBounds(rect.x + dX, rect.y,
                            rect.width, rect.height);
//                    epsilonModel.move(dX, 0);

                }
                if (abs(deltaY) > 8) {
                    mainPanel.setBounds(rect.x, rect.y + dY,
                            rect.width, rect.height);

//                    epsilonModel.move(0, dY);
                }
                for (EpsilonModel epsilonModel : getGlobeModel().getEpsilons()) {
                    epsilonModel.setLocalPanelModel(mainPanel);
                    keepInPanel(epsilonModel, mainPanel);
                }
            } else {
                welcome.stop();
            }
        };
    }

    private void cleanOutOtherObjects() {
        for (EpsilonModel epsilonModel : getGlobeModel().getEpsilons()) {
            epsilonModel.setLocalPanelModel(getGlobeModel().getMainPanelModel());
        }
        for (int i = 0; i < getGlobeModel().getPanelModels().size(); i++) {
            PanelModel gamePanel = getGlobeModel().getPanelModels().get(i);
            if (gamePanel instanceof InternalPanelModel) {
                getGlobeModel().getGameManager().deleteGamePanel(gamePanel);
            }
        }
    }


}
