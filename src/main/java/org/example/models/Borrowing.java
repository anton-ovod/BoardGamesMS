package org.example.models;

import org.example.enums.BorrowingStatus;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

public class Borrowing {
    private Long id;
    private Long userId;
    private Long gameId;
    private LocalDateTime borrowDate;
    private LocalDateTime dueDate;
    private LocalDateTime returnDate;
    private BorrowingStatus status;
    private String notes;

    public Borrowing(Long userId,
                     Long gameId,
                     LocalDateTime dueDate,
                     String notes) {
        this.id = ThreadLocalRandom.current().nextLong();
        this.userId = userId;
        this.gameId = gameId;
        this.dueDate = dueDate;
        this.notes = notes;
    }

    public Long getId()
    {
        return id;
    }

    public Long getUserId()
    {
        return userId;
    }

    public Long getGameId()
    {
        return gameId;
    }

    public LocalDateTime getBorrowDate()
    {
        return borrowDate;
    }

    public LocalDateTime getDueDate()
    {
        return dueDate;
    }

    public LocalDateTime getReturnDate()
    {
        return returnDate;
    }

    public BorrowingStatus getStatus()
    {
        return status;
    }

    public void setStatus(BorrowingStatus status)
    {
        this.status = status;
    }

    public String getNotes()
    {
        return notes;
    }

    public void setNotes(String notes)
    {
        this.notes = notes;
    }

    public boolean isOverdue() {
        return status == BorrowingStatus.ACTIVE &&
               LocalDateTime.now().isAfter(dueDate);
    }

    @Override
    public String toString() {
        return "Borrowing{" +
                "id=" + id +
                ", userId=" + userId +
                ", gameId=" + gameId +
                ", borrowDate=" + borrowDate +
                ", dueDate=" + dueDate +
                ", returnDate=" + returnDate +
                ", status=" + status +
                '}';
    }
}
