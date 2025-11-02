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
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.arkanoid.component.AnimationComponent;
import org.arkanoid.entity.EntityType;
import org.arkanoid.component.HPComponent;

import java.awt.*;


public class GameEntityFactory implements EntityFactory {
    @Spawns("paddle")
    public Entity newPaddle(SpawnData data) {
        Image paddleImage = image("paddle.png");
        ImageView paddleView = new ImageView(paddleImage);

        paddleView.setFitWidth(getPaddleWidth());
        paddleView.setFitHeight(getPaddleHeight());

        return entityBuilder(data)
                .type(EntityType.PADDLE)
                .viewWithBBox(paddleView)
                .with("moveType", 0)
                .collidable()
                .build();
    }

    @Spawns("ball")
    public Entity newBall(SpawnData data) {
        Entity ball = entityBuilder(data)
                .type(EntityType.BALL)
                .viewWithBBox(new Circle(BALL_SIZE / 2, Color.BLUE))
                .collidable()
                .build();

        // Cờ kiểm tra xem bóng đã bắn chưa
        ball.setProperty("isLaunched", false);
        // Bóng chưa bắn thì chưa có vận tốc
        ball.setProperty("velocity", new Point2D(0, 0));

        return ball;
    }

    @Spawns("fireball")
    public Entity newFireBall(SpawnData data) {
        return entityBuilder(data)
                .type(EntityType.FIRE_BALL)
                .viewWithBBox(new Circle(BALL_SIZE/3, Color.RED))
                .collidable()
                .with("velocity", new Point2D(BALL_SPEED/5, BALL_SPEED/5))
                .build();
    }

    @Spawns("boss1")
    public Entity newHellBoss(SpawnData data) {
        AnimationComponent Boss1_texture = new AnimationComponent(
                "hell_boss.png", 15, 90, 120,
                1.0, 0, 0, 0.4, 1, 3,
                1.0, 4, 14);
        return entityBuilder(data)
                .type(EntityType.HELL_BOSS)
                .with(Boss1_texture)
                .bbox(new HitBox(BoundingShape.box(90, 120)))
                .collidable()
                .with(new HPComponent(100))
                .with("gothit", false)
                .with("gothitstart", 0.0)
                .with("gotbreak", false)
                .with("breakstart", 0.0)
                .build();
    }

    @Spawns("brick")
    public Entity newBrick(SpawnData data) {
        Image brickImage = image("brick_new.png");
        ImageView brickView = new ImageView(brickImage);

        brickView.setFitWidth(getBrickWidth());
        brickView.setPreserveRatio(true);

        return entityBuilder(data)
                .type(EntityType.BRICK)
                .viewWithBBox(brickView)
                .collidable()
                .with(new HPComponent(3))
                .build();
    }

    @Spawns("pink_brick")
    public Entity newBrickPink(SpawnData data) {
        Image brickImage = image("brick_pink.png");
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

    @Spawns("orange_brick")
    public Entity newBrickOrange(SpawnData data) {
        Image brickImage = image("brick_orange.png");
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

    @Spawns("green_brick")
    public Entity newBrickGreen(SpawnData data) {
        Image brickImage = image("brick_green.png");
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
        AnimationComponent healItemTexture = new AnimationComponent(
                "heal_item_animated.png",8,60,25,1.0,0,7,
                0,0,0,0,0,0);
        Image healItemImage = image("heal_item_animated.png");

        return entityBuilder(data)
                .type(EntityType.HEAL_ITEM)
                .with(healItemTexture)
                .bbox(new HitBox(BoundingShape.box(60,25)))
                .collidable()
                .build();
    }

    @Spawns("expand_item")
    public Entity newExpandItem(SpawnData data) {
        Image expandItemImage = image("expand_item.png");
        ImageView expandItemView = new ImageView(expandItemImage);

        expandItemView.setFitWidth(getPowerUpSize());
        expandItemView.setFitHeight(getPowerUpSize());

        return entityBuilder(data)
                .type(EntityType.EXPAND_ITEM)
                .viewWithBBox(expandItemView)
                .collidable()
                .build();
    }

    @Spawns("ball_item")
    public Entity newBallItem(SpawnData data) {
        Image ballItemImage = image("ball_item.png");
        ImageView ballItemView = new ImageView(ballItemImage);

        ballItemView.setFitWidth(getPowerUpSize());
        ballItemView.setFitHeight(getPowerUpSize());

        return entityBuilder(data)
                .type(EntityType.BALL_ITEM)
                .viewWithBBox(ballItemView)
                .collidable()
                .build();
    }

    @Spawns("ballspeed_item")
    public Entity newballspeedItem(SpawnData data) {
        Image image = image("ballspeed_item.png");
        ImageView view = new ImageView(image);

        double enlargedSize = getPowerUpSize() * 2.5;

        view.setFitWidth(enlargedSize);
        view.setFitHeight(enlargedSize);

        return entityBuilder(data)
                .type(EntityType.BALLSPEED_ITEM)
                .viewWithBBox(view)
                .collidable()
                .build();
    }

    @Spawns("paddlespeed_item")
    public Entity newpaddlespeedItem(SpawnData data) {
        Image image = image("paddlespeed_item.png");
        ImageView view = new ImageView(image);
        double enlargedSize = getPowerUpSize() * 2.5;
        view.setFitWidth(enlargedSize);
        view.setFitHeight(enlargedSize);

        return entityBuilder(data)
                .type(EntityType.PADDLESPEED_ITEM)
                .viewWithBBox(view)
                .collidable()
                .build();
    }

}