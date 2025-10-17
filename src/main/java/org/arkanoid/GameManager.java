package org.arkanoid;

//fxgl
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
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;
import static com.almasb.fxgl.dsl.FXGL.spawn;
import static com.almasb.fxgl.dsl.FXGLForKtKt.*;
import static org.arkanoid.entity.Ball.*;
import static org.arkanoid.entity.Brick.getBrickSize;
import static org.arkanoid.entity.Paddle.getPaddleSpeed;
import static org.arkanoid.entity.Paddle.getPaddleWidth;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;
import static com.almasb.fxgl.dsl.FXGL.spawn;
import static com.almasb.fxgl.dsl.FXGLForKtKt.*;
import static org.arkanoid.entity.Ball.*;
import static org.arkanoid.entity.Brick.getBrickSize;
import static org.arkanoid.entity.Paddle.getPaddleSpeed;
import static org.arkanoid.entity.Paddle.getPaddleWidth;

//java
import java.time.Duration;
import java.util.Map;
import java.util.Random;

//package
import org.arkanoid.entity.*;
import org.arkanoid.factory.GameBackgroundFactory;
import org.arkanoid.factory.GameEntityFactory;
import org.arkanoid.entity_component.HPComponent;
import org.arkanoid.entity_component.AnimationComponent;




public class GameManager extends GameApplication {
    private Entity paddle;
    AnimationComponent p_anim;
    private Entity ball;
    Random rand = new Random();
//    private final GameEntityFactory entityFactory = new GameEntityFactory();

