package org.windowkillproject.model;

import org.windowkillproject.application.Config;
import org.windowkillproject.model.entities.enemies.SquarantineModel;
import org.windowkillproject.model.entities.enemies.TrigorathModel;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.windowkillproject.application.Application.getGameFrame;
import static org.windowkillproject.application.Config.MAX_ENEMIES;
import static org.windowkillproject.application.Config.WAVE_LOOP;
import static org.windowkillproject.controller.GameController.random;
import static org.windowkillproject.model.entities.enemies.EnemyModel.getEnemiesKilled;
import static org.windowkillproject.model.entities.enemies.EnemyModel.setEnemiesKilled;

public class Wave {
    private static int level;
    public static void spawnWave(){
        level = 0;
        new Wave();
    }

    private static boolean betweenWaves = true;

    public static boolean isBetweenWaves() {
        return betweenWaves;
    }

    public static void setBetweenWaves(boolean betweenWaves) {
        Wave.betweenWaves = betweenWaves;
    }

    private Wave() {
        level++;
        getGameFrame().setWaveLevel(level);
        AtomicInteger count = new AtomicInteger();
        Timer creatorTimer = new Timer((int) (WAVE_LOOP * (1 - 0.2*level)), null);
        creatorTimer.addActionListener(e -> {
            int bound = level * (level + Config.BOUND); //todo still allows too many enemies after one game
            if (count.get() < bound) {
                //doesn't allow too many enemies
                if (count.get() - getEnemiesKilled() <= MAX_ENEMIES) {
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
                    betweenWaves = false;
                    setEnemiesKilled(0);
                    creatorTimer.stop();
                    if (level < 3) new Wave();
                }
            }
        });
        creatorTimer.start();
    }
    private enum Direction{
        TopLeft, TopRight, BottomLeft, BottomRight
    }
}
