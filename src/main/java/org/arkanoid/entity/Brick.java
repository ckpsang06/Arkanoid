package org.arkanoid.entity;

public class Brick extends GameObject {
    private static int BRICK_SIZE = 30;
    private int hitPoints;
    private String type;

    public int getHitPoints() {
        return hitPoints;
    }

    public static int getBrickSize() {
        return BRICK_SIZE;
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }


    public void setType(String type) {
        this.type = type;
    }

    public void takeHit() {

    }

    public void isDestroyed() {

    }
}
