package org.arkanoid.component;

import com.almasb.fxgl.entity.component.Component;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class HPComponent extends Component {
    private final IntegerProperty hp; // HP mặc định
    public HPComponent() {
        hp = new SimpleIntegerProperty(1);
    }

    public HPComponent(int hp) {
        this.hp = new SimpleIntegerProperty(hp);
    }

    public IntegerProperty hpProperty() {
        return hp;
    }

    public int getHP() {
        return hp.get();
    }

    public void setHP(int value) {
        hp.set(value);
    }

    public void damage(int amount) {
        hp.set(hp.get() - amount);
    }

    public void heal(int amount) {
        hp.set(hp.get() + amount);
    }
}