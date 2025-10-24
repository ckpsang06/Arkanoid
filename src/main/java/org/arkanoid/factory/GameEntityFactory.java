package org.arkanoid.factory;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGLForKtKt.image;
import static org.arkanoid.entity.Ball.*;
import static org.arkanoid.entity.Paddle.*;
import static org.arkanoid.entity.Brick.*;
import static org.arkanoid.entity.PowerUpPickup.*;


import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.arkanoid.entity.EntityType;
import org.arkanoid.component.HPComponent;

import java.awt.*;


public class GameEntityFactory implements EntityFactory {
    @Spawns("paddle")
    public Entity newPaddle(SpawnData data) {
        return entityBuilder(data)
                .type(EntityType.PADDLE)
                .viewWithBBox(new Rectangle(getPaddleWidth(), getPaddleHeight()))
                .with("moveType", Integer.valueOf(0))
                .collidable()
                .build();
    }

    @Spawns("ball")
    public Entity newBall(SpawnData data) {
        return entityBuilder(data)
                .type(EntityType.BALL)
                .viewWithBBox(new Circle(BALL_SIZE/2, Color.BLUE))
                .collidable()
                .with("velocity", new Point2D(BALL_SPEED, BALL_SPEED))
                .build();
    }

    @Spawns("brick")
    public Entity newBrick(SpawnData data) {
        Image brickImage = image("brick.png");
        ImageView brickView = new ImageView(brickImage);

        brickView.setFitWidth(getBrickWidth());
        brickView.setFitHeight(getBrickSize());

        return entityBuilder(data)
                .type(EntityType.BRICK)
                .viewWithBBox(brickView)
                .collidable()
                .with(new HPComponent(3))
                .build();
    }

    @Spawns("heal_item")
    public Entity newHealItem(SpawnData data) {
        Image healItemImage = image("heal_item.png");
        ImageView healItemView = new ImageView(healItemImage);

        healItemView.setFitWidth(getPowerUpSize());
        healItemView.setFitHeight(getPowerUpSize());

        return entityBuilder(data)
                .type(EntityType.HEAL_ITEM)
                .viewWithBBox(healItemView)
                .collidable()
                .build();
    }
}
