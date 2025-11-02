package org.arkanoid.level;

import com.almasb.fxgl.dsl.FXGL;
import org.arkanoid.GameManager;
import org.arkanoid.entity.Brick;
import org.arkanoid.utility.GameState;
import org.arkanoid.utility.MusicHandler;

import java.util.List;

public class LevelManager {
    public static List<LevelData> getAvailableLevels() {
        return List.of(
                new LevelData(1, "Level 1 — Tutorial", false),
                new LevelData(2, "Level 2", false),
                new LevelData(3, "Level 3", false),
                new LevelData(4, "Level 4", false)
        );
    }

    public static void startLevel(int levelId) {
        GameState.reset();
        LevelBuilder.clearWorld();

        LevelSelector.inSelection = false;

        switch (levelId) {
            case 1:
                LevelBuilder.startTutorial(true);
                break;
            case 2:
                LevelBuilder.buildLine("spaceBackground", "pink_brick", 12, 100, true);
                LevelBuilder.buildLine("pink_brick", 6, 150, true);
                LevelBuilder.buildLine("pink_brick", 3, 200, true);
                LevelBuilder.buildLine("pink_brick", 12, 250, true);
                MusicHandler.playSpaceMusic();
                break;
            case 3:
                LevelBuilder.buildLine("forestBackground", "green_brick", 10, 50, true);
                LevelBuilder.buildLine("green_brick", 5, 100, true);
                LevelBuilder.buildLine("green_brick", 10, 150, true);
                LevelBuilder.buildLine("green_brick", 5, 200, true);
                MusicHandler.playForestMusic();
                break;
            default:
                LevelBuilder.setBackground("hellBackground");
                LevelBuilder.buildColumn("orange_brick", 5, 40, 200);
                LevelBuilder.buildColumn("orange_brick", 5,
                        FXGL.getAppWidth() - Brick.getBrickSize() - 60, 200);

                LevelBuilder.buildColumn("orange_brick", 3, 120, 260);
                LevelBuilder.buildColumn("orange_brick", 3,
                        FXGL.getAppWidth() - Brick.getBrickSize() - 140, 260);

                LevelBuilder.buildLine(10, 300, 200, 60, "orange_brick");

                LevelBuilder.spawnBoss(600, 50);
                MusicHandler.playHellMusic();
                break;
        }

        LevelBuilder.spawnCoreEntities();
        GameManager.isPaused = false;
    }

}
