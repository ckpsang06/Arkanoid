package org.arkanoid.entity;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.Texture;
import javafx.scene.effect.MotionBlur;
import javafx.scene.effect.Effect;

public class Paddle extends MovableObject {
    private static final int PADDLE_WIDTH = 100;
    private static final int PADDLE_HEIGHT = 25;
    private static final int PADDLE_SPEED = 5;
    private String currentPowerUp;

<<<<<<< Updated upstream
=======
    private PhysicsComponent physics;
    private float speed = 0;
    private Vec2 velocity = new Vec2();
    private Effect blur = new MotionBlur();

    private Texture textureOnHit;

    public Paddle(int currentHp) {
        this.currentHp = currentHp;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public void setCurrentHp() {
        this.currentHp = currentHp;
    }

    public void loseHp(double amount) {
        currentHp -= amount;
    }

>>>>>>> Stashed changes
    public static int getPaddleWidth() {
        return PADDLE_WIDTH;
    }

    public static int getPaddleHeight() {
        return PADDLE_HEIGHT;
    }

    public static int getPaddleSpeed() {
        return PADDLE_SPEED;
    }

    public String getCurrentPowerUp() {
        return currentPowerUp;
    }

<<<<<<< Updated upstream
}
=======
    public void moveLeft() {

    }
    public void moveRight() {

    }
}
>>>>>>> Stashed changes
