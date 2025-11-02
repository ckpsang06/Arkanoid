package org.arkanoid.utility;

import com.almasb.fxgl.dsl.FXGL;

public class GameState {
    public static void reset() {
        FXGL.getWorldProperties().doubleProperty("pixelsMoved").set(0.0);
        FXGL.getWorldProperties().intProperty("score").set(0);
        FXGL.getWorldProperties().intProperty("currentHP").set(3);
    }
}
