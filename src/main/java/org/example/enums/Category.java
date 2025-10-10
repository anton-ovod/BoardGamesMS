package org.example.enums;

/**
 * Board game categories
 */
public enum Category {
    STRATEGY("Strategic"),
    PARTY("Party"),
    FAMILY("Family"),
    CARD_GAME("Card Game"),
    COOPERATIVE("Cooperative"),
    ABSTRACT("Abstract"),
    THEMATIC("Thematic"),
    PUZZLE("Puzzle"),
    DICE("Dice"),
    EDUCATIONAL("Educational"),
    ADVENTURE("Adventure"),
    ECONOMIC("Economic"),
    WAR_GAME("War Game"),
    TRIVIA("Trivia");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
