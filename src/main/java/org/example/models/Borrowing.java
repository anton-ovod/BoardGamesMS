package org.example.models;

import jakarta.persistence.*;
import lombok.*;
import org.example.enums.BorrowingStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "borrowings")
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class Borrowing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NonNull
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="game_id", nullable = false)
    @NonNull
    private BoardGame game;

    @Column(nullable = false)
    @NonNull
    private LocalDateTime borrowDate;

    @Column(nullable = false)
    @NonNull
    private LocalDateTime dueDate;

    @Column
    private LocalDateTime returnDate;

    @Column(nullable = false)
    @NonNull
    private BorrowingStatus status;

    @Column
    private String notes;
}
