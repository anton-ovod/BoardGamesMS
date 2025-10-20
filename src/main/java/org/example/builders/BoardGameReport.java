package org.example.builders;

import lombok.Builder;
import lombok.Data;
import org.example.enums.BorrowingStatus;
import org.example.models.BoardGame;
import org.example.models.Borrowing;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class BoardGameReport {
    private BoardGame game;
    private List<Borrowing> borrowings;
    private LocalDateTime generatedAt;
    private String summary;

    public static class BoardGameReportBuilder {
        public BoardGameReport build() {
            long totalBorrowed = borrowings == null ? 0 : borrowings.size();
            long currentlyBorrowed = borrowings == null ? 0 :
                    borrowings.stream().filter(b -> b.getStatus() == BorrowingStatus.ACTIVE).count();

            String summary = String.format(
                    "Raport gry: %s\nKategoria: %s\nWydawca: %s\nOcena: %.1f\n" +
                            "Wypożyczeń ogółem: %d\nAktywnych: %d\nStatus: %s\nRaport wygenerowano: %s",
                    game.getTitle(),
                    game.getCategory(),
                    game.getPublisher(),
                    game.getRating(),
                    totalBorrowed,
                    currentlyBorrowed,
                    game.getStatus(),
                    LocalDateTime.now()
            );

            return new BoardGameReport(game, borrowings, LocalDateTime.now(), summary);
        }
    }
}

