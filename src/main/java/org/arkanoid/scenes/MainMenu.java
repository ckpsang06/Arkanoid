package org.arkanoid.scenes;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import org.arkanoid.utility.MusicHandler;
import org.jetbrains.annotations.NotNull;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import java.awt.*;

import static com.almasb.fxgl.dsl.FXGL.getAppWidth;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getAppHeight;
import static com.almasb.fxgl.dsl.FXGLForKtKt.image;

public class MainMenu extends FXGLMenu {
    public MainMenu(MenuType type) {
        super(type);
        MusicHandler.playMenuMusic();

        // Title
//        Text title = new Text("Arkanoid");
//        title.setFont(Font.font("Arial", FontWeight.BOLD, 108));
//        title.setFill(Color.RED);
        Image title = image("arkanoid_title.png");
        ImageView titleView = new ImageView(title);
        titleView.setFitWidth(600);
        titleView.setPreserveRatio(true);

        TranslateTransition floatAnim = new TranslateTransition(Duration.seconds(2.5), titleView);
        floatAnim.setByY(-20);// Move up by 20 pixels
        floatAnim.setAutoReverse(true); // Reverse the direction
        floatAnim.setCycleCount(TranslateTransition.INDEFINITE); // Loop forever
        floatAnim.play();

        Image curtain = image("curtain_mainmenu.png");
        ImageView curtainView = new ImageView(curtain);
        Image dropShadow = image("dropshadow.png");
        ImageView dropShadowView = new ImageView(dropShadow);
        dropShadowView.setOpacity(0.5);

        StackPane topPane = new StackPane(titleView);
        topPane.setPadding(new Insets(20, 0, 0, 0));
        topPane.setAlignment(Pos.TOP_CENTER);

        // Buttons
        Button btnStart = FXGL.getUIFactoryService().newButton("Start Game");
        btnStart.setOnAction(e -> {
            FXGL.play("game_start.wav");
            MusicHandler.playLevelMusic();
            fireNewGame();
        });

        Button btnOptions = FXGL.getUIFactoryService().newButton("Options");
        btnOptions.setOnAction(e -> {
            showOptionsMenu();
        });

        Button btnCredits = FXGL.getUIFactoryService().newButton("Credits");

        Button btnExit = FXGL.getUIFactoryService().newButton("Exit");
        btnExit.setOnAction(e -> fireExit());

        VBox buttonBox = new VBox(20, btnStart, btnOptions, btnCredits, btnExit);
        buttonBox.setAlignment(Pos.CENTER);

        StackPane bottomPane = new StackPane(buttonBox);
        bottomPane.setPadding(new Insets(0, 0, 60, 0));
        bottomPane.setAlignment(Pos.BOTTOM_CENTER);

        // Layout
        BorderPane layout = new BorderPane();
        layout.setTop(topPane);
        layout.setBottom(bottomPane);

        layout.setPrefSize(getAppWidth(), getAppHeight());
        StackPane wrapper = new StackPane(layout);
        wrapper.setPrefSize(FXGL.getAppWidth(), FXGL.getAppHeight());

        javafx.scene.shape.Rectangle dim = new Rectangle(getAppWidth(), getAppHeight());
        dim.setFill(javafx.scene.paint.Color.color(0, 0, 0, 0.6));

        getContentRoot().getChildren().addAll(curtainView, dropShadowView);
        getContentRoot().getChildren().add(wrapper);
    }

    private void showOptionsMenu() {
        VBox optionsBox = new VBox(15);
        optionsBox.setAlignment(Pos.CENTER);
        optionsBox.setTranslateX(FXGL.getAppWidth() / 2 - 100);
        optionsBox.setTranslateY(FXGL.getAppHeight() / 2 - 100);

        Label title = new Label("Options");

        CheckBox soundToggle = new CheckBox("Enable Sound");
        soundToggle.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                // bật lại nhạc
                FXGL.getSettings().setGlobalMusicVolume(1);
            } else {
                // tắt nhạc
                FXGL.getSettings().setGlobalMusicVolume(0);
            }
        });

        ComboBox<String> difficultyBox = new ComboBox<>();
        difficultyBox.getItems().addAll("Easy", "Normal", "Hard");
        difficultyBox.setValue("Normal");

        Button backButton = FXGL.getUIFactoryService().newButton("Back");
        backButton.setOnAction(e -> getContentRoot().getChildren().remove(optionsBox));

        optionsBox.getChildren().addAll(title, soundToggle, difficultyBox, backButton);

        getContentRoot().getChildren().add(optionsBox);
    }


}
