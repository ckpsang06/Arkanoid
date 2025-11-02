package org.arkanoid.entity;

import com.almasb.fxgl.entity.Entity;

public class Brick extends Entity {
    private static int BRICK_SIZE = 30;
    private static int BRICK_WIDTH = 60;

    public static int getBrickSize() {
        return BRICK_SIZE;
    }

    public static int getBrickWidth() {
        return BRICK_WIDTH;
    }
}
