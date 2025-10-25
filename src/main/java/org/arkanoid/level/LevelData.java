package org.arkanoid.level;

public class LevelData {
    private final int id;
    private final String displayName;
    private final boolean locked;

    public LevelData(int id, String displayName, boolean locked) {
        this.id = id;
        this.displayName = displayName;
        this.locked = locked;
    }

    public int getId() { return id; }
    public String getDisplayName() { return displayName; }
    public boolean isLocked() { return locked; }

}
