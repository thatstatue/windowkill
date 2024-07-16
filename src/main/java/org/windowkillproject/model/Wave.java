package org.windowkillproject.model;

import org.windowkillproject.application.Application;
import org.windowkillproject.application.Config;
import org.windowkillproject.application.panels.game.GamePanel;
import org.windowkillproject.application.panels.game.InternalGamePanel;
import org.windowkillproject.application.panels.game.MainGamePanel;
import org.windowkillproject.application.panels.game.PanelStatus;
import org.windowkillproject.model.entities.EntityModel;
import org.windowkillproject.model.entities.EpsilonModel;
import org.windowkillproject.model.entities.enemies.EnemyModel;
import org.windowkillproject.model.entities.enemies.finalboss.SmileyHeadModel;
import org.windowkillproject.model.entities.enemies.minibosses.BarricadosModel;
import org.windowkillproject.model.entities.enemies.minibosses.BlackOrbModel;
import org.windowkillproject.model.entities.enemies.normals.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Math.E;
import static java.lang.Math.abs;
import static org.windowkillproject.application.Application.*;
import static org.windowkillproject.application.Config.*;
import static org.windowkillproject.application.SoundPlayer.playCreateSound;
import static org.windowkillproject.application.SoundPlayer.playEndWaveSound;
import static org.windowkillproject.application.panels.game.GamePanel.gamePanels;
import static org.windowkillproject.application.panels.game.GamePanel.gamePanelsBounds;
import static org.windowkillproject.controller.Controller.deleteGamePanel;
import static org.windowkillproject.controller.GameController.keepInPanel;
import static org.windowkillproject.controller.GameController.random;
import static org.windowkillproject.controller.Utils.getSign;
import static org.windowkillproject.controller.Utils.isOccupied;
import static org.windowkillproject.model.entities.EntityModel.entityModels;
import static org.windowkillproject.model.entities.enemies.EnemyModel.getKilledEnemiesInWave;
import static org.windowkillproject.model.entities.enemies.EnemyModel.setKilledEnemiesInWave;
import static org.windowkillproject.model.entities.enemies.minibosses.BlackOrbModel.blackOrbModels;

public class Wave {
    public static ArrayList<Wave> waves = new ArrayList<>();
    private static int level;

    public static int getLevel() {
        return level;
    }

    private static boolean welcome;
    private int END_OF_NORMAL = 1;
    private int END_OF_MINIBOSS = 2;

    public static boolean isWelcome() {
        return welcome;
    }

