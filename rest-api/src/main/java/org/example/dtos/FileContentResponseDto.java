package org.example.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class FileContentResponseDto {
    @NonNull
    private String name;

    @NonNull
    private String content;
}
