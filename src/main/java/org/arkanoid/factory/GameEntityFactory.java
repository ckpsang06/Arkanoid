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
import org.arkanoid.entity_component.AnimationComponent;
import org.arkanoid.entity_component.HPComponent;
import org.arkanoid.entity.Powerup1;
import static com.almasb.fxgl.dsl.FXGL.*;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.texture.AnimatedTexture;
import javafx.util.Duration;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.BoundingShape;



import java.awt.*;


public class GameEntityFactory implements EntityFactory {
    @Spawns("paddle")
    public Entity newPaddle(SpawnData data) {

        AnimationComponent anim = new AnimationComponent();

        return entityBuilder(data)
                .type(EntityType.PADDLE)
                .bbox(new HitBox(BoundingShape.box(125, 10)))
                .collidable()
                .with(anim)
                .with("movetype", Integer.valueOf(0))
                .with("isattack", Boolean.FALSE)
                .with("attack_start_time", Double.valueOf(0))
                .with(new HPComponent(10))
                .build();
    }

    @Spawns("ball")
    public Entity newBall(SpawnData data) {
        Image B_image = image("ball.png");
        ImageView B_View = new ImageView(B_image);
        B_View.setFitWidth(getBallSize());
        B_View.setFitHeight(getBallSize());

        return entityBuilder(data)
                .type(EntityType.BALL)
                .viewWithBBox(B_View)
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
                .with(new HPComponent(3))
                .collidable()
                .build();
    }

    @Spawns("powerup1")
    public Entity newPowerup(SpawnData data) {
        Image powerupImage = image("powerup1.png");
        ImageView powerupView = new ImageView(powerupImage);

        powerupView.setFitWidth(Powerup1.getPower1_size());
        powerupView.setFitHeight(Powerup1.getPower1_size());

        return entityBuilder(data)
                .type(EntityType.POWERUP1)
                .viewWithBBox(powerupView)
                .with("heal_value", Powerup1.getPower1_heal_value())
                .collidable()
                .build();
    }

}
