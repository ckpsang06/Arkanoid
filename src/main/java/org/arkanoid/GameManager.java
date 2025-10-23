package org.arkanoid;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
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
import static org.arkanoid.entity.Paddle.getPaddleSpeed;

import javafx.scene.layout.StackPane;
import org.arkanoid.factory.GameSceneFactory;
import org.arkanoid.utility.MusicHandler;
import org.arkanoid.utility.PhysicsHandler;

public class GameManager extends GameApplication {
    private Entity paddle;
    public static int movetype = 0;
    private Entity ball;

    private boolean ballLaunched = false;


    Random rand = new Random();
//    private final GameEntityFactory entityFactory = new GameEntityFactory();

    // UI fields
    protected static StackPane gameOverOverlay;
    protected static boolean gameOverVisible = false;
    protected static boolean isPaused = false;

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
        settings.setGameMenuEnabled(true);
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

                System.out.println("Ball out! Hp còn : " + currentHP);
                if (currentHP <= 0) {
                    System.out.println("Setting gameIsOver = true");
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