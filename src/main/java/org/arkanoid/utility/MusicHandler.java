package org.arkanoid.utility;

import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.dsl.FXGL;

public class MusicHandler {

    private static Music tutorialTheme;
    private static Music mainMenuTheme;
    private static Music currentMusic;

    public static void playLevelMusic() {
        ensureLoaded();
        stopAllKnownMusic();

        tutorialTheme.getAudio().setVolume(0.7);
        currentMusic = tutorialTheme;
        FXGL.getAudioPlayer().playMusic(currentMusic);
        System.out.println("Level Music Played");
    }

    public static void playMenuMusic() {
        ensureLoaded();
        stopAllKnownMusic();

        currentMusic = mainMenuTheme;
        FXGL.getAudioPlayer().playMusic(currentMusic);
        System.out.println("Menu Music Played");
    }

    private static void ensureLoaded() {
        if (tutorialTheme == null)
            tutorialTheme = FXGL.getAssetLoader().loadMusic("tutorial_theme.mp3");
        if (mainMenuTheme == null)
            mainMenuTheme = FXGL.getAssetLoader().loadMusic("menu_theme.mp3");
    }

    private static void stopAllKnownMusic() {
        if (tutorialTheme != null)
            FXGL.getAudioPlayer().stopMusic(tutorialTheme);
        if (mainMenuTheme != null)
            FXGL.getAudioPlayer().stopMusic(mainMenuTheme);
        FXGL.getAudioPlayer().stopAllMusic(); // Just in case
    }

    public static void pauseMusic() {
        if (currentMusic != null)
            FXGL.getAudioPlayer().pauseMusic(currentMusic);
    }

    public static void resumeMusic() {
        if (currentMusic != null)
            FXGL.getAudioPlayer().resumeMusic(currentMusic);
    }

    public static void stopMusic() {
        FXGL.getAudioPlayer().stopAllMusic();
    }
}