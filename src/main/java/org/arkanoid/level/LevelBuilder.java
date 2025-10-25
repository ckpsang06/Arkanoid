package org.arkanoid.level;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.geometry.Point2D;
import org.arkanoid.GameManager;
import org.arkanoid.factory.GameBackgroundFactory;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;
import static org.arkanoid.GameManager.ball;
import static org.arkanoid.GameManager.isPaused;
import static org.arkanoid.GameManager.ballLaunched;
import static org.arkanoid.entity.Ball.BALL_SPEED;
import static org.arkanoid.entity.Brick.getBrickSize;

public class LevelBuilder {
    private static Entity currentBgEntity = null;

    public static void setBackground(String textureName) {
        try {
            if (currentBgEntity != null) {
                currentBgEntity.removeFromWorld();
                currentBgEntity = null;
            }
        } catch (Exception ignore) {}

        spawn(textureName, 0, 0);

//        var tex = FXGL.getAssetLoader().loadTexture(textureName);
//        tex.setFitWidth(getAppWidth());
//        tex.setFitHeight(getAppHeight());
//        tex.setViewOrder(1000.0);
//        FXGL.entityBuilder().view(tex).buildAndAttach();
    }

    public static void buildLevel(String bg, int rows, int cols, double startY, double startX) {
        setBackground(bg);
        double brickW = getBrickSize();
        double spacing = 18;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                spawn("brick", startX + c * (brickW + spacing), startY + r * (brickW + 12));
            }
        }
    }

    public static void buildLine(String bg, int count, double y, double startX) {
        setBackground(bg);
        double brickW = getBrickSize();
        double spacing = 18;
        for (int i = 0; i < count; i++) {
            spawn("brick", startX + i * (brickW + spacing), y);
        }
    }

    public static void spawnCoreEntities() {
        GameManager.paddle = spawn("paddle", getAppWidth() / 2.0 - 60, getAppHeight() - 120);
        ball = spawn("ball", getAppWidth() / 2.0, getAppHeight() / 2.0);
        ball.setProperty("velocity", Point2D.ZERO);
        GameManager.ballLaunched = false;
    }

    public static void startTutorial(boolean showOverlay) {
        spawnCoreEntities();
        if (showOverlay) {
            TutorialOverlay.show();
            isPaused = true;
        } else {
            ball.setProperty("velocity", new Point2D(BALL_SPEED, BALL_SPEED));
            ballLaunched = true;
            isPaused = false;
        }
    }

    public static void clearWorld() {
        FXGL.getGameWorld().getEntitiesCopy().forEach(Entity::removeFromWorld);
    }

}
