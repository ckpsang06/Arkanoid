package org.arkanoid.utility;

import org.arkanoid.entity.Ball;
import org.arkanoid.entity.GameObject;
import org.arkanoid.entity.Paddle;

public abstract class PowerUp extends GameObject {
    private String type;

    public abstract void applyEffect(Paddle paddle, Ball ball);

    public abstract void removeEffect(Paddle paddle, Ball ball);
}