    private void spawnWave() {
        getGameFrame().setWaveLevel(level);
        AtomicInteger count = new AtomicInteger();
        Timer creatorTimer = new Timer((int) (WAVE_LOOP * (1 - 0.05 * level)), null);
        creatorTimer.addActionListener(e -> {
            int bound = 2; //level * 2 + Config.BOUND; todo uncomment
            if (count.get() < bound) {
                //doesn't allow too many enemies
                if (count.get() - getKilledEnemiesInWave() < MAX_ENEMIES) {
                    createRandomLocalEnemy();
                    count.getAndIncrement();
                }
            } else if (count.get() == bound) {
                //waits until u kill these enemies
                if (getKilledEnemiesInWave() == bound) {
                    playEndWaveSound();
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
                        setKilledEnemiesInWave(0);
                    } else {
                        getGameFrame().endingScene();

                        //waves.remove(this); //todo why?

                    }
                    creatorTimer.stop();
                }
            }
        });
        creatorTimer.start();

    }

    private void createRandomLocalEnemy() {
        Direction direction = Direction.values()[random.nextInt(4)];
        int dX = random.nextInt(CENTER_X * 2) - CENTER_X;
        int dY = random.nextInt(CENTER_Y * 2) - CENTER_Y;
        switch (direction) {
            case TopRight -> new TrigorathModel(CENTER_X + dX,
                    CENTER_Y - dY, getGameFrame().getMainGamePanel()); //todo rand?
            case TopLeft -> new SquarantineModel(-dX, -dY, getGameFrame().getMainGamePanel());
            case BottomLeft -> new TrigorathModel(-dX, CENTER_Y + dY,
                    getGameFrame().getMainGamePanel());
            case BottomRight -> new TrigorathModel(CENTER_X + dX,
                    CENTER_Y + dY, getGameFrame().getMainGamePanel());
        }
        playCreateSound();
    }

    private void spawnMiniBossWave() {
        getGameFrame().setWaveLevel(level);
        AtomicInteger count = new AtomicInteger();
        Timer creatorTimer = new Timer((int) (WAVE_LOOP * (1 - 0.05 * level)), null);
        creatorTimer.addActionListener(e -> {
            int boundKill = level * 2 + Config.BOUND;
            if (getKilledEnemiesInWave() < boundKill) {
                createRandomEnemy();
                count.getAndIncrement();
            }
            //waits until u kill the remaining enemies
            else if (count.get() == getKilledEnemiesInWave()) {
                playEndWaveSound();
                betweenWaves = true;
                count.getAndIncrement();
            } else {
                //10 secs between waves
                if (count.get() < getKilledEnemiesInWave() + 2) {
                    count.getAndIncrement();
                } else {
                    //time to go to the next level
                    startNewWave = true;
                    betweenWaves = false;
                    if (level < END_OF_MINIBOSS) {
                        setKilledEnemiesInWave(0);
                    } else {
                        welcomeBossScene();
                        //waves.remove(this);

                    }
                    creatorTimer.stop();
                }
            }
        });
        creatorTimer.start();

    }

    private void spawnBossWave() {
        SmileyHeadModel.getInstance().appear();
        Timer timer = new Timer(10, null);
        ActionListener defeatChecker = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (SmileyHeadModel.isDefeated()) {
                    initScoreFrame(true);
                    timer.stop();
                }
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
                    var gamePanel = EpsilonModel.getINSTANCE().getLocalPanel();
                    boolean temp = false;
                    if (random.nextInt(2) == 0 || gamePanel == null) {
                        gamePanel = spawnInternalGamePanel(randX, randY, PanelStatus.shrinkable, true);
                        temp = true;
                    }
                    var omenoctModel =new OmenoctModel(randX, randY, gamePanel);
                    if (temp) omenoctModel.setBgPanel((InternalGamePanel) gamePanel);
                }
                case 2 -> {
                    var gamePanel = EpsilonModel.getINSTANCE().getLocalPanel();
                    boolean temp = false;
                    if (random.nextInt(2) == 0 || gamePanel == null) {
                        gamePanel = spawnInternalGamePanel(randX, randY, PanelStatus.shrinkable, true);
                        temp = true;
                    }
                    var archmireModel = new ArchmireModel(gamePanel, randX, randY);
                    if (temp) archmireModel.setBgPanel((InternalGamePanel) gamePanel);

                }
                case 3 -> {
                    new WyrmModel(randX, randY);
                }
                case 4 -> {
//                    new NecropickModel(randX, randY);//todo debug
                    createRandomLocalEnemy();
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


        //todo clear the frames with no entities except the main
    private void spawnMiniBoss(int randX, int randY) {
        int randNum = random.nextInt(3);
        if (randNum == 0 && blackOrbModels.isEmpty()) {
            new BlackOrbModel(randX, randY);
        } else {
            if (!isEntityThere(randX, randY))
                new BarricadosModel(randX, randY);
            else createRandomLocalEnemy();
        }
    }

    private boolean isEntityThere(int randLocX, int randLocY) {
        for (int i = 0; i < entityModels.size(); i++) {
            EntityModel entityModel = entityModels.get(i);
            if (isOccupied(randLocX, entityModel.getX(), entityModel.getRadius())
                    && isOccupied(randLocY, entityModel.getY(), entityModel.getRadius())) {
                return true;
            }
        }
        return false;
    }


    public Wave() {
        startNewWave = false;
        level++;
        waves.add(this);
        if (level <= END_OF_NORMAL) spawnWave();
        else if (level <= END_OF_MINIBOSS) spawnMiniBossWave();
        else spawnBossWave();
    }

    private enum Direction {
        TopLeft, TopRight, BottomLeft, BottomRight
    }

    public void welcomeBossScene() {
        welcome = true;
        Timer welcome = new Timer(FPS, null);
        //take epsilon to main panel
        cleanOutOtherObjects();

        var mainPanel = getGameFrame().getMainGamePanel();

        EpsilonModel.getINSTANCE().setLocalPanel(mainPanel);
        keepInPanel(EpsilonModel.getINSTANCE().getLocalPanel());


        ActionListener actionListener = getMoveToCenter(mainPanel, welcome);
        welcome.addActionListener(actionListener);
        welcome.start();
    }

    private static ActionListener getMoveToCenter(MainGamePanel mainPanel, Timer welcome) {
        EpsilonModel epsilonModel = EpsilonModel.getINSTANCE();
        return e -> {
            int deltaX = -EpsilonModel.getINSTANCE().getX() + CENTER_X - EPSILON_RADIUS;
            int deltaY = -EpsilonModel.getINSTANCE().getY() + CENTER_Y - EPSILON_RADIUS;
            int dX = 6 * getSign(deltaX); int dY = 6 * getSign(deltaY);
            if (abs(deltaX) > 5 || abs(deltaY) > 5) {
                if (abs(deltaX) > 8) {
                    mainPanel.setBounds(mainPanel.getX() + dX, mainPanel.getY(),
                            mainPanel.getWidth(), mainPanel.getHeight());
                    gamePanelsBounds.get(mainPanel).setBounds(
                            mainPanel.getX() + dX,
                            mainPanel.getY(),
                            mainPanel.getWidth(),
                            mainPanel.getHeight());
                    epsilonModel.move(dX, 0);

                }
                if (abs(deltaY) > 8) {

                    mainPanel.setBounds(mainPanel.getX(), dY + mainPanel.getY(),
                            mainPanel.getWidth(), mainPanel.getHeight());
                    gamePanelsBounds.get(mainPanel).setBounds(
                            mainPanel.getX(),
                            mainPanel.getY() + dY,
                            mainPanel.getWidth(),
                            mainPanel.getHeight());
                    epsilonModel.move(0, dY);
                }
            } else {

                Wave.welcome = false;
                welcome.stop();
            }
        };
    }

    private static void cleanOutOtherObjects() { //todo
        int x = EpsilonModel.getINSTANCE().getX();
        int y = EpsilonModel.getINSTANCE().getY();

        var epsilonModel = EpsilonModel.getINSTANCE();
        epsilonModel.setLocalPanel(getGameFrame().getMainGamePanel());
        epsilonModel.move(x - epsilonModel.getX(), y - epsilonModel.getY());
        for (int i = 0; i < gamePanels.size(); i++) {
            GamePanel gamePanel = gamePanels.get(i);
            if (gamePanel instanceof InternalGamePanel) {
                deleteGamePanel(gamePanel);
            }
        }
        nextLevel();
    }



}
