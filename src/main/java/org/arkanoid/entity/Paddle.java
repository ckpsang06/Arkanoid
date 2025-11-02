package org.arkanoid.entity;

public class Paddle extends MovableObject {

    private static final int PADDLE_WIDTH = 100;
    private static final int PADDLE_HEIGHT = 25;
    private static int currentHp = 3;
    private static final double PADDLE_SPEED_DEFAULT = 5.0;
    private static double currentPaddleSpeed = PADDLE_SPEED_DEFAULT;

    public Paddle(int currentHp) {
        this.currentHp = currentHp;
    }

    public static int getPaddleWidth() {
        return PADDLE_WIDTH;
    }

    public static int getPaddleHeight() {
        return PADDLE_HEIGHT;
    }

    public static double getPaddleSpeed() {
        return currentPaddleSpeed;
    }

    public static void setPaddleSpeed(double newSpeed) {
        currentPaddleSpeed = newSpeed;
    }

}