package org.arkanoid;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import java.util.Map;
import java.util.Random;

import org.arkanoid.entity.*;
import org.arkanoid.factory.GameBackgroundFactory;
import org.arkanoid.factory.GameEntityFactory;

import static com.almasb.fxgl.dsl.FXGL.getAppWidth;
import static com.almasb.fxgl.dsl.FXGL.spawn;
import static com.almasb.fxgl.dsl.FXGLForKtKt.*;
import static org.arkanoid.entity.Ball.*;
import static org.arkanoid.entity.Brick.getBrickSize;
import static org.arkanoid.entity.Paddle.getPaddleSpeed;

import javafx.scene.layout.StackPane;
import org.arkanoid.factory.GameSceneFactory;
import org.arkanoid.utility.MusicHandler;
import org.arkanoid.utility.PhysicsHandler;

public class GameManager extends GameApplication {
    private Entity paddle;
    public static int movetype = 0;
    private Entity ball;
    private Entity currentBgEntity = null;

    private boolean ballLaunched = false;


    Random rand = new Random();
//    private final GameEntityFactory entityFactory = new GameEntityFactory();

    // UI fields
    protected static StackPane gameOverOverlay;
    protected static boolean gameOverVisible = false;
    protected static boolean isPaused = false;

    // Menu / Level select
    private StackPane tutorialOverlay = null;
    private int currentLevel = 0;
    private StackPane levelSelectPane = null;
    private boolean initialSpawnsDone = true;


    /**
     * Manage all commands executed via inputs - keyboards in this case.
     * */
    @Override
    protected void initInput() {
        // IF D KEY IS PRESSED.
        FXGL.onKey(KeyCode.D, () -> {
            if (paddle.getRightX() < getAppWidth()) {
                paddle.translateX(getPaddleSpeed());
                movetype = 1;
                FXGL.inc("pixelsMoved", +getPaddleSpeed());
            }
        });

        // IF A KEY IS PRESSED.
        FXGL.onKey(KeyCode.A, () -> {
            if (paddle.getX() > 0) {
                paddle.translateX(-getPaddleSpeed());
                movetype = 2;
                FXGL.inc("pixelsMoved", -getPaddleSpeed());
            }
        });

        FXGL.onKeyDown(KeyCode.ESCAPE, () -> {
//            if (!isPaused) {
//                System.out.println("Paused!");
//                MusicHandler.pauseMusic();
//                isPaused = true;
//            }
//            else if (isPaused) {
//                System.out.println("Unpaused!");
//                MusicHandler.resumeMusic();
//                isPaused = false;
//            }
            MusicHandler.pauseMusic();
        });

        FXGL.onKeyDown(KeyCode.SPACE, () -> {
            if (!ballLaunched) {
                ball.setProperty("velocity", new Point2D(BALL_SPEED, BALL_SPEED));
                ballLaunched = true;
                FXGL.play("launch.wav"); // Optional sound effect
                System.out.println("Ball launched!");
            }
        });

    }

