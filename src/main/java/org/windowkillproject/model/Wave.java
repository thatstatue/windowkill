package org.windowkillproject.model;

import org.windowkillproject.application.Config;
import org.windowkillproject.model.entities.EpsilonModel;
import org.windowkillproject.model.entities.enemies.*;
import org.windowkillproject.view.entities.enemies.NecropickView;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import static org.windowkillproject.application.Application.getGameFrame;
import static org.windowkillproject.application.Config.*;
import static org.windowkillproject.application.SoundPlayer.playCreateSound;
import static org.windowkillproject.application.SoundPlayer.playEndWaveSound;
import static org.windowkillproject.controller.GameController.random;
import static org.windowkillproject.model.entities.enemies.EnemyModel.getKilledEnemiesInWave;
import static org.windowkillproject.model.entities.enemies.EnemyModel.setKilledEnemiesInWave;

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
        int dX = random.nextInt(Config.GAME_WIDTH);
        int dY = random.nextInt(Config.GAME_HEIGHT);
        switch (direction) {
            case TopRight -> new TrigorathModel(getGameFrame().getMainPanelWidth() + dX,
                    -dY, getGameFrame().getMainGamePanel()); //todo rand?
            case TopLeft -> new SquarantineModel(-dX, -dY, getGameFrame().getMainGamePanel());
            case BottomLeft -> new TrigorathModel(-dX, getGameFrame().getMainPanelHeight() + dY,
                    getGameFrame().getMainGamePanel());
            case BottomRight -> new TrigorathModel(getGameFrame().getMainPanelWidth() + dX,
                    getGameFrame().getMainPanelHeight() + dY, getGameFrame().getMainGamePanel());
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
        int randNum = random.nextInt(4);
        int randX;
        int randY;
        do {
            randX = 50 + random.nextInt(getGameFrame().getMainPanelWidth() - 50);
        } while (isEpsilonThere(randX,EpsilonModel.getINSTANCE().getX()));
        do {
            randY = 50 + random.nextInt(getGameFrame().getMainPanelHeight() - 50);
        } while (isEpsilonThere(randY,EpsilonModel.getINSTANCE().getY()));

        switch (randNum) {
            case 0 -> createRandomLocalEnemy();
            case 1, 2, 3 ->{
                var n =  new WyrmModel(randX, randY);//todo randomize
//                System.out.println("wyrm "+ n.getId() );
//                System.out.println(Arrays.toString(n.getPolygon().xpoints));
//                System.out.println(Arrays.toString(n.getPolygon().ypoints));
            }
        }

    }

    private boolean isEpsilonThere(int randLoc, int epsilonLoc) {
        return randLoc + 2 * EPSILON_RADIUS > epsilonLoc
                && randLoc < epsilonLoc + 4 * EPSILON_RADIUS;
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
