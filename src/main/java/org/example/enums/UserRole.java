package org.example.enums;

/**
 * User roles in the system
 */
public enum UserRole {
    ADMIN("Administrator"),
    LIBRARIAN("Librarian"),
    MEMBER("Member");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