    /**
     * Manage the app's initial settings.
     * */
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setSceneFactory(new GameSceneFactory());
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("Arkanoid");
        settings.setVersion("0.1");
        settings.setMainMenuEnabled(true);
    }

    /**
     * Manage the game's initial settings.
     * */
    @Override
    protected void initGame() {
        // Stop any currently playing music and restart track.

        getGameWorld().addEntityFactory(new GameEntityFactory());
        getGameWorld().addEntityFactory(new GameBackgroundFactory());
        // BACKGROUND IMAGE - this doesn't work because of addUINode which will cover the entire screen.
        //  This remains to display how to achieve the above if needed. Deprecated for now.

//        Image grassBackground = image("grass_background.png");
//        ImageView grassBackgroundView = new ImageView(grassBackground);
//        grassBackgroundView.setFitHeight(getAppHeight());
//        grassBackgroundView.setFitWidth(getAppWidth());
//        getGameScene().addUINode(grassBackgroundView);

        spawn("grassBackground", 0, 0);

        paddle = spawn("paddle", 400, 600);

        ball = spawn("ball", getAppWidth() / 2.0, getAppHeight() / 2.0 + 50);
        ball.setProperty("velocity", Point2D.ZERO); // No movement yet

        for (int i = 0; i < 15; i++) {
            spawn("brick", 5 + i * (getAppWidth() / 15 + 5), 200);
            spawn("brick", 5 + i * (getAppWidth() / 15 + 5), 300);
        }
        showLevelSelect();
    }
    private void setLevelBackground(String textureName) {
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

    private void showLevelSelect() {
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
                isPaused = false;
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

    private void startLevel(int level) {
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

    private void startTutorialUsingExisting(boolean showTutorialOverlay) {
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

    /**
     * Manage the physics between entities in the games.
     * PLEASE PLEASE PLEASE REMEMBER TO REFACTOR THIS LATER
     * */
    @Override
    protected void initPhysics() {
        PhysicsHandler.ball_brickCollision();
        PhysicsHandler.ball_paddleCollision();
        PhysicsHandler.paddle_healItemCollision();
    }

    /**
     * Manage the game's global variables.
     *
     * @param vars The game's list of variables.
     * */
    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("pixelsMoved", 0);
        vars.put("score", 0);
        vars.put("currentHP", 3);
    }

    /**
     * Manage the game's UI code.
     * */
    @Override
    protected void initUI() {
        Text textPixels = new Text();
        textPixels.setTranslateX(10); // x = 10
        textPixels.setTranslateY(10); // y = 10

//        Text textScore = getUIFactoryService().newText("", Color.BLACK, 22);
        Text textScore = new Text();
        textScore.setTranslateX(10);
        textScore.setTranslateY(20);

        textPixels.textProperty().bind(getWorldProperties().intProperty("pixelsMoved").asString());
        textScore.textProperty().bind(getWorldProperties().intProperty("score").asString());

        getGameScene().addUINode(textPixels); // add to the scene graph
        getGameScene().addUINode(textScore);

        // UI IMAGEs
//        var computery = FXGL.getAssetLoader().loadTexture("computery.png");
//        computery.setTranslateX((double) getAppWidth() / 2 - 100);
//        computery.setTranslateY(20);
//        getGameScene().addUINode(computery);

        // UI TEXT.
        Text textHp = new Text();
        textHp.setTranslateX(10);
        textHp.setTranslateY(40);
        textHp.textProperty().bind(getWorldProperties().intProperty("currentHP").asString());
        getGameScene().addUINode(textHp);
    }

    /**
     * Manage what happens in the game every tpf.
     * */
    @Override
    protected void onUpdate(double tpf) {
        if (!ballLaunched) {
            return; // Skip ball movement until launched
        }

        try {
            Point2D velocity = ball.getObject("velocity");
            ball.translate(velocity);

            for (Entity e : FXGL.getGameWorld().getEntities()) {
                if (e.getType() == EntityType.HEAL_ITEM) {
                    e.translateY(PowerUpPickup.getPowerUpSpeed());
                }
            }

            if (ball.getX() <= 0) {
                ball.setX(0);
                ball.setProperty("velocity", new Point2D(-velocity.getX(), velocity.getY()));
                FXGL.play("wallhit.wav");
                getGameScene().getViewport().shakeTranslational(4.5);
            }

            if (ball.getRightX() >= getAppWidth()) {
                ball.setX(getAppWidth() - BALL_SIZE);
                ball.setProperty("velocity", new Point2D(-velocity.getX(), velocity.getY()));
                FXGL.play("wallhit.wav");
                getGameScene().getViewport().shakeTranslational(4.5);
            }

            if (ball.getY() <= 0) {
                ball.setY(0);
                ball.setProperty("velocity", new Point2D(velocity.getX(), -velocity.getY()));
                FXGL.play("wallhit.wav");
                getGameScene().getViewport().shakeTranslational(4.5);
            }

            // IF BALL FALLS INTO KILL-ZONE
            if (ball.getBottomY() > getAppHeight()) {
                getGameScene().getViewport().shakeTranslational(9.5);
                FXGL.play("killzone.wav");
                // take and reduce hp.
                var hpProp = getWorldProperties().intProperty("currentHP");
                int currentHP = Math.max(0, hpProp.get() - 1);
                hpProp.set(currentHP);
                resetBall();

                System.out.println("Ball out! Hp còn : " + currentHP);
                if (currentHP <= 0) {
                    System.out.println("Setting gameIsOver = true");
                    onGameOver();
                }
                return;
            }
        } catch (ClassCastException cce) {
            System.err.println("ClassCastException trong onUpdate: " + cce.getMessage());
            cce.printStackTrace();
        } catch (NullPointerException npe) {
            System.err.println("NullPointerException trong onUpdate: " + npe.getMessage());
            npe.printStackTrace();
        } catch (Exception ex) {
            System.err.println("Exception bất ngờ trong onUpdate: " + ex);
            ex.printStackTrace();
        }
    }

    /**
     * Reset ball's position to a fixed point.
     * */
    private void resetBall() {
        ballLaunched = false;
        ball.setProperty("velocity", Point2D.ZERO);
        ball.setPosition(getAppWidth() / 2.0 - BALL_SIZE / 2.0, getAppHeight() / 2.0 - BALL_SIZE / 2.0);

//        if (paddle == null || ball == null) {
//            System.err.println("resetBallAbovePaddle: paddle hoặc ball = null");
//            return;
//        }
//        ball.setPosition((double) getAppWidth() / 2 - (double) BALL_SIZE / 2, (double) getAppHeight() / 2 - (double) BALL_SIZE / 2);
//        ball.setProperty("velocity", new Point2D(BALL_SPEED, BALL_SPEED));
    }

    private void onGameOver() {
        GameOverManager.onGameOver();
    }

    /**
     * Game launch.
     * */
    public static void main(String[] args) {
        launch(args);
    }

}