package org.example.models;

import org.example.enums.UserRole;

import java.util.concurrent.ThreadLocalRandom;

public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private UserRole role;

    public User(String firstName,
                String lastName,
                String email,
                UserRole role) {
        this.id = ThreadLocalRandom.current().nextLong();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
    }

    public Long getId()
    {
        return id;
    }

    public String getFullName()
    {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }
}
