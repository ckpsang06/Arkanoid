package org.arkanoid.scenes;

import com.almasb.fxgl.dsl.FXGL;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.arkanoid.level.LevelSelector;
import org.arkanoid.utility.MusicHandler;

import static com.almasb.fxgl.dsl.FXGL.getAppWidth;
import static com.almasb.fxgl.dsl.FXGLForKtKt.*;
import static org.arkanoid.GameManager.*;

public class GameWinManager {
    public static void onGameWin() {
        System.out.println("Game Won!");
        LevelSelector.inSelection = true;
        FXGL.play("win.wav");
        showgameWin();
    }

    public static void showgameWin() {
        if (gameWinVisible) return;
        gameWinVisible = true;

        Rectangle dimmedBackground = createDimmedBackground();
        VBox vbox = creategameWinVBox();
        var gameWinImage = creategameWinImage();
        HBox buttonsContainer = createButtonsContainer();

        vbox.getChildren().addAll(createScoreText(), buttonsContainer);

        animateFadeIn(dimmedBackground);
        animateSlideIn(gameWinImage, -getAppHeight());
        animateSlideIn(buttonsContainer, getAppHeight());

//        gameWinOverlay = new StackPane(dimmedBackground, vbox);
        vbox.setLayoutX(475);
        vbox.setLayoutY(getAppHeight() - 300);
        gameWinImage.setX(getAppWidth() / 2 - gameWinImage.getFitWidth() / 2);
        gameWinImage.setY(-300);
        getGameScene().addUINodes(dimmedBackground, vbox, gameWinImage);

        pauseGame();
    }

    public static void hidegameWin() {
        if (!gameWinVisible) return;
        gameWinVisible = false;

        if (gameWinOverlay != null) {
            getGameScene().removeUINode(gameWinOverlay);
            gameWinOverlay = null;
        }

        resumeGame();
        refocusGameScene();
    }

    private static Rectangle createDimmedBackground() {
        Rectangle rect = new Rectangle(getAppWidth(), getAppHeight());
        rect.setFill(javafx.scene.paint.Color.color(0, 0, 0));
        return rect;
    }

    private static VBox creategameWinVBox() {
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
            hidegameWin();
            FXGL.getGameController().startNewGame();
            MusicHandler.playLevelMusic();
        });

        Button mainMenuButton = new Button("Exit");
        mainMenuButton.setPrefWidth(150);
        mainMenuButton.setFocusTraversable(false);
        mainMenuButton.setOnAction(e -> {
            hidegameWin();
            FXGL.getGameController().gotoMainMenu();
            MusicHandler.playMenuMusic();
        });

        HBox hbox = new HBox(10, restartButton, mainMenuButton);
        hbox.setStyle("-fx-alignment: center;");
        return hbox;
    }

    private static javafx.scene.image.ImageView creategameWinImage() {
        var image = FXGL.getAssetLoader().loadTexture("gamewin.png");
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