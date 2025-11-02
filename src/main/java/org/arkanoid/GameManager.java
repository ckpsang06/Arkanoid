package org.arkanoid;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import java.util.Map;
import java.util.Random;

import javafx.scene.paint.Color;

import org.arkanoid.component.AnimationComponent;
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
import org.arkanoid.level.LevelSelector;
import org.arkanoid.utility.MusicHandler;
import org.arkanoid.utility.PhysicsHandler;

public class GameManager extends GameApplication {
    public static Entity paddle;
    public static int movetype = 0;
    // UI fields
    protected static StackPane gameOverOverlay;
    protected static StackPane gameWinOverlay;
    protected static boolean gameOverVisible = false;
    protected static boolean gameWinVisible = false;
    public static boolean isPaused = false;

    /**
     * Manage all commands executed via inputs - keyboards in this case.
     * */
    @Override
    protected void initInput() {
        // IF D KEY IS PRESSED.
        FXGL.onKey(KeyCode.A, () -> {
            if (paddle.getX() > 0) {
                paddle.translateX(-getPaddleSpeed());
                movetype = 2;
                FXGL.inc("pixelsMoved", -getPaddleSpeed());
            }
        });
        FXGL.onKey(KeyCode.D, () -> {
            if (paddle.getRightX() < getAppWidth()) {
                paddle.translateX(getPaddleSpeed());
                movetype = 1;
                FXGL.inc("pixelsMoved", +getPaddleSpeed());
            }
        });

        FXGL.onKeyDown(KeyCode.SPACE, () -> {
            var attachedBall = FXGL.getGameWorld()
                    .getEntitiesByType(EntityType.BALL)
                    .stream()
                    .filter(b -> b.getBoolean("attachedToPaddle"))
                    .findFirst();

            if (attachedBall.isPresent()) {
                Entity ballToLaunch = attachedBall.get();
                Point2D launchVelocity = new Point2D(0, -BALL_SPEED);

                if (org.arkanoid.utility.PhysicsHandler.ballSpeedActive) {
                    launchVelocity = launchVelocity.multiply(org.arkanoid.utility.PhysicsHandler.ballSpeedMultiplier);
                }

                ballToLaunch.setProperty("velocity", launchVelocity);
                ballToLaunch.setProperty("attachedToPaddle", false);
                FXGL.play("launch.wav");
                System.out.println("Ball launched!");
            }
        });

        FXGL.onKeyDown(KeyCode.ESCAPE, () -> {
            MusicHandler.pauseMusic();
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
        getGameWorld().addEntityFactory(new GameEntityFactory());
        getGameWorld().addEntityFactory(new GameBackgroundFactory());
        spawn("grassBackground", 0, 0);
        paddle = spawn("temp_paddle", 0, 0);
        LevelSelector.showLevelSelect();
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
        PhysicsHandler.paddle_expandItemCollision();
        PhysicsHandler.paddle_ballItemCollision();
        PhysicsHandler.paddle_ballSpeedItemCollision();
        PhysicsHandler.paddle_paddleSpeedItemCollision();
        PhysicsHandler.ball_boss1Collision();
        PhysicsHandler.fireball_paddleCollision();
    }

    /**
     * Manage the game's global variables.
     *
     * @param vars The game's list of variables.
     * */
    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("pixelsMoved", 0.0);
        vars.put("score", 0);
        vars.put("currentHP",3);
        vars.put("elapsedTime", 0.0);
    }

    /**
     * Manage the game's UI code, including timer, score and HP.
     * */
    @Override
    protected void initUI() {
        Font pixellariFont = loadFont("/assets/ui/fonts/Pixellari.ttf", 30);
        Font pixelmixFont = loadFont("/assets/ui/fonts/pixelmix.ttf", 30);

        addTimerUI(pixelmixFont);
        addScoreUI(pixelmixFont);
        addHealthUI(pixelmixFont);
    }

    /**
     * Load font with specified parameters
     * */
    private Font loadFont(String path, int size) {
        return Font.loadFont(getClass().getResourceAsStream(path), size);
    }

    private void addTimerUI(Font font) {
        var timer = FXGL.getAssetLoader().loadTexture("timer.png");
        timer.setTranslateX(20);
        timer.setTranslateY(20);
        timer.setFitWidth(26);
        timer.setFitHeight(40);
        getGameScene().addUINode(timer);

        Text textPixels = new Text();
        textPixels.setTranslateX(60);
        textPixels.setTranslateY(53);
        textPixels.setFont(font);
        textPixels.setFill(Color.WHITE);
        textPixels.setStroke(Color.BLACK);
        getGameScene().addUINode(textPixels);

        FXGL.getGameTimer().runAtInterval(() -> {
            int totalSeconds = (int) FXGL.getd("elapsedTime");
            int minutes = totalSeconds / 60;
            int seconds = totalSeconds % 60;
            textPixels.setText(String.format("%02d:%02d", minutes, seconds));
        }, javafx.util.Duration.millis(200));
    }

    private void addScoreUI(Font font) {
        var scoreIcon = FXGL.getAssetLoader().loadTexture("power.png");
        Text textScore = new Text();
        textScore.setTranslateX(getAppWidth() - 70);
        textScore.setTranslateY(55);
        textScore.setFont(font);
        textScore.setFill(Color.WHITE);
        textScore.setStroke(Color.BLACK);
        textScore.textProperty().bind(getWorldProperties().intProperty("score").asString());
        getGameScene().addUINode(textScore);
    }

    private void addHealthUI(Font font) {
        var heart = FXGL.getAssetLoader().loadTexture("heart.png");
        heart.setTranslateX(5);
        heart.setTranslateY(75);
        heart.setFitWidth(60);
        heart.setPreserveRatio(true);
        getGameScene().addUINode(heart);

        Text textHp = new Text();
        textHp.setTranslateX(67);
        textHp.setTranslateY(108);
        textHp.setFont(font);
        textHp.setFill(Color.WHITE);
        textHp.setStroke(Color.BLACK);
        textHp.textProperty().bind(getWorldProperties().intProperty("currentHP").asString());
        getGameScene().addUINode(textHp);
    }

    /**
     * Manage what happens in the game every tpf.
     * Including: timer's clock
     * */
    @Override
    protected void onUpdate(double tpf) {
        // Update timer
        double elapsed = FXGL.getd("elapsedTime");
        FXGL.set("elapsedTime", elapsed + tpf);

        // Paddle's HP when falling below 0 will trigger gameOver
        var hpProp = getWorldProperties().intProperty("currentHP");
        if (hpProp.get() == 0) {
            GameOverManager.onGameOver();
        }

        // Power-up items position translation
        for (Entity e : FXGL.getGameWorld().getEntities()) {
            if (e.getType() == EntityType.HEAL_ITEM
                    || e.getType() == EntityType.EXPAND_ITEM
                    || e.getType() == EntityType.BALL_ITEM
                    || e.getType() == EntityType.BALLSPEED_ITEM
                    || e.getType() == EntityType.PADDLESPEED_ITEM) {
                e.translateY(PowerUpPickup.getPowerUpSpeed());
            }
        }

        // Add balls that fall into the killzone.
        java.util.List<Entity> ballsToRemove = new java.util.ArrayList<>();
        // Manage ball's collision with wall behavior
        for (Entity currentBall : FXGL.getGameWorld().getEntitiesByType(EntityType.BALL)) {
            try {
                if (currentBall.getBoolean("attachedToPaddle")) {

                    // Nếu dính, di chuyển nó theo paddle
                    currentBall.setX(paddle.getX() + paddle.getWidth() / 2.0 - Ball.BALL_SIZE / 2.0);
                    currentBall.setY(paddle.getY() - 30);

                } else {
                    // Wall collision
                    Point2D velocity = currentBall.getObject("velocity");
                    PhysicsHandler.ball_wallCollision(currentBall, ballsToRemove, velocity);
                    // Boss movement in hell level
                    bossBehaviour(velocity);

                }
                } catch (Exception ex){
                System.err.println("Exception catched in onUpdate: " + ex);
                ex.printStackTrace();
            }
        }

        if (getGameWorld().getEntitiesByType(EntityType.BRICK).isEmpty() && !LevelSelector.inSelection
                && getGameWorld().getEntitiesByType(EntityType.HELL_BOSS).isEmpty()) {
            GameWinManager.onGameWin();
        }

        // No more brick = Win the game
        if (getGameWorld().getEntitiesByType(EntityType.BRICK).isEmpty() && !LevelSelector.inSelection) {
            GameWinManager.onGameWin();
        }

        // When ball falls into the killzone
        if (!ballsToRemove.isEmpty()) {
            for (Entity ballToRemove : ballsToRemove) {
                ballToRemove.removeFromWorld();
            }

            getGameScene().getViewport().shakeTranslational(9.5);
            FXGL.play("killzone.wav");

            if (FXGL.getGameWorld().getEntitiesByType(EntityType.BALL).isEmpty()) {

                hpProp = getWorldProperties().intProperty("currentHP");
                int currentHP = Math.max(0, hpProp.get() - 1);
                hpProp.set(currentHP);

                System.out.println("Ball out! Hp còn : " + currentHP);

                resetBall();
            }
        }
    }

    private void bossBehaviour(Point2D velocity) {
        Random rand = new Random();
        for (Entity boss1 : FXGL.getGameWorld().getEntitiesByType(EntityType.HELL_BOSS)) {
            if (boss1.getBoolean("gotbreak")) {
                AnimationComponent anim = boss1.getComponent(AnimationComponent.class);
                if (FXGL.getGameTimer().getNow() - boss1.getDouble("breakstart") < 1.0) {
                    anim.playAttack(1.0);
                    continue;
                }
                else {
                    boss1.removeFromWorld();
                    continue;
                }
            }

            if (velocity.getX() < 0) {
                if (boss1.getX() <= getAppWidth() - 90) {
                    boss1.translateX(2);
                }
            }
            if (velocity.getX() > 0) {
                if (boss1.getX() >= 45) {
                    boss1.translateX(-2);
                }
            }

            if (boss1.getBoolean("gothit")) {
                AnimationComponent anim = boss1.getComponent(AnimationComponent.class);
                if (FXGL.getGameTimer().getNow() - boss1.getDouble("gothitstart") > 0.4) {
                    int dropChance = rand.nextInt(100);
                    if (dropChance < 10) {
                        // 20% rơi Heal Item
                        spawn("heal_item", boss1.getX() + 10, boss1.getY() + 110);
                    } else if (dropChance < 20) {
                        // 20% rơi Expand Item
                        spawn("expand_item", boss1.getX() + 43, boss1.getY() + 110);
                    } else if (dropChance < 30) {
                        spawn("ball_item", boss1.getX() + 43, boss1.getY() + 110);
                    } else if (dropChance < 40) {
                        spawn("paddlespeed_item", boss1.getX() + 43, boss1.getY() + 110);
                    } else if (dropChance < 50) {
                        spawn("ballspeed_item",  boss1.getX() + 43, boss1.getY() + 110);
                    }
                    if (rand.nextBoolean()) {
                        Entity fireball = spawn("fireball", boss1.getX() + 43, boss1.getY() + 110);
                        if (paddle.getX() < boss1.getX()) fireball.setProperty("velocity", new Point2D(-getBallSpeed() / 3, getBallSpeed() / 3));
                        else fireball.setProperty("velocity", new Point2D(getBallSpeed() / 3, getBallSpeed() / 3));
                        boss1.setProperty("gothit", false);
                        anim.playIdle();
                        continue;
                    }
                }
                anim.playRun();
            }
        }
        for (Entity fball : getGameWorld().getEntitiesByType(EntityType.FIRE_BALL)) {
            Point2D fvelocity = fball.getObject("velocity");
            fball.translate(fvelocity);
            PhysicsHandler.ball_wallCollision(fball, fvelocity);
        }
    }

    /**
     * Reset ball's position to a fixed point.
     * */
    private void resetBall() {
        // Tạo bóng mới dính vào paddle
        double ballX = paddle.getX() + paddle.getWidth() / 2.0 - Ball.BALL_SIZE / 2.0;
        double ballY = paddle.getY() - 30;
        Entity newBall = spawn("ball", ballX, ballY);

        // Bóng chưa bắn
        newBall.setProperty("velocity", Point2D.ZERO);
        newBall.setProperty("attachedToPaddle", true);
    }

    /**
     * Game launch.
     * */
    public static void main(String[] args) {
        launch(args);
    }

}