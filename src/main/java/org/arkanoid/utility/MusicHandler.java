package org.arkanoid.utility;

import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.logging.FileOutput;

public class MusicHandler {
    private static Music tutorialTheme;
    private static Music mainMenuTheme;
    private static Music forestTheme;
    private static Music spaceTheme;
    private static Music hellTheme;
    private static Music currentMusic;

    /**
     * Play the tutorial level's music.
     * */
    public static void playLevelMusic() {
        ensureLoaded();
        stopAllKnownMusic();

        tutorialTheme.getAudio().setVolume(0.7);
        currentMusic = tutorialTheme;
        FXGL.getAudioPlayer().loopMusic(currentMusic);
        System.out.println("Level Music Played");
    }

    public static void playMenuMusic() {
        ensureLoaded();
        stopAllKnownMusic();

        currentMusic = mainMenuTheme;
        FXGL.getAudioPlayer().loopMusic(currentMusic);
        System.out.println("Menu Music Played");
    }

    public static void playForestMusic() {
        ensureLoaded();
        stopAllKnownMusic();

        currentMusic = forestTheme;
        FXGL.getAudioPlayer().loopMusic(currentMusic);
        System.out.println("Forest Music Played");
    }

    public static void playHellMusic() {
        ensureLoaded();
        stopAllKnownMusic();

        currentMusic = hellTheme;
        FXGL.getAudioPlayer().loopMusic(currentMusic);
        System.out.println("Hell Music Played");
    }

    public static void playSpaceMusic() {
        ensureLoaded();
        stopAllKnownMusic();

        currentMusic = spaceTheme;
        FXGL.getAudioPlayer().loopMusic(currentMusic);
        System.out.println("Space Music Played");
    }

    private static void ensureLoaded() {
        if (tutorialTheme == null)
            tutorialTheme = FXGL.getAssetLoader().loadMusic("tutorial_theme.mp3");
        if (mainMenuTheme == null)
            mainMenuTheme = FXGL.getAssetLoader().loadMusic("menu_theme.mp3");
        if (forestTheme == null)
            forestTheme = FXGL.getAssetLoader().loadMusic("forest_theme.mp3");
        if (spaceTheme == null)
            spaceTheme = FXGL.getAssetLoader().loadMusic("space_theme.mp3");
        if (hellTheme == null) {
            hellTheme = FXGL.getAssetLoader().loadMusic("hell_theme.mp3");
        }
    }

    private static void stopAllKnownMusic() {
        FXGL.getAudioPlayer().stopAllMusic();
    }

    public static void pauseMusic() {
        if (currentMusic != null)
            FXGL.getAudioPlayer().pauseMusic(currentMusic);
    }

    public static void resumeMusic() {
        if (currentMusic != null)
            FXGL.getAudioPlayer().resumeMusic(currentMusic);
    }
}