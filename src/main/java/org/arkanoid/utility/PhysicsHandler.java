package org.arkanoid.utility;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import javafx.geometry.Point2D;
import javafx.scene.text.Font;
import org.arkanoid.GameManager;
import org.arkanoid.component.HPComponent;
import org.arkanoid.entity.EntityType;
import com.almasb.fxgl.entity.Entity;
import static org.arkanoid.entity.Paddle.getPaddleSpeed;

import org.arkanoid.entity.PowerUpPickup;

import java.util.Random;

import static com.almasb.fxgl.dsl.FXGL.spawn;
import static com.almasb.fxgl.dsl.FXGLForKtKt.onCollisionBegin;
import static org.arkanoid.entity.Ball.getBallSpeed;
import static org.arkanoid.entity.Paddle.setPaddleSpeed;

public class PhysicsHandler {
    public static boolean ballSpeedActive = false;
    public static double ballSpeedMultiplier = 1.0;
    private static final java.util.Map<String, javafx.scene.text.Text> countdownMap = new java.util.HashMap<>();
    private static final java.util.Map<String, javafx.animation.Timeline> iconTimers = new java.util.HashMap<>();
    private static javafx.animation.Timeline ballSpeedTimer;
    private static boolean paddleSpeedActive = false;
    private static javafx.animation.Timeline paddleSpeedTimer;

    static Random rand = new Random();

    public static void ball_brickCollision() {
        onCollisionBegin(EntityType.BALL, EntityType.BRICK, (ball, brick) -> {
            // HP reduction for brick
            HPComponent hp = brick.getComponent(HPComponent.class);
            hp.damage(1);
            if (hp.getHP() <= 0) {
                brick.removeFromWorld();
                FXGL.play("brick_break.wav");

                // Xác suất rơi item
                // Xác suất rơi item
                int dropChance = rand.nextInt(100); // random 0 - 99

                if (dropChance < 10) {
                    spawn("heal_item", brick.getX(), brick.getY());
                } else if (dropChance < 20) {
                    spawn("expand_item", brick.getX(), brick.getY());
                } else if (dropChance < 60) {
                    spawn("ball_item", brick.getX(), brick.getY());
                } else if (dropChance < 70) {
                    spawn("ballspeed_item", brick.getX(), brick.getY());
                } else if (dropChance < 80) {
                    spawn("paddlespeed_item", brick.getX(), brick.getY());
                }
            }
            // Scoring and Brick Hitting
            FXGL.inc("score", 1);
            FXGL.play("land.wav");

            Point2D velocity = ball.getObject("velocity");

            // Hitting on the side.
            if (ball.getRightX() < brick.getX() || ball.getX() > brick.getRightX()) {
                ball.setProperty("velocity", new Point2D(-velocity.getX(), velocity.getY()));
            }

            // Hitting on top/bottom
            if (ball.getBottomY() > brick.getY() || ball.getY() < brick.getBottomY()) {
                ball.setProperty("velocity", new Point2D(velocity.getX(), -velocity.getY()));
            }

            return null;
        });
    }

    public static void ball_paddleCollision() {
        onCollisionBegin(EntityType.BALL, EntityType.PADDLE, (ball, paddle) -> {
            FXGL.play("paddle_land.wav");

            Point2D velocity1 = ball.getObject("velocity");

            double speed = velocity1.magnitude();

            if (speed < 0.1) {
                speed = Math.abs(velocity1.getX());
            }
            if (speed < 0.1) {
                speed = getBallSpeed(); // Dùng tốc độ mặc định
            }

            double ballCenter = ball.getCenter().getX();
            double paddleCenter = paddle.getCenter().getX();
            double paddleWidth = paddle.getWidth();

            double diff = ballCenter - paddleCenter;

            double normalizedDiff = diff / (paddleWidth / 2.0);

            normalizedDiff = Math.max(-1.0, Math.min(1.0, normalizedDiff));

            double maxBounceAngle = Math.PI / 3;
            double bounceAngle = normalizedDiff * maxBounceAngle;

            double newVelX = speed * Math.sin(bounceAngle);
            double newVelY = -speed * Math.cos(bounceAngle);

            if (ballSpeedActive) {
                newVelX *= ballSpeedMultiplier;
                newVelY *= ballSpeedMultiplier;
            }

            ball.setProperty("velocity", new Point2D(newVelX, newVelY));

            return null;
        });
    }

    public static void paddle_healItemCollision() {
        onCollisionBegin(EntityType.PADDLE, EntityType.HEAL_ITEM, (paddle, heal_item) -> {
            int HP = FXGL.geti("currentHP");
            FXGL.play("power_up.wav");
            if (HP < 5) {
                FXGL.inc("currentHP", +PowerUpPickup.getHealItemValue());
            }
            heal_item.removeFromWorld();
            return null;
        });
    }

