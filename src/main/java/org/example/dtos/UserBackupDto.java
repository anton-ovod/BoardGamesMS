package org.example.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.enums.UserRole;
import org.example.models.User;

@Data
@AllArgsConstructor
public class UserBackupDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private UserRole role;
    private Boolean active;

    public UserBackupDto(User user)
    {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.active = user.getActive();
    }

    public User toEntity()
    {
        return new User(
                id,
                firstName,
                lastName,
                email,
                role,
                active
        );
    }
}
