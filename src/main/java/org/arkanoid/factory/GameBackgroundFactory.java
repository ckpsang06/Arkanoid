package org.arkanoid.factory;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getAppHeight;
import static com.almasb.fxgl.dsl.FXGLForKtKt.image;
import static org.arkanoid.factory.BackgroundType.*;

import com.almasb.fxgl.entity.Spawns;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class GameBackgroundFactory implements EntityFactory {
    @Spawns("grassBackground")
    public Entity newGrassBackground(SpawnData data) {
        Image grassImage = image("grass_background.png");
        ImageView grassView = new ImageView(grassImage);

        grassView.setFitWidth(getAppWidth());
        grassView.setFitHeight(getAppHeight());
        return entityBuilder(data)
                .type(GRASS)
                .view(grassView)
                .opacity(0.4)
                .build();
    }

    @Spawns("gridBackground")
    public Entity newGridBackground(SpawnData data) {
        Image gridImage = image("grid.png");
        ImageView gridView = new ImageView(gridImage);

        gridView.setFitWidth(getAppWidth());
        gridView.setFitHeight(getAppHeight());

        return entityBuilder(data)
                .type(GRID)
                .view(gridView)
                .opacity(0.7)
                .build();
    }

    @Spawns("forestBackground")
    public Entity newForestBackground(SpawnData data) {
        Image forestImage = image("forest_background.png");
        ImageView forestView = new ImageView(forestImage);

        forestView.setFitWidth(getAppWidth());
        forestView.setFitHeight(getAppHeight());
        return entityBuilder(data)
                .type(FOREST)
                .view(forestView)
                .build();
    }

    @Spawns("hellBackground")
    public Entity newHellBackground(SpawnData data) {
        Image hellImage = image("hell_background.png");
        ImageView hellView = new ImageView(hellImage);

        hellView.setFitWidth(getAppWidth());
        hellView.setFitHeight(getAppHeight());
        return entityBuilder(data)
                .type(HELL)
                .view(hellView)
                .build();
    }

    @Spawns("spaceBackground")
    public Entity newSpaceBackground(SpawnData data) {
        Image spaceImage = image("space_background.png");
        ImageView spaceView = new ImageView(spaceImage);

        spaceView.setFitWidth(getAppWidth());
        spaceView.setFitHeight(getAppHeight());
        return entityBuilder(data)
                .type(SPACE)
                .view(spaceView)
                .build();
    }
}