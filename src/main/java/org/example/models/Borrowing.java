package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.example.enums.BorrowingStatus;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Borrowing {
    private Long id;
    @NonNull
    private Long userId;
    @NonNull
    private Long gameId;
    @NonNull
    private LocalDateTime borrowDate;
    @NonNull
    private LocalDateTime dueDate;
    private LocalDateTime returnDate;
    @NonNull
    private BorrowingStatus status;
    private String notes;
}
