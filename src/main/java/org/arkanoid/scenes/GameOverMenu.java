package org.arkanoid.scenes;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.awt.*;

public class GameOverMenu extends FXGLMenu {

    public GameOverMenu(MenuType type) {
        super(type);

        VBox box = new VBox(15);
        box.setAlignment(Pos.CENTER);

        Text title = new Text("Game Over");
        title.setFont(Font.font(36));
        title.setFill(Color.BLUE);

        Button restartBtn = FXGL.getUIFactoryService().newButton("Restart");
        restartBtn.setOnAction(e -> fireNewGame());

        Button exitBtn = FXGL.getUIFactoryService().newButton("Exit");
        exitBtn.setOnAction(e -> fireExitToMainMenu());

        box.getChildren().addAll(title, restartBtn, exitBtn);
        getContentRoot().getChildren().add(box);
    }
}