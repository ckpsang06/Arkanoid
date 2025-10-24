package org.arkanoid.utility;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
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
            if(hp.getHP() <= 0) {
                if (rand.nextBoolean()) spawn("heal_item", brick.getX(), brick.getY());
                brick.removeFromWorld();
                FXGL.play("brick_break.wav");
            }

            // Scoring and Brick Hitting
            FXGL.inc("score", 1);
            FXGL.play("land.wav");

            Point2D velocity = ball.getObject("velocity");
            if (FXGLMath.randomBoolean()) {
                ball.setProperty("velocity", new Point2D(-velocity.getX(), velocity.getY()));
            } else {
                ball.setProperty("velocity", new Point2D(velocity.getX(), -velocity.getY()));
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
}
