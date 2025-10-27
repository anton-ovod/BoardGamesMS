package org.example.models;

import lombok.*;
import org.example.enums.UserRole;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class User {
    private Long id;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @NonNull
    private String email;
    @NonNull
    private UserRole role;
    @NonNull
    private Boolean active;

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