    public static void paddle_expandItemCollision() {
        onCollisionBegin(EntityType.PADDLE, EntityType.EXPAND_ITEM, (paddle, expand_item) -> {
            FXGL.play("power_up.wav");
            showPowerUpIcon("expand_item", 8);
            var paddleView = (javafx.scene.image.ImageView) paddle.getViewComponent().getChildren().get(0);
            double currentWidth = paddleView.getFitWidth();
            double newWidth = currentWidth * 1.5;
            paddleView.setFitWidth(newWidth);
            paddle.getBoundingBoxComponent().clearHitBoxes();
            paddle.getBoundingBoxComponent().addHitBox(
                    new HitBox("main", BoundingShape.box(newWidth, paddleView.getFitHeight()))
            );
            expand_item.removeFromWorld();
            FXGL.runOnce(() -> {
                double reducedWidth = paddleView.getFitWidth() / 1.5;
                paddleView.setFitWidth(reducedWidth);

                paddle.getBoundingBoxComponent().clearHitBoxes();
                paddle.getBoundingBoxComponent().addHitBox(
                        new HitBox("main", BoundingShape.box(reducedWidth, paddleView.getFitHeight()))
                );
            }, javafx.util.Duration.seconds(8));

            return null;
        });
    }

    public static void paddle_ballItemCollision() {
        onCollisionBegin(EntityType.PADDLE, EntityType.BALL_ITEM, (paddle, ball_item) -> {
            FXGL.play("power_up.wav");

            // Lấy vị trí quả bóng GỐC (nếu có)
            Entity existingBall = FXGL.getGameWorld().getEntitiesByType(EntityType.BALL).get(0);
            Point2D originalVelocity = new Point2D(0, -getBallSpeed());
            Point2D ballPosition = new Point2D(paddle.getX() + paddle.getWidth() / 2, paddle.getY() - 30);

            if (existingBall != null) {
                originalVelocity = existingBall.getObject("velocity");
                ballPosition = existingBall.getPosition(); // Tạo bóng mới tại vị trí bóng cũ
            }

            // Tạo bóng mới tại vị trí bóng cũ (hoặc trên paddle)
            Entity newBall = spawn("ball", ballPosition.getX(), ballPosition.getY());

            // --- 🌟 SỬA LỖI VÀ XỬ LÝ TRƯỜNG HỢP 180 ĐỘ 🌟 ---

            double originalVx = originalVelocity.getX();
            double originalVy = originalVelocity.getY();

            double newVy = -Math.abs(originalVy);

            if (Math.abs(newVy) < 0.1) {
                newVy = -getBallSpeed();
            }

            double newVx = -originalVx;

            if (Math.abs(originalVx) < 0.1) {

                double splitSpeed = getBallSpeed() * 0.5; // Tốc độ tách ngang

                if (existingBall != null) {
                    existingBall.setProperty("velocity", new Point2D(-splitSpeed, newVy));
                }

                newVx = splitSpeed;

            } else {

                if (existingBall != null) {
                    existingBall.setProperty("velocity", new Point2D(originalVx, newVy));
                }
            }

            newBall.setProperty("velocity", new Point2D(newVx, newVy));
            newBall.setProperty("attachedToPaddle", false);

            // --- Hết phần sửa lỗi ---

            // Xóa item khỏi thế giới
            ball_item.removeFromWorld();
            return null;
        });
    }

    private static Font loadFont(String path, int size) {
        return Font.loadFont(PhysicsHandler.class.getResourceAsStream(path), size);
    }

