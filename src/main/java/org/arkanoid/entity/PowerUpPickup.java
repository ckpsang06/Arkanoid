package org.arkanoid.entity;

import com.almasb.fxgl.entity.Entity;

public class PowerUpPickup extends Entity {
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