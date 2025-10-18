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
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import java.util.Map;

import org.arkanoid.entity.*;
import org.arkanoid.factory.GameBackgroundFactory;
import org.arkanoid.factory.GameEntityFactory;

import static com.almasb.fxgl.dsl.FXGL.getAppWidth;
import static com.almasb.fxgl.dsl.FXGL.spawn;
import static com.almasb.fxgl.dsl.FXGLForKtKt.*;
import static org.arkanoid.entity.Ball.*;
import static org.arkanoid.entity.Brick.getBrickSize;
import static org.arkanoid.entity.Paddle.getPaddleSpeed;
import static org.arkanoid.entity.Paddle.getPaddleWidth;
<<<<<<< Updated upstream

=======
import javafx.application.Platform;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
>>>>>>> Stashed changes

public class GameManager extends GameApplication {
    private Entity paddle;
    int movetype = 0;
    private Entity ball;
//    private final GameEntityFactory entityFactory = new GameEntityFactory();

// UI fields
    private HBox hpBox;
    private StackPane gameOverOverlay;
    private boolean gameOverVisible = false;
    private boolean isPaused = false;

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
    }

    /**
     * Manage the app's initial settings.
     * */
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(800);
        settings.setHeight(600);
        settings.setTitle("Arkanoid");
        settings.setVersion("0.1");settings.setGameMenuEnabled(true);
        settings.setMainMenuEnabled(true);
    }

    /**
     * Manage the game's initial settings.
     * */
    @Override
    protected void initGame() {
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

        paddle = spawn("paddle", 400, 500);
        ball = spawn("ball", (double) getAppWidth() / 2, (double) getAppHeight() / 2);
        for (int i = 0; i < 15; i++) {
//            spawn("brick", (double) getAppWidth() / 2 - 200 - getBrickSize(), 100 + i*(getBrickSize() + 20));
//            spawn("brick", (double) getAppWidth() / 2 + 200, 100 + i*(getBrickSize() + 20));
            spawn("brick", 20 + i * (getBrickSize() + 30), 250);
            spawn("brick", 20 + i * (getBrickSize() + 30), 350);
        }
    }

    /**
     * Manage the physics between entities in the games.
     * */
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
        var computery = FXGL.getAssetLoader().loadTexture("computery.png");
        computery.setTranslateX((double) getAppWidth() / 2 - 100);
        computery.setTranslateY(20);
        getGameScene().addUINode(computery);

        // UI TEXT.
        Text textHp = new Text();
        textHp.setTranslateX(10);
        textHp.setTranslateY(40);
        textHp.textProperty().bind(getWorldProperties().intProperty("currentHp").asString());
        getGameScene().addUINode(textHp);
    }

    /**
     * Manage what happens in the game every tpf.
     * */
    @Override
    protected void onUpdate(double tpf) {
        try {
            Point2D velocity = ball.getObject("velocity");
            ball.translate(velocity);

            if (velocity == null) {
                velocity = new Point2D(BALL_SPEED, BALL_SPEED);
                ball.setProperty("velocity", velocity);
            }

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

            if (ball.getBottomY() > getAppHeight()) {
                // take and reduce hp.
                var hpProp = getWorldProperties().intProperty("currentHp");
                int currentHp = Math.max(0, hpProp.get() - 1);
                hpProp.set(currentHp);

                System.out.println("Ball out! Hp còn : " + currentHp);
                if (currentHp <= 0) {
                    onGameOver();
                } else {
                    resetBall();
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
         if (paddle == null || ball == null) {
             System.err.println("resetBallAbovePaddle: paddle hoặc ball = null");
             return;
    }
        ball.setPosition((double) getAppWidth() / 2 - (double) BALL_SIZE / 2, (double) getAppHeight() / 2 - (double) BALL_SIZE / 2);
        ball.setProperty("velocity", new Point2D(BALL_SPEED, BALL_SPEED));
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

    /**
     * Game launch.
     * */
    public static void main(String[] args) {
        launch(args);
    }

}
