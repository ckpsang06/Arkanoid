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
import static org.arkanoid.entity.Ball.BALL_SIZE;
import static org.arkanoid.entity.Ball.BALL_SPEED;
import static org.arkanoid.entity.Brick.getBrickSize;
import static org.arkanoid.entity.Paddle.getPaddleSpeed;


public class GameManager extends GameApplication {
//    private static final int PADDLE_WIDTH = 30;
//    private static final int PADDLE_HEIGHT = 100;
//    private static final int BALL_SIZE = 20;
//    private static final int PADDLE_SPEED = 5;
//    private static final int BALL_SPEED = 5;

    private Entity paddle;
    private Entity ball;
    private GameEntityFactory entityFactory = new GameEntityFactory();

    @Override
    protected void initInput() {
        FXGL.onKey(KeyCode.RIGHT, () -> {
            paddle.translateX(getPaddleSpeed());
            FXGL.inc("pixelsMoved", +getPaddleSpeed());
        });

        FXGL.onKey(KeyCode.LEFT, () -> {
            paddle.translateX(-getPaddleSpeed());
            FXGL.inc("pixelsMoved", -getPaddleSpeed());
        });
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(800);
        settings.setHeight(600);
        settings.setTitle("Arkanoid");
        settings.setVersion("0.1");
    }

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

    @Override
    protected void initPhysics() {
        onCollisionBegin(EntityType.BALL, EntityType.BRICK, (ball, brick) -> {
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
            Point2D velocity = ball.getObject("velocity");

            // Reverse Y direction for horizontal paddle bounce
            Point2D newVelocity = new Point2D(velocity.getX(), -Math.abs(velocity.getY()));
            ball.setProperty("velocity", newVelocity);
            return null;
        });
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("pixelsMoved", 0);
        vars.put("score", 0);
    }

    protected void initUI() {
        Text textPixels = new Text();
        textPixels.setTranslateX(20); // x = 50
        textPixels.setTranslateY(10); // y = 100

//        Text textScore = getUIFactoryService().newText("", Color.BLACK, 22);
        Text textScore = new Text();
        textScore.setTranslateX(100);
        textScore.setTranslateY(100);


        textPixels.textProperty().bind(getWorldProperties().intProperty("pixelsMoved").asString());
        textScore.textProperty().bind(getWorldProperties().intProperty("score").asString());

        getGameScene().addUINode(textPixels); // add to the scene graph
    }

//    @Override
//    protected void onUpdate(double tpf) {
//        Point2D velocity = ball.getObject("velocity");
//        ball.translate(velocity);
//
//        if (ball.getBottomY() == paddle.getY()
//                && ball.getX() > paddle.getRightX()
//                && ball.getRightX() < paddle.getX()) {
//            ball.setProperty("velocity", new Point2D(velocity.getX(), -velocity.getY()));
//        }
//
//        if (ball.getY() <= 0) {
//            resetBall();
//        }
//
//        if (ball.getY() >= getAppHeight()) {
//            resetBall();
//        }
//
//        if (ball.getX() <= 0) {
//            ball.setX(0);
//            ball.setProperty("velocity", new Point2D(-velocity.getX(), velocity.getY()));
//        }
//
//        if (ball.getRightX() >= getAppWidth()) {
//            ball.setX(getAppWidth() - BALL_SIZE);
//            ball.setProperty("velocity", new Point2D(-velocity.getX(), velocity.getY()));
//        }
//    }

    @Override
    protected void onUpdate(double tpf) {
        Point2D velocity = ball.getObject("velocity");
        ball.translate(velocity);

        if (ball.getBottomY() == paddle.getY()
                && ball.getX() < paddle.getRightX()
                && ball.getRightX() > paddle.getX()) {
            ball.setProperty("velocity", new Point2D(velocity.getX(), -velocity.getY()));
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

        if (ball.getBottomY() >= getAppHeight()) {
            ball.setY(getAppHeight() - BALL_SIZE);
            ball.setProperty("velocity", new Point2D(velocity.getX(), -velocity.getY()));
        }
    }

    private void resetBall() {
        ball.setPosition((double) getAppWidth() / 2 - (double) BALL_SIZE / 2, (double) getAppHeight() / 2 - (double) BALL_SIZE / 2);
        ball.setProperty("velocity", new Point2D(BALL_SPEED, BALL_SPEED));
    }

    public static void main(String[] args) {
        launch(args);
    }

}
