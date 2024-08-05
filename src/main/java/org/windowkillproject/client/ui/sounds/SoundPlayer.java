package org.windowkillproject.client.ui.sounds;


import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;


public class SoundPlayer {
    private final AudioInputStream bgThemeAS, hitAS, createAS, destroyAS, waveAS, bulletAS;
    private final Clip bgTheme, hit, create, destroy, wave, bullet;

    {
        try {
            String root = "src/main/java/org/windowkillproject/client/ui/sounds/";
            bgThemeAS = AudioSystem.getAudioInputStream(new File(root +"BGTheme.wav"));
            hitAS = AudioSystem.getAudioInputStream(new File(root +"hit.wav"));
            createAS = AudioSystem.getAudioInputStream(new File(root +"create.wav"));
            destroyAS = AudioSystem.getAudioInputStream(new File(root +"destroy.wav"));
            waveAS = AudioSystem.getAudioInputStream(new File(root +"wave.wav"));
            bulletAS = AudioSystem.getAudioInputStream(new File(root +"bullet.wav"));

        } catch (UnsupportedAudioFileException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SoundPlayer() {
        try {
            this.bgTheme = AudioSystem.getClip();
            this.hit = AudioSystem.getClip();
            this.create = AudioSystem.getClip();
            this.destroy = AudioSystem.getClip();
            this.wave = AudioSystem.getClip();
            this.bullet = AudioSystem.getClip();

        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
        try {
            this.bgTheme.open(bgThemeAS);
            this.hit.open(hitAS);
            this.create.open(createAS);
            this.destroy.open(destroyAS);
            this.wave.open(waveAS);
            this.bullet.open(bulletAS);

        } catch (LineUnavailableException | IOException e) {
            throw new RuntimeException(e);
        }
        this.bgThemeControl = (FloatControl) bgTheme.getControl(FloatControl.Type.MASTER_GAIN);
        playThemeSong();
    }


    private void playThemeSong() {
        bgTheme.start();
        bgTheme.loop(500);
    }

    public void playHitSound() {
        hit.stop();
        hit.setMicrosecondPosition(0);
        hit.start();
    }

    public void playCreateSound() {
        create.stop();
        create.setMicrosecondPosition(0);
        create.start();
    }

    public void playEndWaveSound() {
        wave.stop();
        wave.setMicrosecondPosition(0);
        wave.start();
    }

    public void playDestroySound() {
        destroy.stop();
        destroy.setMicrosecondPosition(0);
        destroy.start();
    }

    public void playBulletSound() {
        bullet.stop();
        bullet.setMicrosecondPosition(0);
        bullet.start();
    }

    private FloatControl bgThemeControl;

    public void setSoundVolume(String rate) {
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
