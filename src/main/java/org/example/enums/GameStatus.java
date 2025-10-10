package org.example.enums;

/**
 * Game status in the system
 */
public enum GameStatus {
    AVAILABLE("Available"),
    BORROWED("Borrowed"),
    RESERVED("Reserved"),
    MAINTENANCE("Maintenance"),
    LOST("Lost"),
    DAMAGED("Damaged");

    private final String displayName;

    GameStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
