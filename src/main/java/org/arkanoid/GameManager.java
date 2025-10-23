package org.arkanoid;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.handlers.CollectibleHandler;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
<<<<<<< Updated upstream
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
=======
>>>>>>> Stashed changes
import javafx.scene.text.Text;
import java.util.Map;

import org.arkanoid.entity.*;
import org.arkanoid.factory.GameBackgroundFactory;
import org.arkanoid.factory.GameEntityFactory;

import static com.almasb.fxgl.dsl.FXGL.getAppWidth;
import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.spawn;
import static org.arkanoid.entity.Ball.*;
import static org.arkanoid.entity.Brick.getBrickSize;
import static org.arkanoid.entity.Paddle.getPaddleSpeed;
import static org.arkanoid.entity.Paddle.getPaddleWidth;
<<<<<<< Updated upstream

<<<<<<< Updated upstream
=======
import javafx.application.Platform;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
>>>>>>> Stashed changes
=======
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import org.arkanoid.factory.GameSceneFactory;
import org.arkanoid.utility.MusicHandler;
import org.arkanoid.utility.PhysicsHandler;
>>>>>>> Stashed changes

public class GameManager extends GameApplication {
    private Entity paddle;
    int movetype = 0;
    private Entity ball;
<<<<<<< Updated upstream
//    private final GameEntityFactory entityFactory = new GameEntityFactory();
=======

    private boolean ballLaunched = false;
    private boolean initialSpawnsDone = false;

    Random rand = new Random();
>>>>>>> Stashed changes

// UI fields
    private HBox hpBox;
    private StackPane gameOverOverlay;
    private boolean gameOverVisible = false;
    private boolean isPaused = false;

    // Menu / Level select
    private StackPane tutorialOverlay = null;
    private int currentLevel = 0;

    /* ---------------- Input ---------------- */
    @Override
    protected void initInput() {
        FXGL.onKey(KeyCode.D, () -> {
            if (isPaused) return;
            if (paddle == null) return;
            if (paddle.getRightX() < getAppWidth()) {
                paddle.translateX(getPaddleSpeed());
                movetype = 1;
                FXGL.inc("pixelsMoved", +getPaddleSpeed());
            }
        });

        FXGL.onKey(KeyCode.A, () -> {
            if (isPaused) return;
            if (paddle == null) return;
            if (paddle.getX() > 0) {
                paddle.translateX(-getPaddleSpeed());
                movetype = 2;
                FXGL.inc("pixelsMoved", -getPaddleSpeed());
            }
        });
<<<<<<< Updated upstream
=======

        FXGL.onKey(KeyCode.ESCAPE, () -> {
            MusicHandler.pauseMusic();
        });

        FXGL.onKeyDown(KeyCode.SPACE, () -> {
            if (isPaused) return;
            if (!ballLaunched && ball != null) {
                ball.setProperty("velocity", new Point2D(BALL_SPEED, BALL_SPEED));
                ballLaunched = true;
                FXGL.play("launch.wav");
                System.out.println("Ball launched!");
            }
        });
>>>>>>> Stashed changes
    }

    @Override
    protected void initSettings(GameSettings settings) {
<<<<<<< Updated upstream
        settings.setWidth(800);
        settings.setHeight(600);
=======
        settings.setSceneFactory(new GameSceneFactory()); // your factory (must return MainMenu)
        settings.setWidth(1280);
        settings.setHeight(720);
>>>>>>> Stashed changes
        settings.setTitle("Arkanoid");
        settings.setVersion("0.1");settings.setGameMenuEnabled(true);
        settings.setMainMenuEnabled(true);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("pixelsMoved", 0);
        vars.put("score", 0);
        vars.put("currentHP", 3);
    }

