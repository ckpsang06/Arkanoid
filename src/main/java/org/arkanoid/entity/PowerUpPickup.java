package org.arkanoid.entity;

public class PowerUpPickup extends MovableObject {
    private static final int powerUpSize = 20;
    private static final int powerUpSpeed = 2;
    private static final int healItemValue = 1;

    public static int getPowerUpSize() {
        return powerUpSize;
    }

    public static int getPowerUpSpeed() {
        return powerUpSpeed;
    }

    public static int getHealItemValue() {
        return healItemValue;
    }
}