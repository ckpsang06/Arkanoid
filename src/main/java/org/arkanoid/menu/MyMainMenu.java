package org.arkanoid.menu;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class MyMainMenu extends FXGLMenu {

    public MyMainMenu() {
        super(MenuType.MAIN_MENU);

        Button btnStart = new Button("START");
        btnStart.setOnAction(e -> fireNewGame());

        Button btnExit = new Button("EXIT");
        btnExit.setOnAction(e -> fireExit());

        VBox vbox = new VBox(20, btnStart, btnExit);
        vbox.setTranslateX(600);
        vbox.setTranslateY(400);

        getContentRoot().getChildren().add(vbox);
    }
}
