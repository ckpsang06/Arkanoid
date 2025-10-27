package org.arkanoid.entity;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getAppHeight;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getAppWidth;

public class Ball extends MovableObject {
    public static int BALL_SPEED = 3;
    public static final int BALL_SIZE = 15;
    private int directionX;
    private int directionY;

    public static int getBallSpeed() {
        return BALL_SPEED;
    }

    public static int getBallSize() {
        return BALL_SIZE;
    }

    public int getDirectionX() {
        return directionX;
    }

    public int getDirectionY() {
        return directionY;
    }

    public void setSpeed(int speed) { BALL_SPEED = speed;
    }

    public void bounceOff(GameObject other) {

    }

    public void checkCollision(GameObject other) {

    }



//    private void resetBall() {
//        setPosition(getAppWidth() / 2 - BALL_SIZE / 2, getAppHeight() / 2 - BALL_SIZE / 2);
//        setProperty("velocity", new Point2D(BALL_SPEED, BALL_SPEED));
//    }
}
