package org.arkanoid.factory;

import com.almasb.fxgl.dsl.FXGLForKtKt;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGLForKtKt.image;
import static org.arkanoid.BackgroundType.GRASS;
import static org.arkanoid.entity.Ball.*;
import static org.arkanoid.entity.Paddle.*;
import static org.arkanoid.entity.Brick.*;

import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.arkanoid.BackgroundType;
import org.arkanoid.entity.EntityType;

import java.awt.*;


public class GameBackgroundFactory implements EntityFactory {
    @Spawns("grassBackground")
    public Entity newGrassBackground(SpawnData data) {
        return entityBuilder(data)
                .type(GRASS)
                .view(new ImageView(image("grass_background.png")))
                .opacity(0.4)
                .build();
    }
}
