package org.windowkillproject.model;

import org.windowkillproject.application.Config;
import org.windowkillproject.controller.GameController;
import org.windowkillproject.model.entities.enemies.EnemyModel;
import org.windowkillproject.model.entities.enemies.SquarantineModel;
import org.windowkillproject.model.entities.enemies.TrigorathModel;
import org.windowkillproject.view.entities.enemies.SquarantineView;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.windowkillproject.application.Application.gameFrame;
import static org.windowkillproject.application.Config.LOOP;
import static org.windowkillproject.controller.GameController.random;
import static org.windowkillproject.model.entities.enemies.EnemyModel.getEnemiesKilled;
import static org.windowkillproject.model.entities.enemies.EnemyModel.setEnemiesKilled;

public class Wave {
    private static int level;

    private static boolean betweenWaves = true;

    public static boolean isBetweenWaves() {
        return betweenWaves;
    }

    public static void setBetweenWaves(boolean betweenWaves) {
        Wave.betweenWaves = betweenWaves;
    }

    public Wave() {
        level++;
        gameFrame.setWaveLevel(level);
        AtomicInteger count = new AtomicInteger();
        Timer creatorTimer = new Timer((int) (LOOP * (1 - 0.2*level)), null);
        creatorTimer.addActionListener(e -> {
            int bound = level * (level + 5); //todo unhard
            if (count.get() < bound) {
                //doesn't allow too many enemies
                if (count.get() - getEnemiesKilled() < 6) {
                    Direction direction = Direction.values()[random.nextInt(4)];
                    int dX = random.nextInt(Config.GAME_WIDTH);
                    int dY = random.nextInt(Config.GAME_HEIGHT);
                    switch (direction) {
                        case TopRight -> new TrigorathModel(gameFrame.getWidth() + dX, -dY);
                        case TopLeft -> new SquarantineModel(-dX, -dY);
                        case BottomLeft -> new TrigorathModel(-dX, gameFrame.getHeight() + dY);
                        case BottomRight -> new TrigorathModel(gameFrame.getWidth() + dX, gameFrame.getHeight() + dY);
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
        //todo: add square
    }
    private enum Direction{
        TopLeft, TopRight, BottomLeft, BottomRight
    }
}
