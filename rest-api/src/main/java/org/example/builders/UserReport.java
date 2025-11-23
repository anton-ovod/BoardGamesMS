package org.example.builders;

import lombok.Builder;
import lombok.Data;
import org.example.enums.BorrowingStatus;
import org.example.models.Borrowing;
import org.example.models.User;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class UserReport {
    private User user;
    private List<Borrowing> borrowings;
    private LocalDateTime generatedAt;
    private String summary;

    public static class UserReportBuilder {
        public UserReport build() {
            int active = (int) borrowings.stream()
                    .filter(b -> b.getStatus() == BorrowingStatus.ACTIVE)
                    .count();

            String summary = String.format(
                    "Raport użytkownika: %s %s\nŁączna liczba wypożyczeń: %d\nAktywne: %d\nRaport wygenerowany: %s",
                    user.getFirstName(), user.getLastName(),
                    borrowings.size(), active, LocalDateTime.now()
            );

            return new UserReport(user, borrowings, LocalDateTime.now(), summary);
        }
    }
}

