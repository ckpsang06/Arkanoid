package org.arkanoid;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import static com.almasb.fxgl.dsl.FXGL.getAppWidth;
import static com.almasb.fxgl.dsl.FXGL.spawn;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getAppHeight;
import static org.arkanoid.entity.Ball.BALL_SPEED;
import static org.arkanoid.entity.Brick.getBrickSize;

import static org.arkanoid.GameManager.isPaused;
import static org.arkanoid.GameManager.*;

public class LevelSelector {
    private static Entity currentBgEntity = null;

    // Menu / Level select
    private static StackPane tutorialOverlay = null;
    private static int currentLevel = 0;
    private static StackPane levelSelectPane = null;
    private static boolean initialSpawnsDone = true;

    private static void setLevelBackground(String textureName) {
        // remove previous background entity nếu có
        try {
            if (currentBgEntity != null) {
                currentBgEntity.removeFromWorld();
                currentBgEntity = null;
            }
        } catch (Exception ignore) {}

        // load texture và scale full-screen
        var tex = FXGL.getAssetLoader().loadTexture(textureName);
        tex.setFitWidth(getAppWidth());
        tex.setFitHeight(getAppHeight());

        // đảm bảo background bị vẽ SAU (ở dưới) các node khác: set viewOrder lớn
        tex.setViewOrder(1000.0);

        // tạo entity background và attach vào world
        // đặt view là texture node đã set viewOrder
        currentBgEntity = FXGL.entityBuilder()
                .view(tex)
                .buildAndAttach();
    }

    public static void showLevelSelect() {
        if (levelSelectPane != null && FXGL.getGameScene().getUINodes().contains(levelSelectPane)) {
            return;
        }

        final StackPane pane = new StackPane();
        pane.setPrefSize(getAppWidth(), getAppHeight());

        VBox v = new VBox(12);
        v.setStyle("-fx-alignment: center; -fx-padding: 20; -fx-background-color: rgba(0,0,0,0.6);");
        v.setPrefWidth(420);
        v.setPrefHeight(300);

        Text title = new Text("SELECT LEVEL");
        title.setStyle("-fx-font-size: 26px; -fx-fill: white;");

        v.getChildren().add(title);

        // số level đang có hiện tại.
        int levelsCount = 4;

        for (int i = 1; i <= levelsCount; i++) {
            final int levelIndex = i;
            Button btn = new Button("Level " + i + (i == 1 ? " — Tutorial" : ""));
            btn.setPrefWidth(300);

            // nếu muốn khóa một số level, kiểm tra điều kiện và setDisable = true.
            boolean locked = false;
            if (locked) btn.setDisable(true);

            btn.setOnAction(e -> {
                // remove overlay rồi bắt đầu level được chọn.
                try { FXGL.getGameScene().removeUINode(pane); } catch (Exception ignore) {}
                // reset focus, unpause if needed
                GameManager.isPaused = false;
                startLevel(levelIndex);
            });

            v.getChildren().add(btn);
        }

        // Back to menu.
        Button btnBack = new Button("Back to Menu");
        btnBack.setPrefWidth(300);
        btnBack.setOnAction(e -> {
            try { FXGL.getGameScene().removeUINode(pane); } catch (Exception ignore) {}
            FXGL.getGameController().gotoMainMenu();
        });
        v.getChildren().add(btnBack);

        pane.getChildren().add(v);
        StackPane.setAlignment(v, javafx.geometry.Pos.CENTER);

        // lưu reference để tránh add trùng và để có thể remove sau.
        levelSelectPane = pane;
        FXGL.getGameScene().addUINode(levelSelectPane);
    }

