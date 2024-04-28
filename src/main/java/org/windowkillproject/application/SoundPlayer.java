package org.windowkillproject.application;


import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;


public class SoundPlayer {
    private static SoundPlayer soundPlayer;
    private final AudioInputStream bgThemeAS;

    {
        try {
            bgThemeAS = AudioSystem.getAudioInputStream(new File("BGMusic.wav"));
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
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
        try {
            SoundPlayer.bgTheme.open(bgThemeAS);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        SoundPlayer.bgThemeControl = (FloatControl) bgTheme.getControl(FloatControl.Type.MASTER_GAIN);
        playTheme();
    }

    private static Clip bgTheme;
    private static void playTheme(){
        bgTheme.start();
        System.out.println("hahhaha");
        bgTheme.loop(500);
    }

    private static FloatControl bgThemeControl;
    public static void setSound(String rate){
        bgThemeControl.setValue(-15.0f);
        if (rate.equals("LOW")){
            bgThemeControl.setValue(-30.0f);
        }else if (rate.equals("HIGH")){
            bgThemeControl.setValue(0.0f);
        }else if (!rate.equals("MEDIUM")) {
            System.out.println("unexpected value for sound");
        }
    }

}
