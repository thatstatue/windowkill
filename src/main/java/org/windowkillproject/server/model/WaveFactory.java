package org.windowkillproject.server.model;

import org.windowkillproject.server.model.globe.GlobeModel;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static org.windowkillproject.server.Config.END_OF_MINIBOSS;
import static org.windowkillproject.server.Config.END_OF_NORMAL;

public class WaveFactory extends Timer{
    private final ArrayList<Wave> waves = new ArrayList<>();
    private int level;
    public  Timer waveTimer;
    private boolean forceStop;

    public boolean isForceStop() {
        return forceStop;
    }

    public void setForceStop(boolean forceStop) {
        this.forceStop = forceStop;
    }

    public int getLevel() {
        return level;
    }

    private final String globeId;
    private boolean betweenWaves, startNewWave = true;

    public boolean isStartNewWave() {
        return startNewWave;
    }

    public boolean isBetweenWaves() {
        return betweenWaves;
    }

    public void setBetweenWaves(boolean betweenWaves) {
        this.betweenWaves = betweenWaves;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setStartNewWave(boolean startNewWave) {
        this.startNewWave = startNewWave;
    }

    public WaveFactory(String globeId) {
        super(500, null);
        this.globeId = globeId;
        ActionListener listener = e -> {
            System.out.println("work");
            if (!betweenWaves && startNewWave) {
                var wave = new Wave(globeId);
                waves.add(wave);
                System.out.println("WELCOME TO WAVE");
                if (level <= END_OF_NORMAL) wave.spawnMiniBossWave();
                else if (level <= END_OF_MINIBOSS) wave.spawnMiniBossWave();
                else wave.spawnBossWave();
            }
        };
        this.addActionListener(listener);
    }

    public ArrayList<Wave> getWaves() {
        return waves;
    }

}
