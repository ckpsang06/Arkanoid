package org.arkanoid.factory;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.dsl.FXGL;
import org.arkanoid.scenes.MainMenu;
import org.arkanoid.scenes.PauseMenu;
import org.arkanoid.utility.MusicHandler;
import org.jetbrains.annotations.NotNull;

public class GameSceneFactory extends SceneFactory {

    @NotNull
    @Override
    public FXGLMenu newMainMenu() {
        MusicHandler.playMenuMusic();
        return new MainMenu(MenuType.MAIN_MENU);
    }

    /**
     * THIS IS THE PAUSE MENU DESPITE BEING NAMED GAME MENU.
     * THIS IS WHAT APPEARS WHEN YOU PRESS ESC.
     * */
    @NotNull
    @Override
    public FXGLMenu newGameMenu() {
        return new PauseMenu(MenuType.GAME_MENU);
    }
}