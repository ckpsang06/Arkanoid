package org.arkanoid.level;

import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.arkanoid.GameManager;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getAppWidth;

public class TutorialOverlay {
    public static void show() {
        VBox vbox = new VBox(8);
        vbox.setStyle("-fx-background-color: rgba(0,0,0,0.7); -fx-padding: 12; -fx-alignment: center;");
        vbox.setPrefWidth(460);
        vbox.setTranslateX(getAppWidth() / 2.0 - 230);
        vbox.setTranslateY(40);

        Text title = new Text("TUTORIAL");
        title.setStyle("-fx-font-size: 20px; -fx-fill: white;");
        Text line1 = new Text("Move left / right: A / D");
        Text line2 = new Text("Press SPACE to launch the ball. If ball falls you lose HP.");
        line1.setStyle("-fx-fill: white;");
        line2.setStyle("-fx-fill: white;");

        Button startBtn = new Button("Start");
        startBtn.setFocusTraversable(false);
        startBtn.setOnAction(e -> {
            FXGL.getGameScene().removeUINode(startBtn.getParent().getParent());
            GameManager.isPaused = false;
        });

        vbox.getChildren().addAll(title, line1, line2, startBtn);
        FXGL.getGameScene().addUINode(new StackPane(vbox));
    }

}
