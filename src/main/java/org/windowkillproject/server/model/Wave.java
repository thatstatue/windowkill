package org.windowkillproject.server.model;

import org.windowkillproject.server.ClientHandler;
import org.windowkillproject.server.Config;
import org.windowkillproject.server.model.abilities.PortalModel;
import org.windowkillproject.server.model.entities.EntityModel;
import org.windowkillproject.server.model.entities.EpsilonModel;
import org.windowkillproject.server.model.entities.enemies.finalboss.SmileyHeadModel;
import org.windowkillproject.server.model.entities.enemies.minibosses.BarricadosModel;
import org.windowkillproject.server.model.entities.enemies.minibosses.BlackOrbModel;
import org.windowkillproject.server.model.entities.enemies.EnemyModel;
import org.windowkillproject.server.model.entities.enemies.normals.*;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Math.abs;
import static org.windowkillproject.Constants.*;
import static org.windowkillproject.Request.*;
import static org.windowkillproject.server.Config.*;

import static org.windowkillproject.controller.GameController.*;
import static org.windowkillproject.controller.Utils.getSign;
import static org.windowkillproject.controller.Utils.isOccupied;

public class Wave {
    public static ArrayList<Wave> waves = new ArrayList<>();
    private final ClientHandler clientHandler;
    private static int level;
    public static Timer waveTimer;

    public static int getLevel() {
        return level;
    }

    private int END_OF_NORMAL = 3;
    private int END_OF_MINIBOSS = 5;



    private void spawnWave() {
        AtomicInteger count = new AtomicInteger();
        waveTimer = new Timer((int) (WAVE_LOOP * (1 - 0.05 * level)), null);
        waveTimer.addActionListener(e -> {
            int bound = level * 2 + Config.BOUND;
            if (count.get() < bound) {
                //doesn't allow too many enemies
                if (count.get() - EnemyModel.getKilledEnemiesInWave() < MAX_ENEMIES) {
                    createRandomLocalEnemy();
                    count.getAndIncrement();
                }
            } else if (count.get() == bound) {
                //waits until u kill these enemies
                if (EnemyModel.getKilledEnemiesInWave() == bound) {
                    clientHandler.sendMessage(REQ_PLAY_END_WAVE_SOUND);
                    betweenWaves = true;
                    count.getAndIncrement();
                }
            } else {
                //10 secs between waves
                if (count.get() < bound + 2) {
                    count.getAndIncrement();
                } else {
                    //time to go to the next level !
                    startNewWave = true;
                    betweenWaves = false;
                    if (level < END_OF_NORMAL) {
                        EnemyModel.setKilledEnemiesInWave(0);
                    } else {
                        clientHandler.sendMessage(REQ_ENDING_SCENE);
                    }
                    waveTimer.stop();
                }
            }
        });
        waveTimer.start();

    }

    private void createRandomLocalEnemy() {
        Direction direction = Direction.values()[random.nextInt(4)];
        int dX = random.nextInt(CENTER_X * 2) - CENTER_X;
        int dY = random.nextInt(CENTER_Y * 2) - CENTER_Y;
        switch (direction) {
            case TopRight -> new TrigorathModel(CENTER_X + dX,
                    CENTER_Y - dY, MainGamePanel.getInstance());
            case TopLeft -> new SquarantineModel(-dX, -dY, MainGamePanel.getInstance());
            case BottomLeft -> new TrigorathModel(-dX, CENTER_Y + dY,

                    MainGamePanel.getInstance());
            case BottomRight -> new TrigorathModel(CENTER_X + dX,
                    CENTER_Y + dY, MainGamePanel.getInstance());
        }
        clientHandler.sendMessage(REQ_PLAY_CREATE_SOUND);
    }

