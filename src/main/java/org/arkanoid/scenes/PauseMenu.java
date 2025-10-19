package org.arkanoid.scenes;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import org.arkanoid.utility.MusicHandler;

import static com.almasb.fxgl.dsl.FXGL.getAppWidth;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getAppHeight;

public class PauseMenu extends FXGLMenu {
    public static boolean isPausing = false;

    public PauseMenu(MenuType type) {
        super(type);

        Rectangle dimmedBackground = new Rectangle(FXGL.getAppWidth(), FXGL.getAppHeight());
        dimmedBackground.setFill(javafx.scene.paint.Color.color(0, 0, 0, 0.6));

        // Toggle music pause/resume
        if (isPausing) {
            System.out.println("Pressed Pause");
            MusicHandler.resumeMusic();
            isPausing = false;
        } else {
            System.out.println("Pressed Pause");
            MusicHandler.pauseMusic();
            isPausing = true;
        }

        // Create button box
        VBox box = new VBox(15);
        box.setAlignment(Pos.CENTER);

        Button resumeBtn = FXGL.getUIFactoryService().newButton("Resume");
        resumeBtn.setOnAction(e -> fireResume());

        Button mainMenuBtn = FXGL.getUIFactoryService().newButton("Main Menu");
        mainMenuBtn.setOnAction(e -> fireExitToMainMenu());

        box.getChildren().addAll(resumeBtn, mainMenuBtn);

        // Wrap in StackPane to center on screen
        StackPane layout = new StackPane(dimmedBackground, box);
        StackPane.setAlignment(box, Pos.CENTER);

        getContentRoot().getChildren().add(layout);
    }
}