    private static void startLevel(int level) {
        currentLevel = level;

        if (level == 1 && initialSpawnsDone) {
            FXGL.getWorldProperties().intProperty("pixelsMoved").set(0);
            FXGL.getWorldProperties().intProperty("score").set(0);
            FXGL.getWorldProperties().intProperty("currentHP").set(3);

            if (paddle == null) {
                paddle = spawn("paddle", getAppWidth() / 2.0 - 60, getAppHeight() - 120);
            } else {
                paddle.setX(getAppWidth() / 2.0 - 60);
                paddle.setY(getAppHeight() - 120);
            }

            if (ball == null) {
                ball = spawn("ball", getAppWidth() / 2.0, getAppHeight() / 2.0);
            }

            ball.setProperty("velocity", Point2D.ZERO);
            ballLaunched = false;

            startTutorialUsingExisting(true);
            return;
        }

        // với các level khác nhau: tạo world cho level đã chọn.
        try {
            var ents = FXGL.getGameWorld().getEntitiesCopy();
            for (var e : ents) e.removeFromWorld();
        } catch (Exception ex) {
            System.err.println("Error clearing world: " + ex);
        }

        // Spawn layout depending on level number.
        if (level == 2) {
            setLevelBackground("danger.png");
            double brickW = getBrickSize();
            double spacing = 18;
            double startX = 40;
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 12; c++) {
                    spawn("brick", startX + c * (brickW + spacing), 140 + r * (brickW + 12));
                }
            }
        } else if (level == 3) {
            setLevelBackground("forest.png");

        } else {
            setLevelBackground("space.png");
            double brickW = getBrickSize();
            double spacing = 18;
            double startX = 60;
            for (int i = 0; i < 15; i++) {
                spawn("brick", startX + i * (brickW + spacing), 200);
            }
        }

        // reset vars
        FXGL.getWorldProperties().intProperty("pixelsMoved").set(0);
        FXGL.getWorldProperties().intProperty("score").set(0);
        FXGL.getWorldProperties().intProperty("currentHP").set(3);

        // spawn background and core entities
        paddle = spawn("paddle", getAppWidth() / 2.0 - 60, getAppHeight() - 120);
        ball = spawn("ball", getAppWidth() / 2.0, getAppHeight() / 2.0);
        ball.setProperty("velocity", Point2D.ZERO);
        ballLaunched = false;

        isPaused = false;
    }

    private static void startTutorialUsingExisting(boolean showTutorialOverlay) {
        currentLevel = 1;

        // reset vars
        FXGL.getWorldProperties().intProperty("pixelsMoved").set(0);
        FXGL.getWorldProperties().intProperty("score").set(0);
        FXGL.getWorldProperties().intProperty("currentHP").set(3);

        // đảm bảo entities tồn tại, spawn nếu thiếu.
        if (paddle == null) paddle = spawn("paddle", getAppWidth() / 2.0 - 60, getAppHeight() - 120);
        if (ball == null) {
            ball = spawn("ball", getAppWidth() / 2.0, getAppHeight() / 2.0);
            ball.setProperty("velocity", Point2D.ZERO);
            ballLaunched = false;
        }

        if (!showTutorialOverlay) {
            ball.setProperty("velocity", new Point2D(BALL_SPEED, BALL_SPEED));
            ballLaunched = true;
            isPaused = false;
        } else {
            VBox v = new VBox(8);
            v.setStyle("-fx-background-color: rgba(0,0,0,0.7); -fx-padding: 12; -fx-alignment: center;");
            v.setPrefWidth(460);
            v.setTranslateX((double) getAppWidth() / 2 - 230);
            v.setTranslateY(40);

            Text title = new Text("TUTORIAL");
            title.setStyle("-fx-font-size: 20px; -fx-fill: white;");
            Text line1 = new Text("Move left / right: A / D");
            line1.setStyle("-fx-fill: white;");
            Text line2 = new Text("Press SPACE to launch the ball. If ball falls you lose HP.");
            line2.setStyle("-fx-fill: white;");

            Button btnStart = new Button("Start");
            btnStart.setOnAction(e -> {
                try { FXGL.getGameScene().removeUINode(tutorialOverlay); } catch (Exception ignore) {}
                tutorialOverlay = null;
                isPaused = false;
            });
            btnStart.setFocusTraversable(false);

            v.getChildren().addAll(title, line1, line2, btnStart);

            tutorialOverlay = new StackPane(v);
            tutorialOverlay.setPickOnBounds(false);
            FXGL.getGameScene().addUINode(tutorialOverlay);

            // dừng đến khi start.
            isPaused = true;
        }

        System.out.println("[LEVEL] Tutorial started using existing spawns; showOverlay=" + showTutorialOverlay);
    }
}
