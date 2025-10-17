package org.arkanoid.entity;

public class Powerup1 extends MovableObject {
    private static final int power1_size = 20;
    private static final int Power1_SPEED = 2;
    private static final int Power1_heal_value = 1;

    public static int getPower1_size() {
        return power1_size;
    }

    public static int getPower1Speed() {
        return Power1_SPEED;
    }

    public static int getPower1_heal_value() {
        return Power1_heal_value;
    }
}