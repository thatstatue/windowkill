package org.windowkillproject.application;


import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;


public class SoundPlayer {
    private static SoundPlayer soundPlayer;
    private final AudioInputStream bgThemeAS, hitAS, createAS, destroyAS, waveAS, bulletAS;
    private static Clip bgTheme, hit, create, destroy, wave, bullet;

    {
        try {
            bgThemeAS = AudioSystem.getAudioInputStream(new File("BGTheme.wav"));
            hitAS = AudioSystem.getAudioInputStream(new File("hit.wav"));
            createAS = AudioSystem.getAudioInputStream(new File("create.wav"));
            destroyAS = AudioSystem.getAudioInputStream(new File("destroy.wav"));
            waveAS = AudioSystem.getAudioInputStream(new File("wave.wav"));
            bulletAS = AudioSystem.getAudioInputStream(new File("bullet.wav"));

        } catch (UnsupportedAudioFileException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static SoundPlayer getSoundPlayer() {
        if (soundPlayer == null) soundPlayer = new SoundPlayer();
        return soundPlayer;
    }

    private SoundPlayer() {
        try {
            SoundPlayer.bgTheme = AudioSystem.getClip();
            SoundPlayer.hit = AudioSystem.getClip();
            SoundPlayer.create = AudioSystem.getClip();
            SoundPlayer.destroy = AudioSystem.getClip();
            SoundPlayer.wave = AudioSystem.getClip();
            SoundPlayer.bullet = AudioSystem.getClip();

        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
        try {
            SoundPlayer.bgTheme.open(bgThemeAS);
            SoundPlayer.hit.open(hitAS);
            SoundPlayer.create.open(createAS);
            SoundPlayer.destroy.open(destroyAS);
            SoundPlayer.wave.open(waveAS);
            SoundPlayer.bullet.open(bulletAS);

        } catch (LineUnavailableException | IOException e) {
            throw new RuntimeException(e);
        }
        SoundPlayer.bgThemeControl = (FloatControl) bgTheme.getControl(FloatControl.Type.MASTER_GAIN);
        playTheme();
    }


    private static void playTheme() {
        bgTheme.start();
        bgTheme.loop(500);
    }

    public static void playHit() {
        hit.stop();
        hit.setMicrosecondPosition(0);
        hit.start();
    }

    public static void playCreate() {
        create.stop();
        create.setMicrosecondPosition(0);
        create.start();
    }

    public static void playEndWave() {
        wave.stop();
        wave.setMicrosecondPosition(0);
        wave.start();
    }

    public static void playDestroy() {
        destroy.stop();
        destroy.setMicrosecondPosition(0);
        destroy.start();
    }

    public static void playBullet() {
        bullet.stop();
        bullet.setMicrosecondPosition(0);
        bullet.start();
    }

    private static FloatControl bgThemeControl;

    public static void setSound(String rate) {
        bgThemeControl.setValue(-25.0f);
        if (rate.equals("LOW")) {
            bgThemeControl.setValue(-40.0f);
        } else if (rate.equals("HIGH")) {
            bgThemeControl.setValue(-10.0f);
        } else if (!rate.equals("MEDIUM")) {
            System.out.println("unexpected value for sound");
        }
    }

}
