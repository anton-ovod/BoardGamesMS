package org.example.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class AuthRequestDto {
    @NonNull
    private String email;

    @NonNull
    private String password;
}
