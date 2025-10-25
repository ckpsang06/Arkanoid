package org.arkanoid.level;

import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.arkanoid.GameManager;

import java.util.List;

public class LevelUIFactory {
    public static VBox createLevelMenu(List<LevelData> levels) {
        VBox vbox = new VBox(12);
        vbox.setStyle("-fx-alignment: center; -fx-padding: 20; -fx-background-color: rgba(0,0,0,0.6);");
        vbox.setPrefSize(420, 300);

        Text title = new Text("SELECT LEVEL");
        title.setStyle("-fx-font-size: 26px; -fx-fill: white;");
        vbox.getChildren().add(title);

        for (LevelData level : levels) {
            Button btn = new Button(level.getDisplayName());
            btn.setPrefWidth(300);
            btn.setDisable(level.isLocked());
            btn.setOnAction(e -> {
                FXGL.getGameScene().removeUINode(vbox.getParent());
                GameManager.isPaused = false;
                LevelManager.startLevel(level.getId());
            });
            vbox.getChildren().add(btn);
        }

        Button backBtn = new Button("Back to Menu");
        backBtn.setPrefWidth(300);
        backBtn.setOnAction(e -> {
            FXGL.getGameScene().removeUINode(vbox.getParent());
            FXGL.getGameController().gotoMainMenu();
        });
        vbox.getChildren().add(backBtn);

        return vbox;
    }

}
