package org.example.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.example.enums.BorrowingStatus;
import org.example.models.Borrowing;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class BorrowingBackupDto {
    @NonNull
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

    public BorrowingBackupDto(Borrowing borrowing)
    {
        this.id = borrowing.getId();
        this.userId = borrowing.getUserId();
        this.gameId = borrowing.getGameId();
        this.borrowDate = borrowing.getBorrowDate();
        this.dueDate = borrowing.getDueDate();
        this.returnDate = borrowing.getReturnDate();
        this.status = borrowing.getStatus();
        this.notes = borrowing.getNotes();
    }

    public Borrowing toEntity()
    {
        return new Borrowing(
                id,
                userId,
                gameId,
                borrowDate,
                dueDate,
                returnDate,
                status,
                notes
        );
    }
}
