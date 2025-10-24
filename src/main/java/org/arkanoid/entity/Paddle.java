package org.arkanoid.entity;

public class Paddle extends MovableObject {
    private static final int PADDLE_WIDTH = 100;
    private static final int PADDLE_HEIGHT = 25;
    private static final int PADDLE_SPEED = 5;
    private static int currentHp = 3;
    private String currentPowerUp;

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

    public void moveLeft() {

    }

    public void moveRight() {

    }

}