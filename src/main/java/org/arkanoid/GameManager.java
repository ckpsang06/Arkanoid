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
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import java.util.Map;

import org.arkanoid.entity.*;
import org.arkanoid.factory.GameEntityFactory;

import static com.almasb.fxgl.dsl.FXGL.getAppWidth;
import static com.almasb.fxgl.dsl.FXGL.spawn;
import static com.almasb.fxgl.dsl.FXGLForKtKt.*;
import static org.arkanoid.entity.Ball.*;
import static org.arkanoid.entity.Brick.getBrickSize;
import static org.arkanoid.entity.Paddle.getPaddleSpeed;
import static org.arkanoid.entity.Paddle.getPaddleWidth;


public class GameManager extends GameApplication {
    private Entity paddle;
    int movetype = 0;
    private Entity ball;
    private final GameEntityFactory entityFactory = new GameEntityFactory();

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
        settings.setVersion("0.1");
//        settings.setGameMenuEnabled(true);
    }

    /**
     * Manage the game's initial settings.
     * */
    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new GameEntityFactory());
        paddle = spawn("paddle", 400, 500);
        ball = spawn("ball", getAppWidth() / 2, getAppHeight() / 2);
        for (int i = 0; i < 10; i++) {
            spawn("brick", getAppWidth() / 2 - 200 - getBrickSize(), 100 + i*(getBrickSize() + 20));
            spawn("brick", getAppWidth() / 2 + 200, 100 + i*(getBrickSize() + 20));
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

        // IMAGEs
        var computery = FXGL.getAssetLoader().loadTexture("computery.png");
//        var brick = FXGL.getAssetLoader().loadTexture("brick.png");
        computery.setTranslateX(getAppWidth() / 2 - 100);
        computery.setTranslateY(20);
//        brick.setTranslateX(getAppWidth() - 100);
//        brick.setTranslateY(100);
        getGameScene().addUINode(computery);
//        getGameScene().addUINode(brick);
    }

    /**
     * Manage what happens in the game every tpf.
     * */
    @Override
    protected void onUpdate(double tpf) {
        Point2D velocity = ball.getObject("velocity");
        ball.translate(velocity);

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

        if (ball.getBottomY() >= getAppHeight()) {
            ball.setY(getAppHeight() - BALL_SIZE);
            ball.setProperty("velocity", new Point2D(velocity.getX(), -velocity.getY()));
        }
    }

    /**
     * Reset ball's position to a fixed point.
     * */
    private void resetBall() {
        ball.setPosition((double) getAppWidth() / 2 - (double) BALL_SIZE / 2, (double) getAppHeight() / 2 - (double) BALL_SIZE / 2);
        ball.setProperty("velocity", new Point2D(BALL_SPEED, BALL_SPEED));
    }

    /**
     * Game launch.
     * */
    public static void main(String[] args) {
        launch(args);
    }

}