    @Override
    protected void initGame() {
<<<<<<< Updated upstream
        getGameWorld().addEntityFactory(new GameEntityFactory());
        getGameWorld().addEntityFactory(new GameBackgroundFactory());
        // BACKGROUND IMAGE - this doesn't work because of addUINode which will cover the entire screen.
        //  This remains to display how to achieve the above if needed. Deprecated for now.

//        Image grassBackground = image("grass_background.png");
//        ImageView grassBackgroundView = new ImageView(grassBackground);
//        grassBackgroundView.setFitHeight(getAppHeight());
//        grassBackgroundView.setFitWidth(getAppWidth());
//        getGameScene().addUINode(grassBackgroundView);
=======
        FXGL.getGameWorld().addEntityFactory(new GameEntityFactory());
        FXGL.getGameWorld().addEntityFactory(new GameBackgroundFactory());
>>>>>>> Stashed changes

        spawn("grassBackground", 0, 0);

        paddle = spawn("paddle", 400, 500);
        ball = spawn("ball", (double) getAppWidth() / 2, (double) getAppHeight() / 2);
        for (int i = 0; i < 15; i++) {
//            spawn("brick", (double) getAppWidth() / 2 - 200 - getBrickSize(), 100 + i*(getBrickSize() + 20));
//            spawn("brick", (double) getAppWidth() / 2 + 200, 100 + i*(getBrickSize() + 20));
            spawn("brick", 20 + i * (getBrickSize() + 30), 250);
            spawn("brick", 20 + i * (getBrickSize() + 30), 350);
        }

            // hiển thị level để chọn.
        showLevelSelect();
    }

<<<<<<< Updated upstream
    /**
     * Manage the physics between entities in the games.
     * */
=======
>>>>>>> Stashed changes
    @Override
    protected void initPhysics() {
        onCollisionBegin(EntityType.BALL, EntityType.BRICK, (ball, brick) -> {
            FXGL.inc("score", 1);
            FXGL.play("land.wav");
            brick.removeFromWorld();
            Point2D velocity = ball.getObject("velocity");

            if (FXGLMath.randomBoolean()) {
                ball.setProperty("velocity", new Point2D(-velocity.getX(), velocity.getY()));
            } else {
                ball.setProperty("velocity", new Point2D(velocity.getX(), -velocity.getY()));
            }
            return null;
        });

        onCollisionBegin(EntityType.BALL, EntityType.PADDLE, (ball, paddle) -> {
            Point2D velocity1 = ball.getObject("velocity");


            // Reverse Y direction for horizontal paddle bounce
            Point2D newVelocity;
            if (movetype == 1) {
                newVelocity = new Point2D(getBallSpeed(), -velocity1.getY());
            } else if (movetype == 2) {
                newVelocity = new Point2D(-getBallSpeed(), -velocity1.getY());
            } else {
                newVelocity = new Point2D(0, -getBallSpeed() * 1.41);
            }
            ball.setProperty("velocity", newVelocity);
            return null;
        });
    }

<<<<<<< Updated upstream
    /**
     * Manage the game's global variables.
     *
     * @param vars The game's list of variables.
     * */
     @Override
protected void initGameVars(Map<String, Object> vars) {
    vars.put("pixelsMoved", 0);
    vars.put("score", 0);
    vars.put("currentHp", 3);   // <-- thêm dòng này (số HP ban đầu)
}

    /**
     * Manage the game's UI code.
     * */
=======
>>>>>>> Stashed changes
    @Override
    protected void initUI() {
        Text textPixels = new Text();
        textPixels.setTranslateX(10);
        textPixels.setTranslateY(10);
        Text textScore = new Text();
        textScore.setTranslateX(10);
        textScore.setTranslateY(30);

        textPixels.textProperty().bind(FXGL.getWorldProperties().intProperty("pixelsMoved").asString());
        textScore.textProperty().bind(FXGL.getWorldProperties().intProperty("score").asString());
        FXGL.getGameScene().addUINode(textPixels);
        FXGL.getGameScene().addUINode(textScore);

        var computery = FXGL.getAssetLoader().loadTexture("computery.png");
        computery.setTranslateX((double) getAppWidth() / 2 - 100);
        computery.setTranslateY(20);
        FXGL.getGameScene().addUINode(computery);

        Text textHp = new Text();
        textHp.setTranslateX(10);
<<<<<<< Updated upstream
        textHp.setTranslateY(40);
        textHp.textProperty().bind(getWorldProperties().intProperty("currentHp").asString());
        getGameScene().addUINode(textHp);
=======
        textHp.setTranslateY(50);
        textHp.textProperty().bind(FXGL.getWorldProperties().intProperty("currentHP").asString("HP: %d"));
        FXGL.getGameScene().addUINode(textHp);
>>>>>>> Stashed changes
    }

