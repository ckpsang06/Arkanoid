package org.arkanoid;

import com.almasb.fxgl.dsl.FXGL;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.arkanoid.utility.MusicHandler;

import static com.almasb.fxgl.dsl.FXGL.getAppWidth;
import static com.almasb.fxgl.dsl.FXGLForKtKt.*;
import static org.arkanoid.GameManager.gameOverOverlay;
import static org.arkanoid.GameManager.gameOverVisible;
import static org.arkanoid.GameManager.isPaused;

public class GameOverManager {
    public static void onGameOver() {
        System.out.println("Game Over!");
        showGameOver();
    }

    public static void showGameOver() {
        if (gameOverVisible) return;
        gameOverVisible = true;

        // Dimmed Background initiation
        Rectangle dimmedBackground = new Rectangle(getAppWidth(), getAppHeight());
        dimmedBackground.setFill(javafx.scene.paint.Color.color(0, 0, 0, 0.6));

        VBox vbox = new VBox(12);
        vbox.setPrefWidth(320);
        vbox.setPrefHeight(220);
        vbox.setStyle("-fx-alignment: center; -fx-padding: 20;");

        // Ảnh gameover (texture)
        var gameOverImage = FXGL.getAssetLoader().loadTexture("gameover.png");
        gameOverImage.setFitWidth(300);
        gameOverImage.setFitHeight(100);

        // Score gotten
        Text finalScore = new Text("Score: " + getWorldProperties().intProperty("score").get());
        finalScore.setStyle("-fx-font-size: 20px; -fx-fill: white;");

        // Restart Button
        Button restartButton = new Button("Restart");
        restartButton.setPrefWidth(120);
        restartButton.setFocusTraversable(false);
        restartButton.setOnAction(e -> {
            hideGameOver();
            FXGL.getGameController().startNewGame();
            MusicHandler.playLevelMusic();
        });

        // Back to Main Menu Button
        Button mainMenuButton = new Button("Exit");
        mainMenuButton.setPrefWidth(120);
        mainMenuButton.setFocusTraversable(false);
        mainMenuButton.setOnAction(e -> {
            hideGameOver();
            FXGL.getGameController().gotoMainMenu();
            MusicHandler.playMenuMusic();
        });

        HBox buttonsContainer = new HBox(10, restartButton, mainMenuButton);
        buttonsContainer.setStyle("-fx-alignment: center;");

        vbox.getChildren().addAll(gameOverImage, finalScore, buttonsContainer);

        FadeTransition fadeDim = new FadeTransition(Duration.seconds(0.6), dimmedBackground);
        fadeDim.setFromValue(0);
        fadeDim.setToValue(0.6);
        fadeDim.setInterpolator(Interpolator.EASE_OUT);
        fadeDim.play();

        gameOverImage.setTranslateY(-getAppHeight()); // Start off-screen

        TranslateTransition slideImg = new TranslateTransition(Duration.seconds(1), gameOverImage);
        slideImg.setToY(0);
        slideImg.setInterpolator(Interpolator.SPLINE(0.2, 0.8, 0.3, 1)); // Bounce-like ease out
        slideImg.play();

        buttonsContainer.setTranslateY(getAppHeight()); // Start off-screen

        TranslateTransition slideBtns = new TranslateTransition(Duration.seconds(1), buttonsContainer);
        slideBtns.setToY(0);
        slideBtns.setInterpolator(Interpolator.SPLINE(0.2, 0.8, 0.3, 1)); // Bounce-like ease out
        slideBtns.play();


        gameOverOverlay = new StackPane(dimmedBackground, vbox);
        StackPane.setAlignment(vbox, Pos.CENTER);

        getGameScene().addUINode(gameOverOverlay);

        //Dừng cập nhật game mà vẫn giữ UI hoạt động
        FXGL.getGameController().pauseEngine();
        FXGL.getInput().setProcessInput(false);

//        GameManager.isPaused = true;
    }

    public static void hideGameOver() {
        if (!gameOverVisible) return;
        gameOverVisible = false;

        if (gameOverOverlay != null) {
            getGameScene().removeUINode(gameOverOverlay);
            gameOverOverlay = null;
        }

        // Tiếp tục game
        FXGL.getGameController().resumeEngine();
        FXGL.getInput().setProcessInput(true);

        isPaused = false;

        // Trả focus lại cho scene để ăn phím
        Platform.runLater(() -> {
            try {
                getGameScene().getRoot().requestFocus();
            } catch (Exception ex) {
                try {
                    FXGL.getPrimaryStage().getScene().getRoot().requestFocus();
                } catch (Exception ignored) {}
            }
        });
    }
}
