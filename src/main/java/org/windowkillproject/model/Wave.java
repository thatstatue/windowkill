package org.windowkillproject.model;

import org.windowkillproject.application.Config;
import org.windowkillproject.model.entities.EpsilonModel;
import org.windowkillproject.model.entities.enemies.SquarantineModel;
import org.windowkillproject.model.entities.enemies.TrigorathModel;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static org.windowkillproject.application.Application.getGameFrame;
import static org.windowkillproject.application.Application.initPFrame;
import static org.windowkillproject.application.Config.*;
import static org.windowkillproject.controller.GameController.random;
import static org.windowkillproject.model.entities.enemies.EnemyModel.getEnemiesKilled;
import static org.windowkillproject.model.entities.enemies.EnemyModel.setEnemiesKilled;

public class Wave {
    public static ArrayList<Wave> waves = new ArrayList<>();
    private static int level;
    private void spawnWave(){

        System.out.println(level + " now");
        getGameFrame().setWaveLevel(level);
        AtomicInteger count = new AtomicInteger();
        Timer creatorTimer = new Timer((int) (WAVE_LOOP * (1 - 0.05*level)), null);
        creatorTimer.addActionListener(e -> {
            int bound = level *2 + Config.BOUND; //todo still allows too many enemies after one game
            if (count.get() < bound) {
                //doesn't allow too many enemies
                if (count.get() - getEnemiesKilled() < MAX_ENEMIES) {
                    System.out.println("count is" + (count.get()) + " killed " + getEnemiesKilled());
                    System.out.println();
                    Direction direction = Direction.values()[random.nextInt(4)];
                    int dX = random.nextInt(Config.GAME_WIDTH);
                    int dY = random.nextInt(Config.GAME_HEIGHT);
                    switch (direction) {
                        case TopRight -> new TrigorathModel(getGameFrame().getWidth() + dX, -dY);
                        case TopLeft -> new SquarantineModel(-dX, -dY);
                        case BottomLeft -> new TrigorathModel(-dX, getGameFrame().getHeight() + dY);
                        case BottomRight -> new TrigorathModel(getGameFrame().getWidth() + dX, getGameFrame().getHeight() + dY);
                    }
                    count.getAndIncrement();
                }
            } else if (count.get()==bound) {
                if (getEnemiesKilled() == bound){
                    //todo play sound
                    betweenWaves = true;
                    count.getAndIncrement();
                }
            }else {
                if (count.get()< bound+2){
                    count.getAndIncrement();
                }else {
                    if (level < 3) {
                        startNewWave = true;
                        betweenWaves = false;
                        setEnemiesKilled(0);
                        creatorTimer.stop();
                    } else {
                        getGameFrame().endingScene();
                        waves.remove(this);
                        creatorTimer.stop();
                    }
                }
            }
        });
        creatorTimer.start();

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

    public Wave() {
        startNewWave = false;
        level++;
        waves.add(this);
        spawnWave();
    }
    private enum Direction{
        TopLeft, TopRight, BottomLeft, BottomRight
    }
}