    private void spawnMiniBossWave() {
        AtomicInteger spawnedEnemies = new AtomicInteger();
        AtomicInteger wait = new AtomicInteger();
        AtomicInteger count = new AtomicInteger();
        int boundKill = level * 2 + Config.BOUND;


        waveTimer = new Timer((int) (WAVE_LOOP * (1 - 0.05 * level)), null);
        waveTimer.addActionListener(e -> {
            count.getAndIncrement();
            if (EnemyModel.getKilledEnemiesInWave() < boundKill) {
                createRandomEnemy();
                spawnedEnemies.getAndIncrement();
            } else {
                //wait for remaining ones to be killed
                if (spawnedEnemies.get() <= EnemyModel.getKilledEnemiesInWave()) {
                    clientHandler.sendMessage(REQ_PLAY_END_WAVE_SOUND);
                    startNewWave = false;
                    betweenWaves = true;
                    if(wait.get()<3){
                        wait.getAndIncrement();
                    }else {
                        startNewWave = true;
                        betweenWaves = false;
                        if (level < END_OF_MINIBOSS) {
                            EnemyModel.setKilledEnemiesInWave(0);
                        }
                        waveTimer.stop();
                    }
                } else {
                    System.out.println("Waiting for remaining enemies to be killed.");
                }
            }
            if (count.get() == 5) {
                new PortalModel(MainGamePanel.getInstance().getX()+40,
                        MainGamePanel.getInstance().getY()+40);
                setCheckPointOn(true);
            }
        });
        waveTimer.start();

    }

    private void spawnBossWave() {

        welcomeBossScene();
        SmileyHeadModel.getInstance().appear();
        Timer timer = new Timer(5000, null);
        ActionListener defeatChecker = e -> {
            keepInPanel(MainGamePanel.getInstance());
            if (SmileyHeadModel.isDefeated()) {
                clientHandler.sendMessage(REQ_INIT_SCORE_FRAME+ REGEX_SPLIT + true);
                timer.stop();
            }
        };
        timer.addActionListener(defeatChecker);
        timer.start();

    }

    private static boolean betweenWaves = true, startNewWave;

    public static boolean isStartNewWave() {
        return startNewWave;
    }

    public static boolean isBetweenWaves() {
        return betweenWaves;
    }

    public static void setBetweenWaves(boolean betweenWaves) {
        Wave.betweenWaves = betweenWaves;
    }

    public static void setLevel(int level) {
        Wave.level = level;
    }

    public static void setStartNewWave(boolean startNewWave) {
        Wave.startNewWave = startNewWave;
    }

