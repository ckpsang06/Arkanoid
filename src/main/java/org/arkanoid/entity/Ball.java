package org.arkanoid.entity;

public class Ball extends MovableObject {
    public static int BALL_SPEED = 3;
    public static final int BALL_SIZE = 15;

    public static int getBallSpeed() {
        return BALL_SPEED;
    }
    public static int getBallSize() {
        return BALL_SIZE;
    }
}
