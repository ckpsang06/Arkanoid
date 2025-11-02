package org.arkanoid.level;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.geometry.Point2D;
import org.arkanoid.GameManager;
import org.arkanoid.entity.EntityType;
import org.arkanoid.factory.GameBackgroundFactory;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

import static org.arkanoid.GameManager.isPaused;
// (ĐÃ XÓA) import static org.arkanoid.GameManager.ball;
// (ĐÃ XÓA) import static org.arkanoid.GameManager.ballLaunched;

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
    }

    public static void spawnBoss(int X, int Y) {
        spawn("boss1", X, Y);
    }

    public static void buildLevel(String bg, int rows, int cols, double startY, double startX) {
        setBackground(bg);
        double brickW = getBrickSize();
        double spacing = getBrickSize() + 5;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                spawn("brick", startX + c * (brickW + spacing), startY + r * (brickW + 12));
            }
        }
    }

    public static void buildLine(String bg, int count, double y, double startX) {
        setBackground(bg);
        double brickW = getBrickSize();
        double spacing = getBrickSize() + 5;
        for (int i = 0; i < count; i++) {
            spawn("brick", startX + i * (brickW + spacing), y);
        }
    }

    public static void buildLine(String bg, int count, double y, double startX, double _spacing, String brick) {
        setBackground(bg);
        double brickW = getBrickSize();
        double spacing = _spacing;
        for (int i = 0; i < count; i++) {
            spawn(brick, startX + i * (brickW + spacing), y);
        }
    }

    public static void buildLine(String bg, String brickName, int brickCount, double heightOffset, boolean screenSpread) {
        setBackground(bg);
        double spacing = getAppWidth() / (brickCount + 1);
        for (int i = 0; i < brickCount; i++) {
            spawn(brickName, spacing * (i + 1), heightOffset);
        }
    }

    public static void buildLine(String brickName, int brickCount, double heightOffset, boolean screenSpread) {
        double spacing = getAppWidth() / (brickCount + 1);
        for (int i = 0; i < brickCount; i++) {
            spawn(brickName, spacing * (i + 1), heightOffset);
        }
    }

    public static void spawnCoreEntities() {
        GameManager.paddle = spawn("paddle", getAppWidth() / 2.0 - 60, getAppHeight() - 120);
        Entity newBall = spawn("ball", getAppWidth() / 2.0, getAppHeight() / 2.0);
        newBall.setProperty("velocity", Point2D.ZERO);
        newBall.setProperty("attachedToPaddle", true);

    }

    public static void startTutorial(boolean showOverlay) {
        buildLevel("grassBackground", 2, 15, 100, 150);
        if (showOverlay) {
            TutorialOverlay.show();
            isPaused = true;
        } else {
            // Sửa lỗi: Tìm quả bóng đang dính vào paddle và "launch" nó
            var attachedBall = FXGL.getGameWorld()
                    .getEntitiesByType(EntityType.BALL) // Tìm tất cả các bóng
                    .stream()
                    .filter(b -> b.getBoolean("attachedToPaddle")) // Lọc ra bóng đang dính
                    .findFirst();

            if (attachedBall.isPresent()) {
                Entity ballToLaunch = attachedBall.get();
                ballToLaunch.setProperty("velocity", new Point2D(BALL_SPEED, BALL_SPEED));
                ballToLaunch.setProperty("attachedToPaddle", false); // Dùng logic mới
            }

            isPaused = false;
        }
    }

    public static void clearWorld() {
        FXGL.getGameWorld().getEntitiesCopy().forEach(Entity::removeFromWorld);
    }

}