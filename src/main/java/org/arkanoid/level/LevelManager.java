package org.arkanoid.level;

import org.arkanoid.GameManager;
import org.arkanoid.utility.GameState;

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

        switch (levelId) {
            case 1:
                LevelBuilder.startTutorial(true);
                break;
            case 2:
                LevelBuilder.buildLevel("hellBackground", 3, 12, 140, 40);
                break;
            case 3:
                LevelBuilder.setBackground("forestBackground");
                break;
            default:
                LevelBuilder.buildLine("spaceBackground", 15, 200, 60);
                break;
        }

        LevelBuilder.spawnCoreEntities();
        GameManager.isPaused = false;
    }

}
