package org.example.enums;

public enum BorrowingStatus {
    ACTIVE("Active"),
    RETURNED("Returned"),
    OVERDUE("Overdue"),
    LOST("Lost"),
    DAMAGED("Damaged");

    private final String displayName;

    BorrowingStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
