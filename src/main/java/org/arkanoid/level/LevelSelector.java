package org.arkanoid.level;

import com.almasb.fxgl.dsl.FXGL;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getAppHeight;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getAppWidth;

public class LevelSelector {
    public static boolean inSelection = true;
    private static StackPane levelSelectPane;

    public static void showLevelSelect() {
        if (levelSelectPane != null && FXGL.getGameScene().getUINodes().contains(levelSelectPane)) return;

        VBox menuBox = LevelUIFactory.createLevelMenu(LevelManager.getAvailableLevels());
        StackPane rootPane = new StackPane(menuBox);
        rootPane.setPrefSize(getAppWidth(), getAppHeight());
        StackPane.setAlignment(menuBox, Pos.CENTER);

        levelSelectPane = rootPane;
        FXGL.getGameScene().addUINode(levelSelectPane);
    }
}