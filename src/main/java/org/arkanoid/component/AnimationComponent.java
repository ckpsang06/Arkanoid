package org.arkanoid.component;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.scene.image.Image;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.getAssetLoader;

public class AnimationComponent extends Component {
    private AnimatedTexture texture;
    private AnimationChannel animIdle;
    private AnimationChannel animRun;
    private AnimationChannel animAttack;

    private enum State { IDLE, RUN, ATTACK }
    private State state = State.IDLE;
    private int facing = 1;

    public AnimationComponent() {
        Image image = getAssetLoader().loadImage("111.png");

        // Giả sử spritesheet 12 frame, 128x128 mỗi frame
        animIdle = new AnimationChannel(image, 28, 128, 128, Duration.seconds(1), 0, 6);
        animRun  = new AnimationChannel(image, 28, 128, 128, Duration.seconds(0.3), 7, 18);
        animAttack = new AnimationChannel(image, 28, 128, 128, Duration.seconds(0.8), 19, 27);
        texture = new AnimatedTexture(animIdle);
    }

    public AnimationComponent(
            String imageName,
            int frames,
            int frameWidth, int frameHeight,
            double idleDuration, int idleStart, int idleEnd,
            double runDuration, int runStart, int runEnd,
            double attackDuration, int attackStart, int attackEnd
    ) {
        Image image = getAssetLoader().loadImage(imageName);

        animIdle   = new AnimationChannel(image, frames, frameWidth, frameHeight, Duration.seconds(idleDuration), idleStart, idleEnd);
        animRun    = new AnimationChannel(image, frames, frameWidth, frameHeight, Duration.seconds(runDuration), runStart, runEnd);
        animAttack = new AnimationChannel(image, frames, frameWidth, frameHeight, Duration.seconds(attackDuration), attackStart, attackEnd);

        texture = new AnimatedTexture(animIdle);
    }

//    public AnimationComponent( );
    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
        texture.loopAnimationChannel(animIdle);
    }

    /**
     * Gọi khi entity bắt đầu chạy
     * */
    public void playRun() {
        if (state != State.RUN) {
            texture.loopAnimationChannel(animRun);
            state = State.RUN;
        }
    }

    /**
     * Gọi khi entity dừng lại
     * */
    public void playIdle() {
        if (state != State.IDLE) {
            texture.loopAnimationChannel(animIdle);
            state = State.IDLE;
        }
    }

    /**
     * Gọi khi entity tấn công
     * */
    public void playAttack(double attack_duration) {
        if (state != State.ATTACK) {
            texture.playAnimationChannel(animAttack);
            state = State.ATTACK;
            FXGL.getGameTimer().runOnceAfter(() -> playIdle(), Duration.seconds(attack_duration));
        }
    }

    public void faceRight() {
        facing = 1;
        texture.setScaleX(1);
    }

    public void faceLeft() {
        facing = -1;
        texture.setScaleX(-1);
    }

    /**
     * Dừng animation hẳn
     * */
    public void stop() {
        texture.stop();
    }

    public AnimatedTexture getTexture() {
        return texture;
    }

}