    @Override
    protected void onUpdate(double tpf) {
<<<<<<< Updated upstream
=======
        if (isPaused) return;

>>>>>>> Stashed changes
        try {
            Point2D velocity = ball.getObject("velocity");

            if (ballLaunched && velocity != null) {
                ball.translate(velocity);
            }

            if (velocity == null) {
                velocity = new Point2D(BALL_SPEED, BALL_SPEED);
                ball.setProperty("velocity", velocity);
            }

            if (velocity != null) {
                if (ball.getX() <= 0) {
                    ball.setX(0);
                    ball.setProperty("velocity", new Point2D(-velocity.getX(), velocity.getY()));
                }

                if (ball.getRightX() >= getAppWidth()) {
                    ball.setX(getAppWidth() - BALL_SIZE);
                    ball.setProperty("velocity", new Point2D(-velocity.getX(), velocity.getY()));
                }

                if (ball.getY() <= 0) {
                    ball.setY(0);
                    ball.setProperty("velocity", new Point2D(velocity.getX(), -velocity.getY()));
                }
            }

<<<<<<< Updated upstream
            if (ball.getRightX() >= getAppWidth()) {
                ball.setX(getAppWidth() - BALL_SIZE);
                ball.setProperty("velocity", new Point2D(-velocity.getX(), velocity.getY()));
            }

            if (ball.getY() <= 0) {
                ball.setY(0);
                ball.setProperty("velocity", new Point2D(velocity.getX(), -velocity.getY()));
            }

            if (ball.getBottomY() > getAppHeight()) {
                // take and reduce hp.
                var hpProp = getWorldProperties().intProperty("currentHp");
                int currentHp = Math.max(0, hpProp.get() - 1);
                hpProp.set(currentHp);

                System.out.println("Ball out! Hp còn : " + currentHp);
                if (currentHp <= 0) {
=======
            if (ball.getBottomY() > getAppHeight()) {
                FXGL.play("killzone.wav");
                var hpProp = FXGL.getWorldProperties().intProperty("currentHP");
                int currentHP = Math.max(0, hpProp.get() - 1);
                hpProp.set(currentHP);

                System.out.println("Ball out! Hp còn : " + currentHP);
                if (currentHP <= 0) {
>>>>>>> Stashed changes
                    onGameOver();
                } else {
                    resetBall();
                }
                return;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void showLevelSelect() {
        StackPane pane = new StackPane();
        pane.setPrefSize(getAppWidth(), getAppHeight());

        VBox v = new VBox(12);
        v.setStyle("-fx-alignment: center; -fx-padding: 20; -fx-background-color: rgba(0,0,0,0.6);");
        v.setPrefWidth(420);
        v.setPrefHeight(300);

        Text title = new Text("SELECT LEVEL");
        title.setStyle("-fx-font-size: 26px; -fx-fill: white;");

        Button btnL1 = new Button("Level 1 — Tutorial");
        btnL1.setPrefWidth(300);
        btnL1.setOnAction(e -> {
            try { FXGL.getGameScene().removeUINode(pane); } catch (Exception ignore) {}
            startTutorialUsingExisting(true);
        });

        Button btnBack = new Button("Back to Menu");
        btnBack.setPrefWidth(300);
        btnBack.setOnAction(e -> {
            try { FXGL.getGameScene().removeUINode(pane); } catch (Exception ignore) {}
            FXGL.getGameController().gotoMainMenu();
        });

        v.getChildren().addAll(title, btnL1, btnBack);
        pane.getChildren().add(v);

        StackPane.setAlignment(v, javafx.geometry.Pos.CENTER);

        FXGL.getGameScene().addUINode(pane);
    }

    private void startTutorialUsingExisting(boolean showTutorialOverlay) {
        currentLevel = 1;

        FXGL.getWorldProperties().intProperty("pixelsMoved").set(0);
        FXGL.getWorldProperties().intProperty("score").set(0);
        FXGL.getWorldProperties().intProperty("currentHP").set(3);

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

            isPaused = true;
        }

        System.out.println("[LEVEL] Tutorial started using existing spawns; showOverlay=" + showTutorialOverlay);
    }

    private void resetBall() {
<<<<<<< Updated upstream
         if (paddle == null || ball == null) {
             System.err.println("resetBallAbovePaddle: paddle hoặc ball = null");
             return;
    }
        ball.setPosition((double) getAppWidth() / 2 - (double) BALL_SIZE / 2, (double) getAppHeight() / 2 - (double) BALL_SIZE / 2);
        ball.setProperty("velocity", new Point2D(BALL_SPEED, BALL_SPEED));
=======
        ballLaunched = false;
        if (ball == null) return;
        ball.setProperty("velocity", Point2D.ZERO);
        ball.setPosition(getAppWidth() / 2.0 - BALL_SIZE / 2.0, getAppHeight() / 2.0 - BALL_SIZE / 2.0);
>>>>>>> Stashed changes
    }

    private void onGameOver() {
        System.out.println("Game Over!");
<<<<<<< Updated upstream
=======
        showGameOver();
    }

    private void showGameOver() {
        if (gameOverVisible) return;
        gameOverVisible = true;

        Rectangle dim = new Rectangle(getAppWidth(), getAppHeight());
        dim.setFill(javafx.scene.paint.Color.color(0, 0, 0, 0.6));

        VBox vbox = new VBox(12);
        vbox.setPrefWidth(320);
        vbox.setPrefHeight(220);
        vbox.setStyle("-fx-alignment: center; -fx-padding: 20;");

        // Ảnh gameover (texture)
        var goImg = FXGL.getAssetLoader().loadTexture("gameover.png");
        goImg.setFitWidth(300);
        goImg.setFitHeight(100);

        Text finalScore = new Text("Score: " + getWorldProperties().intProperty("score").get());
        finalScore.setStyle("-fx-font-size: 20px; -fx-fill: white;");

        Button btnRestart = new Button("Restart");
        btnRestart.setPrefWidth(120);
        btnRestart.setFocusTraversable(false);
        btnRestart.setOnAction(e -> {
            hideGameOver();
            FXGL.getGameController().startNewGame();
        });

        Button btnMainMenu = new Button("Main Menu");
        btnMainMenu.setPrefWidth(120);
        btnMainMenu.setFocusTraversable(false);
        btnMainMenu.setOnAction(e -> {
            hideGameOver();
            FXGL.getGameController().gotoMainMenu();
        });

        HBox btns = new HBox(10, btnRestart, btnMainMenu);
        btns.setStyle("-fx-alignment: center;");

        vbox.getChildren().addAll(goImg, finalScore, btns);

        gameOverOverlay = new StackPane(dim, vbox);
        StackPane.setAlignment(vbox, Pos.CENTER);

        getGameScene().addUINode(gameOverOverlay);

        //Dừng cập nhật game mà vẫn giữ UI hoạt động
        FXGL.getGameController().pauseEngine();
        FXGL.getInput().setProcessInput(false);

        isPaused = true;
    }

    private void hideGameOver() {
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
>>>>>>> Stashed changes
    }

    public static void main(String[] args) {
        launch(args);
    }
<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
}
