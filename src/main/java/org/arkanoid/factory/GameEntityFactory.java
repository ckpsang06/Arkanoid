package org.arkanoid.factory;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGLForKtKt.image;
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
import org.arkanoid.entity.EntityType;

import java.awt.*;


public class GameEntityFactory implements EntityFactory {
    @Spawns("paddle")
    public Entity newPaddle(SpawnData data) {
        return entityBuilder(data)
                .type(EntityType.PADDLE)
                .viewWithBBox(new Rectangle(getPaddleWidth(), getPaddleHeight()))
                .collidable()
                .build();
    }

    @Spawns("ball")
    public Entity newBall(SpawnData data) {
        return entityBuilder(data)
                .type(EntityType.BALL)
                .viewWithBBox(new Rectangle(BALL_SIZE, BALL_SIZE, Color.BLUE))
                .collidable()
                .with("velocity", new Point2D(BALL_SPEED, BALL_SPEED))
                .build();
    }

    @Spawns("brick")
    public Entity newBrick(SpawnData data) {
        Image brickImage = image("brick.png");
        ImageView brickView = new ImageView(brickImage);

        brickView.setFitWidth(getBrickSize());
        brickView.setFitHeight(getBrickSize());

        return entityBuilder(data)
                .type(EntityType.BRICK)
                .viewWithBBox(brickView)
                .collidable()
                .build();
    }
}
