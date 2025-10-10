package org.example.model;

import org.example.enums.BorrowingStatus;

import java.time.LocalDateTime;

public class Borrowing {
    private Long id;
    private Long userId;
    private Long gameId;
    private LocalDateTime borrowDate;
    private LocalDateTime dueDate;
    private LocalDateTime returnDate;
    private BorrowingStatus status;
    private String notes;

    public Borrowing() {
        this.borrowDate = LocalDateTime.now();
        this.status = BorrowingStatus.ACTIVE;
    }

    public Borrowing(Long userId, Long gameId, LocalDateTime dueDate) {
        this();
        this.userId = userId;
        this.gameId = gameId;
        this.dueDate = dueDate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public LocalDateTime getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDateTime borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public BorrowingStatus getStatus() {
        return status;
    }

    public void setStatus(BorrowingStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isOverdue() {
        return status == BorrowingStatus.ACTIVE &&
               dueDate != null &&
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
