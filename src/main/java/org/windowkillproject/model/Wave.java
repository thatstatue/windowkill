package org.windowkillproject.model;

import org.windowkillproject.application.Config;
import org.windowkillproject.application.panels.game.InternalGamePanel;
import org.windowkillproject.application.panels.game.PanelStatus;
import org.windowkillproject.model.entities.EntityModel;
import org.windowkillproject.model.entities.EpsilonModel;
import org.windowkillproject.model.entities.enemies.*;
import org.windowkillproject.model.entities.enemies.minibosses.BarricadosModel;
import org.windowkillproject.model.entities.enemies.minibosses.BlackOrbModel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static org.windowkillproject.application.Application.getGameFrame;
import static org.windowkillproject.application.Config.*;
import static org.windowkillproject.application.SoundPlayer.playCreateSound;
import static org.windowkillproject.application.SoundPlayer.playEndWaveSound;
import static org.windowkillproject.controller.GameController.random;
import static org.windowkillproject.controller.Utils.isOccupied;
import static org.windowkillproject.model.entities.EntityModel.entityModels;
import static org.windowkillproject.model.entities.enemies.EnemyModel.getKilledEnemiesInWave;
import static org.windowkillproject.model.entities.enemies.EnemyModel.setKilledEnemiesInWave;
import static org.windowkillproject.model.entities.enemies.minibosses.BlackOrbModel.blackOrbModels;

public class Wave {
    public static ArrayList<Wave> waves = new ArrayList<>();
    private static int level;

    private void spawnWave() {
        getGameFrame().setWaveLevel(level);
        AtomicInteger count = new AtomicInteger();
        Timer creatorTimer = new Timer((int) (WAVE_LOOP * (1 - 0.05 * level)), null);
        creatorTimer.addActionListener(e -> {
            int bound = level * 2 + Config.BOUND;
            if (count.get() < bound) {
                //doesn't allow too many enemies
                if (count.get() - getKilledEnemiesInWave() < MAX_ENEMIES) {
                    createRandomEnemy();/*todo Local*/
                    count.getAndIncrement();
                }
            } else if (count.get() == bound) {
                //waits until u kill the remaining enemies
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
                    if (level < 3) {
                        startNewWave = true;
                        betweenWaves = false;
                        setKilledEnemiesInWave(0);
                        creatorTimer.stop();
                    } else {
                        getGameFrame().endingScene();
                        //waves.remove(this); //todo why?
                        creatorTimer.stop();
                    }
                }
            }
        });
        creatorTimer.start();

    }

    private void createRandomLocalEnemy() {
        Direction direction = Direction.values()[random.nextInt(4)];
        int dX = random.nextInt(CENTER_X*2) - CENTER_X;
        int dY = random.nextInt(CENTER_Y*2) - CENTER_Y;
        switch (direction) {
            case TopRight -> new TrigorathModel( CENTER_X + dX,
                    CENTER_Y-dY, getGameFrame().getMainGamePanel()); //todo rand?
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
                    //time to go to the next level !
                    if (level < 10) {
                        startNewWave = true;
                        betweenWaves = false;
                        setKilledEnemiesInWave(0);
                        creatorTimer.stop();
                    } else {
                        getGameFrame().endingScene();
                        //waves.remove(this); //todo final ending scene to be implemented
                        creatorTimer.stop();
                    }
                }
            }
        });
        creatorTimer.start();

    }

    private void spawnBossWave() {

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
        int chanceOfMinyBoss = level/2;

        int randNum = random.nextInt(10);

        int randX;
        int randY;

        randX = 50 + random.nextInt(CENTER_X*2 - 50);
        randY = 50 + random.nextInt(CENTER_Y*2 - 50);
        if (randNum > 10-chanceOfMinyBoss) {
            spawnMiniBoss(randX, randY);
        }else {
            randNum = random.nextInt(5);

            switch (randNum) {
                case 0 -> createRandomLocalEnemy();
                case 1 -> {
                    var gamePanel = EpsilonModel.getINSTANCE().getLocalPanel();
                    if (random.nextInt(2) == 0 || gamePanel == null)
                        gamePanel = spawnInternalGamePanel(randX, randY, PanelStatus.shrinkable, true);
                    new OmenoctModel(randX, randY, gamePanel);
                }
                case 2 -> {
                    var gamePanel = EpsilonModel.getINSTANCE().getLocalPanel();
                    if (random.nextInt(2) == 0 || gamePanel == null)
                        gamePanel = spawnInternalGamePanel(randX, randY, PanelStatus.shrinkable, true);
                    new ArchmireModel(gamePanel, randX, randY);
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
    private void spawnMiniBoss(int randX, int randY){
        int randNum = random.nextInt(3);
        if (randNum == 0 && blackOrbModels.isEmpty()) {
            new BlackOrbModel(randX, randY);
        }
        else {
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
        if (level < 4) spawnWave();
        else if (level < 9) spawnMiniBossWave();
        else spawnBossWave();
    }

    private enum Direction {
        TopLeft, TopRight, BottomLeft, BottomRight
    }
}
