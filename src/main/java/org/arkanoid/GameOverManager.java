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
import org.arkanoid.level.LevelSelector;
import org.arkanoid.utility.MusicHandler;

import static com.almasb.fxgl.dsl.FXGL.getAppWidth;
import static com.almasb.fxgl.dsl.FXGLForKtKt.*;
import static org.arkanoid.GameManager.*;

public class GameOverManager {

    public static void onGameOver() {
        System.out.println("Game Over!");
        LevelSelector.inSelection = true;
        showGameOver();
    }

    public static void showGameOver() {
        if (gameOverVisible) return;
        gameOverVisible = true;

        Rectangle dimmedBackground = createDimmedBackground();
        VBox vbox = createGameOverVBox();
        var gameOverImage = createGameOverImage();
        HBox buttonsContainer = createButtonsContainer();

        vbox.getChildren().addAll(createScoreText(), buttonsContainer);

        animateFadeIn(dimmedBackground);
        animateSlideIn(gameOverImage, -getAppHeight());
        animateSlideIn(buttonsContainer, getAppHeight());

//        gameOverOverlay = new StackPane(dimmedBackground, vbox);
        vbox.setLayoutX(475);
        vbox.setLayoutY(getAppHeight() - 300);
        gameOverImage.setX(getAppWidth() / 2 - gameOverImage.getFitWidth() / 2);
        gameOverImage.setY(-300);
        getGameScene().addUINodes(dimmedBackground, vbox, gameOverImage);

        pauseGame();
    }

    public static void hideGameOver() {
        if (!gameOverVisible) return;
        gameOverVisible = false;

        if (gameOverOverlay != null) {
            getGameScene().removeUINode(gameOverOverlay);
            gameOverOverlay = null;
        }

        resumeGame();
        refocusGameScene();
    }

    private static Rectangle createDimmedBackground() {
        Rectangle rect = new Rectangle(getAppWidth(), getAppHeight());
        rect.setFill(javafx.scene.paint.Color.color(0, 0, 0));
        return rect;
    }

    private static VBox createGameOverVBox() {
        VBox vbox = new VBox(12);
        vbox.setPrefWidth(320);
        vbox.setPrefHeight(220);
        vbox.setStyle("-fx-alignment: center; -fx-padding: 20;");
        return vbox;
    }

    private static Text createScoreText() {
        Text score = new Text("Score: " + getWorldProperties().intProperty("score").get());
        score.setStyle("-fx-font-size: 30px; -fx-fill: white;");
        return score;
    }

    private static HBox createButtonsContainer() {
        Button restartButton = new Button("Restart");
        restartButton.setPrefWidth(150);
        restartButton.setFocusTraversable(false);
        restartButton.setOnAction(e -> {
            hideGameOver();
            FXGL.getGameController().startNewGame();
            MusicHandler.playLevelMusic();
        });

        Button mainMenuButton = new Button("Exit");
        mainMenuButton.setPrefWidth(150);
        mainMenuButton.setFocusTraversable(false);
        mainMenuButton.setOnAction(e -> {
            hideGameOver();
            FXGL.getGameController().gotoMainMenu();
            MusicHandler.playMenuMusic();
        });

        HBox hbox = new HBox(10, restartButton, mainMenuButton);
        hbox.setStyle("-fx-alignment: center;");
        return hbox;
    }

    private static javafx.scene.image.ImageView createGameOverImage() {
        var image = FXGL.getAssetLoader().loadTexture("gameover.png");
        image.setFitWidth(700);
        image.setPreserveRatio(true);
        return image;
    }

    private static void animateFadeIn(Rectangle node) {
        FadeTransition fade = new FadeTransition(Duration.seconds(0.6), node);
        fade.setFromValue(0);
        fade.setToValue(0.6);
        fade.setInterpolator(Interpolator.EASE_OUT);
        fade.play();
    }

    private static void animateSlideIn(javafx.scene.Node node, double fromY) {
        node.setTranslateY(fromY);
        TranslateTransition slide = new TranslateTransition(Duration.seconds(1), node);
        slide.setToY(0);
        slide.setInterpolator(Interpolator.SPLINE(0.2, 0.8, 0.3, 1));
        slide.play();
    }

    private static void pauseGame() {
        FXGL.getGameController().pauseEngine();
        FXGL.getInput().setProcessInput(false);
    }

    private static void resumeGame() {
        FXGL.getGameController().resumeEngine();
        FXGL.getInput().setProcessInput(true);
        isPaused = false;
    }

    private static void refocusGameScene() {
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