    /**
     * Manage all commands executed via inputs - keyboards in this case.
     * */
    @Override
    protected void initInput() {
        // IF D KEY IS PRESSED.
        FXGL.onKey(KeyCode.D, () -> {
            if (paddle.getRightX() < getAppWidth()) {
                paddle.translateX(getPaddleSpeed());
                paddle.setProperty("movetype", 1);
                FXGL.inc("pixelsMoved", +getPaddleSpeed());
                p_anim.faceRight();
            }
        });

        // IF A KEY IS PRESSED.
        FXGL.onKey(KeyCode.A, () -> {
            if (paddle.getX() > 0) {
                paddle.translateX(-getPaddleSpeed());
                paddle.setProperty("movetype", 2);
                FXGL.inc("pixelsMoved", -getPaddleSpeed());
                p_anim.faceLeft();
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
        getGameWorld().addEntityFactory(new GameBackgroundFactory());
        // BACKGROUND IMAGE - this doesn't work because of addUINode which will cover the entire screen.
        //  This remains to display how to achieve the above if needed. Deprecated for now.

//        Image grassBackground = image("grass_background.png");
//        ImageView grassBackgroundView = new ImageView(grassBackground);
//        grassBackgroundView.setFitHeight(getAppHeight());
//        grassBackgroundView.setFitWidth(getAppWidth());
//        getGameScene().addUINode(grassBackgroundView);

        spawn("grassBackground", 0, 0);

        paddle = spawn("paddle", 400, 400);
        p_anim = paddle.getComponent(AnimationComponent.class);
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
            //xu ly brick
            HPComponent hp = brick.getComponent(HPComponent.class);
            hp.damage(1);
            if(hp.getHP() <= 0) {
                Entity powerup1 = spawn("powerup1", brick.getX(), brick.getY());
                brick.removeFromWorld();
            }
            //sai so ngau nhien
            int randInt1 = rand.nextInt(1) - 1;
            int  randInt2 = rand.nextInt(1) - 1;

            //am thanh + score
            FXGL.inc("score", 1);
            FXGL.play("land.wav");

            Point2D velocity = ball.getObject("velocity");

            if (FXGLMath.randomBoolean()) {
                ball.setProperty("velocity", new Point2D(-velocity.getX() + randInt1, velocity.getY() + randInt2));
            } else {
                ball.setProperty("velocity", new Point2D(velocity.getX() + randInt1, -velocity.getY() + randInt2));
            }
            return null;
        });

        onCollisionBegin(EntityType.BALL, EntityType.PADDLE, (ball, paddle) -> {
            Point2D velocity1 = ball.getObject("velocity");
            paddle.setProperty("isattack",Boolean.TRUE);
            paddle.setProperty("attack_start_time", FXGL.getGameTimer().getNow());

            //sai so ngau nhien
            int randInt1 = rand.nextInt(1) - 1;
            int  randInt2 = rand.nextInt(1) - 1;

            // Reverse Y direction for horizontal paddle bounce
            Point2D newVelocity;
            if (paddle.getInt("movetype") == 1) {
                newVelocity = new Point2D(getBallSpeed() + randInt1, -velocity1.getY() + randInt2);
            } else if (paddle.getInt("movetype") == 2) {
                newVelocity = new Point2D(-getBallSpeed() + randInt1, -velocity1.getY() + randInt2);
            } else {
                int v_y = getBallSpeed();
                if (velocity1.getY() * v_y > 0) {
                    v_y = -v_y;
                }
                newVelocity = new Point2D(0 + randInt1, v_y * 1.41 + randInt2);
            }
            ball.setProperty("velocity", newVelocity);
            return null;
        });

        onCollisionBegin(EntityType.PADDLE, EntityType.POWERUP1, (paddle, powerup1) -> {
            HPComponent hp = paddle.getComponent(HPComponent.class);
            hp.heal(powerup1.getInt("heal_value"));
            powerup1.removeFromWorld();
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

//      Text textScore = getUIFactoryService().newText("", Color.BLACK, 22);
        Text textScore = new Text();
        textScore.setTranslateX(10);
        textScore.setTranslateY(20);

        Text textHP = new Text();
        textHP.setTranslateX(10);
        textHP.setTranslateY(30);

        textPixels.textProperty().bind(getWorldProperties().intProperty("pixelsMoved").asString());
        textScore.textProperty().bind(getWorldProperties().intProperty("score").asString());
        HPComponent hpComp = paddle.getComponent(HPComponent.class);
        textHP.textProperty().bind(hpComp.hpProperty().asString());

        getGameScene().addUINode(textPixels); // add to the scene graph
        getGameScene().addUINode(textScore);
        getGameScene().addUINode(textHP);

        // UI IMAGEs
        var computery = FXGL.getAssetLoader().loadTexture("computery.png");
        computery.setTranslateX((double) getAppWidth() / 2 - 100);
        computery.setTranslateY(20);
        getGameScene().addUINode(computery);
    }

    /**
     * Manage what happens in the game every tpf.
     * */
    @Override
    protected void onUpdate(double tpf) {
        Point2D velocity = ball.getObject("velocity");
        int ball_current_speed = (int)Math.sqrt(velocity.getX() * velocity.getX() + velocity.getY() * velocity.getY());
        if (ball_current_speed < getBallSpeed() * 1.41 || ball_current_speed > 5) {
                int v_y = getBallSpeed();
                if (velocity.getX() == 0) v_y *= 1.41;
                velocity = new Point2D(getBallSpeed() * Math.signum(velocity.getX()),
                        v_y * Math.signum(velocity.getY()));
        }
        ball.translate(velocity);

        double distance = paddle.getCenter().distance(ball.getCenter());


        if (FXGL.getGameTimer().getNow() - paddle.getDouble("attack_start_time") >= 0.8) {
            paddle.setProperty("isattack", Boolean.FALSE);
        }
        if (paddle.getBoolean("isattack")) {
            p_anim.playAttack(0.8);
        } else {
            if (paddle.getInt("movetype") == 0) {
                p_anim.playIdle();
            } else if (paddle.getInt("movetype") == 1 ||  paddle.getInt("movetype") == 2) {
                p_anim.playRun();
            }
        }


        paddle.setProperty("movetype", 0);
        for (Entity e : FXGL.getGameWorld().getEntities()) {
            if (e.getType() == EntityType.POWERUP1) {
                e.translateY(Powerup1.getPower1Speed());
            }
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
            HPComponent hpComp = paddle.getComponent(HPComponent.class);
            hpComp.damage(1);
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