    private static void showPowerUpIcon(String iconName, double durationSeconds) {
        var gameScene = FXGL.getGameScene();
        Font pixelmixFont = loadFont("/assets/ui/fonts/pixelmix.ttf", 30);

        if (countdownMap.containsKey(iconName)) {
            javafx.scene.text.Text existingText = countdownMap.get(iconName);
            existingText.setText(String.valueOf((int) durationSeconds));

            // Reset timer cũ nếu có
            if (iconTimers.containsKey(iconName)) {
                javafx.animation.Timeline oldTimer = iconTimers.get(iconName);
                oldTimer.stop();
                oldTimer.playFromStart();
            }
            return;
        }

        var icon = FXGL.getAssetLoader().loadTexture(iconName + ".png");
        icon.setFitWidth(50);
        icon.setFitHeight(50);
        icon.setTranslateX(20);
        icon.setTranslateY(10 + gameScene.getUINodes().size() * 30);

        javafx.scene.text.Text countdownText = new javafx.scene.text.Text(String.valueOf((int) durationSeconds));
        countdownText.setFill(javafx.scene.paint.Color.WHITE);
        countdownText.setStroke(javafx.scene.paint.Color.BLACK);
        countdownText.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 30));
        countdownText.setTranslateX(icon.getTranslateX() + 60);
        countdownText.setTranslateY(icon.getTranslateY() + 35);

        gameScene.addUINode(icon);
        gameScene.addUINode(countdownText);

        countdownMap.put(iconName, countdownText);

        // Tạo timer đếm ngược
        var timerAction = FXGL.getGameTimer().runAtInterval(() -> {
            int current = Integer.parseInt(countdownText.getText());
            if (current > 0) {
                countdownText.setText(String.valueOf(current - 1));
            }
        }, javafx.util.Duration.seconds(1));

        // Khi hết thời gian → xóa UI + countdown + timer
        javafx.animation.Timeline iconTimer = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(javafx.util.Duration.seconds(durationSeconds), e -> {
                    timerAction.expire();
                    gameScene.removeUINode(icon);
                    gameScene.removeUINode(countdownText);
                    countdownMap.remove(iconName);
                    iconTimers.remove(iconName);
                })
        );
        iconTimer.play();

        iconTimers.put(iconName, iconTimer);
    }

    public static void paddle_ballSpeedItemCollision() {
        onCollisionBegin(EntityType.PADDLE, EntityType.BALLSPEED_ITEM, (paddle, item) -> {
            FXGL.play("power_up.wav");

            // Nếu đang có hiệu ứng: reset lại thời gian đếm
            if (ballSpeedActive) {
                if (ballSpeedTimer != null) {
                    ballSpeedTimer.stop();
                    ballSpeedTimer.playFromStart();
                }

                showPowerUpIcon("ballspeed_item", 8);

                item.removeFromWorld();
                return null;
            }

            // Hiệu ứng bắt đầu
            ballSpeedActive = true;
            ballSpeedMultiplier = 1.25;
            showPowerUpIcon("ballspeed_item", 8);

            // Tăng tốc tất cả bóng hiện có
            FXGL.getGameWorld().getEntitiesByType(EntityType.BALL).forEach(ball -> {
                Point2D v = ball.getObject("velocity");
                ball.setProperty("velocity", v.multiply(ballSpeedMultiplier));
            });

            item.removeFromWorld();

            // Sau 8 giây, reset tốc độ về ban đầu
            ballSpeedTimer = new javafx.animation.Timeline(
                    new javafx.animation.KeyFrame(javafx.util.Duration.seconds(8), e -> {
                        ballSpeedActive = false;

                        FXGL.getGameWorld().getEntitiesByType(EntityType.BALL).forEach(ball -> {
                            Point2D v = ball.getObject("velocity");
                            // Nhân với nghịch đảo để khôi phục đúng tốc độ
                            ball.setProperty("velocity", v.multiply(1 / ballSpeedMultiplier));
                        });

                        ballSpeedMultiplier = 1.0;
                    })
            );
            ballSpeedTimer.play();

            return null;
        });
    }

    public static void paddle_paddleSpeedItemCollision() {
        onCollisionBegin(EntityType.PADDLE, EntityType.PADDLESPEED_ITEM, (paddle, item) -> {
            FXGL.play("power_up.wav");

            // Nếu đang có hiệu ứng paddle speed → reset thời gian và countdown
            if (paddleSpeedActive) {
                if (paddleSpeedTimer != null) {
                    paddleSpeedTimer.stop();
                    paddleSpeedTimer.playFromStart();
                }

                // Làm mới lại countdown UI
                showPowerUpIcon("paddlespeed_item", 8);

                item.removeFromWorld();
                return null;
            }

            // Bắt đầu hiệu ứng
            paddleSpeedActive = true;
            showPowerUpIcon("paddlespeed_item", 8);

            double oldSpeed = getPaddleSpeed();
            double boostedSpeed = oldSpeed * 1.5;
            setPaddleSpeed(boostedSpeed);

            item.removeFromWorld();

            // Sau 8 giây → trả về tốc độ cũ
            paddleSpeedTimer = new javafx.animation.Timeline(
                    new javafx.animation.KeyFrame(javafx.util.Duration.seconds(8), e -> {
                        setPaddleSpeed(oldSpeed);
                        paddleSpeedActive = false;
                    })
            );
            paddleSpeedTimer.play();

            return null;
        });
    }

    public static void fireball_paddleCollision() {
        onCollisionBegin(EntityType.FIRE_BALL, EntityType.PADDLE, (ball, paddle) -> {
            FXGL.play("paddle_land.wav");
            FXGL.inc("currentHP", -1);
            ball.removeFromWorld();
            return null;
        });
    }

    public static void ball_boss1Collision() {
        onCollisionBegin(EntityType.BALL, EntityType.HELL_BOSS, (ball, boss1) -> {
            boss1.setProperty("gothit", true);
            boss1.setProperty("gothitstart", FXGL.getGameTimer().getNow());
            HPComponent hp = boss1.getComponent(HPComponent.class);
            hp.damage(1);
            if (hp.getHP() <= 0) {
                boss1.setProperty("gotbreak", true);
                boss1.setProperty("breakstart", FXGL.getGameTimer().getNow());
            }
            Point2D velocity = ball.getObject("velocity");
            if (ball.getY() >= boss1.getY() || ball.getY() <= boss1.getY()) {
                ball.setProperty("velocity", new Point2D(velocity.getX(), -velocity.getY()));
            } else {
                ball.setProperty("velocity", new Point2D(-velocity.getX(), velocity.getY()));
            }
            return null;
        });
    }
}