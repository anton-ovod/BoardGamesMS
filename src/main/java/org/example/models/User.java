package org.example.models;

import jakarta.persistence.*;
import lombok.*;
import org.example.enums.UserRole;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @NonNull
    private String firstName;
    @Column(nullable = false)
    @NonNull
    private String lastName;
    @Column(nullable = false, unique = true)
    @NonNull
    private String email;
    @Column(nullable = false)
    @NonNull
    private UserRole role;
    @Column(nullable = false)
    @NonNull
    private Boolean active;

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
