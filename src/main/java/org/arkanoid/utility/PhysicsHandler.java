package org.arkanoid.utility;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import javafx.geometry.Point2D;
import org.arkanoid.GameManager;
import org.arkanoid.component.HPComponent;
import org.arkanoid.entity.EntityType;
import org.arkanoid.entity.PowerUpPickup;

import java.util.Random;

import static com.almasb.fxgl.dsl.FXGL.spawn;
import static com.almasb.fxgl.dsl.FXGLForKtKt.onCollisionBegin;
import static org.arkanoid.entity.Ball.getBallSpeed;

public class PhysicsHandler {
    static Random rand = new Random();
    public static void ball_brickCollision() {
        onCollisionBegin(EntityType.BALL, EntityType.BRICK, (ball, brick) -> {
            // HP reduction for brick
            HPComponent hp = brick.getComponent(HPComponent.class);
            hp.damage(1);
            if (hp.getHP() <= 0) {
                brick.removeFromWorld();
                FXGL.play("brick_break.wav");

                // Xác suất rơi item
                int dropChance = rand.nextInt(100); // random 0 - 99

                if (dropChance < 20) {
                    // 20% rơi Heal Item
                    spawn("heal_item", brick.getX(), brick.getY());
                } else if (dropChance < 40) {
                    // 20% rơi Expand Item
                    spawn("expand_item", brick.getX(), brick.getY());
                }
            }
            // Scoring and Brick Hitting
            FXGL.inc("score", 1);
            FXGL.play("land.wav");

            Point2D velocity = ball.getObject("velocity");
            if (ball.getY() > brick.getY() || ball.getY() < brick.getY()) {
                ball.setProperty("velocity", new Point2D(velocity.getX(), -velocity.getY()));
            } else {
                ball.setProperty("velocity", new Point2D(-velocity.getX(), velocity.getY()));
            }
            return null;
        });
    }

    public static void ball_paddleCollision() {
        // BALL - PADDLE COLLISION
        onCollisionBegin(EntityType.BALL, EntityType.PADDLE, (ball, paddle) -> {
            FXGL.play("paddle_land.wav");
            Point2D velocity1 = ball.getObject("velocity");
            // Reverse Y direction for horizontal paddle bounce
            Point2D newVelocity;
            if (GameManager.movetype == 1) {
                newVelocity = new Point2D(getBallSpeed(), -velocity1.getY());
            } else if (GameManager.movetype == 2) {
                newVelocity = new Point2D(-getBallSpeed(), -velocity1.getY());
            } else {
                newVelocity = new Point2D(0, -getBallSpeed() * 1.41);
            }
            ball.setProperty("velocity", newVelocity);
            return null;
        });
    }

    public static void paddle_healItemCollision() {
        onCollisionBegin(EntityType.PADDLE, EntityType.HEAL_ITEM, (paddle, heal_item) -> {
            int HP = FXGL.geti("currentHP");
            FXGL.play("power_up.wav");
            if (HP < 5) {
                FXGL.inc("currentHP", +PowerUpPickup.getHealItemValue());
            }
            heal_item.removeFromWorld();
            return null;
        });
    }

    public static void paddle_expandItemCollision() {
        onCollisionBegin(EntityType.PADDLE, EntityType.EXPAND_ITEM, (paddle, expand_item) -> {
            FXGL.play("power_up.wav");
            var paddleView = (javafx.scene.image.ImageView) paddle.getViewComponent().getChildren().get(0);
            double currentWidth = paddleView.getFitWidth();
            double newWidth = currentWidth * 1.5;
            paddleView.setFitWidth(newWidth);
            paddle.getBoundingBoxComponent().clearHitBoxes();
            paddle.getBoundingBoxComponent().addHitBox(
                    new HitBox("main", BoundingShape.box(newWidth, paddleView.getFitHeight()))
            );
            expand_item.removeFromWorld();
            FXGL.runOnce(() -> {
                double reducedWidth = paddleView.getFitWidth() / 1.5;
                paddleView.setFitWidth(reducedWidth);

                paddle.getBoundingBoxComponent().clearHitBoxes();
                paddle.getBoundingBoxComponent().addHitBox(
                        new HitBox("main", BoundingShape.box(reducedWidth, paddleView.getFitHeight()))
                );
            }, javafx.util.Duration.seconds(8));

            return null;
        });
    }
}