    private void createRandomEnemy() {
        int chanceOfMinyBoss = level / 2;

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
                    var gamePanel = EpsilonModel.getINSTANCE().getLocalPanelModel();
                    boolean temp = false;
                    if (random.nextInt(2) == 0 || gamePanel == null) {
                        gamePanel = spawnInternalGamePanel(randX, randY, PanelStatus.shrinkable, true);
                        temp = true;
                    }
                    var omenoctModel = new OmenoctModel(randX, randY, gamePanel);
                    if (temp) omenoctModel.setBgPanel((InternalGamePanel) gamePanel);
                }
                case 2 -> {
                    var gamePanel = EpsilonModel.getINSTANCE().getLocalPanelModel();
                    boolean temp = false;
                    if (random.nextInt(2) == 0 || gamePanel == null) {
                        gamePanel = spawnInternalGamePanel(randX, randY, PanelStatus.isometric, true);
                        temp = true;
                    }
                    var archmireModel = new ArchmireModel(gamePanel, randX, randY);
                    if (temp) archmireModel.setBgPanel((InternalGamePanel) gamePanel);

                }
                case 3 -> {
                    if (WyrmModel.getCount() < 1) new WyrmModel(randX, randY);
                    else createRandomLocalEnemy();
                }
                case 4 -> {
                    new NecropickModel(randX, randY);
                }
            }
        }
    }

    private static InternalGamePanel spawnInternalGamePanel(int randX, int randY, PanelStatus panelStatus, boolean flexible) {
        int randWidth = GAME_MIN_SIZE / 2 + random.nextInt(GAME_MIN_SIZE);
        int randHeight = GAME_MIN_SIZE / 2 + random.nextInt(GAME_MIN_SIZE);
        return new InternalGamePanel(randX - randWidth / 2, randY - randHeight / 2,
                randWidth, randHeight, panelStatus, flexible);
    }


    private void spawnMiniBoss(int randX, int randY) {
        int randNum = random.nextInt(3);
        if (randNum == 0 && BlackOrbModel.blackOrbModels.isEmpty()) {
            new BlackOrbModel(randX, randY);
        } else {
            if (!isEntityThere(randX, randY))
                new BarricadosModel(randX, randY);
            else createRandomLocalEnemy();
        }
    }

    private boolean isEntityThere(int randLocX, int randLocY) {
        for (int i = 0; i < EntityModel.entityModels.size(); i++) {
            EntityModel entityModel = EntityModel.entityModels.get(i);
            if (isOccupied(randLocX, entityModel.getX(), entityModel.getRadius())
                    && isOccupied(randLocY, entityModel.getY(), entityModel.getRadius())) {
                return true;
            }
        }
        return false;
    }


    public Wave(ClientHandler clientHandler) {
        startNewWave = false;
        level++;
        waves.add(this);
        this.clientHandler = clientHandler;
        clientHandler.sendMessage(REQ_SET_WAVE_LEVEl+ REGEX_SPLIT + level);
        if (level <= END_OF_NORMAL) spawnWave();
        else if (level <= END_OF_MINIBOSS) spawnMiniBossWave();
        else spawnBossWave();
    }

    private enum Direction {
        TopLeft, TopRight, BottomLeft, BottomRight
    }

    public void welcomeBossScene() {
        Timer welcome = new Timer(FPS, null);
        //take epsilon to main panel
        var epsilon = EpsilonModel.clientEpsilonModelMap.get(clientHandler);
        EntityModel.entityModels.remove(epsilon);

        cleanOutOtherObjects();
        clientHandler.sendMessage(REQ_NEXT_LEVEL);

        var mainPanel = MainGamePanel.getInstance();

        EpsilonModel.getINSTANCE().setLocalPanelModel(mainPanel);
        keepInPanel(EpsilonModel.getINSTANCE().getLocalPanelModel());

        ActionListener actionListener = getMoveToCenter(mainPanel, welcome);
        welcome.addActionListener(actionListener);
        welcome.start();
    }

    private static ActionListener getMoveToCenter(MainGamePanel mainPanel, Timer welcome) {
        return e -> {
            var rect = gamePanelsBounds.get(mainPanel);
            int deltaX = CENTER_X - GAME_MIN_SIZE - rect.x;
            int deltaY = CENTER_Y - rect.y ;
            int dX = 6 * getSign(deltaX);
            int dY = 6 * getSign(deltaY);

            if (abs(deltaX) > 10 || abs(deltaY) > 10) {
                if (abs(deltaX) > 8) {
                    mainPanel.setBounds(rect.x + dX, rect.y,
                            rect.width, rect.height);
                    gamePanelsBounds.get(mainPanel).
                            setBounds(rect.x + dX, rect.y,
                            rect.width, rect.height);
//                    epsilonModel.move(dX, 0);

                }
                if (abs(deltaY) > 8) {
                    mainPanel.setBounds(rect.x , rect.y + dY,
                            rect.width, rect.height);
                    gamePanelsBounds.get(mainPanel).
                            setBounds(rect.x + dX, rect.y + dY,
                                    rect.width, rect.height);
//                    epsilonModel.move(0, dY);
                }
                EpsilonModel.getINSTANCE().setLocalPanel(mainPanel);
                keepInPanel(mainPanel);
            } else {
                welcome.stop();
            }
        };
    }

    private static void cleanOutOtherObjects() {
        int x = EpsilonModel.getINSTANCE().getX();
        int y = EpsilonModel.getINSTANCE().getY();

        var epsilonModel = EpsilonModel.getINSTANCE();
        epsilonModel.setLocalPanel(MainGamePanel.getInstance());
//        epsilonModel.move(x - epsilonModel.getX(), y - epsilonModel.getY());
        for (int i = 0; i < gamePanels.size(); i++) {
            GamePanel gamePanel = gamePanels.get(i);
            if (gamePanel instanceof InternalGamePanel) {
                deleteGamePanel(gamePanel);
            }
        }
    }


}
