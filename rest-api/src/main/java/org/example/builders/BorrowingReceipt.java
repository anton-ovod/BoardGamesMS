package org.example.builders;

import lombok.Builder;
import lombok.Data;
import org.example.models.BoardGame;
import org.example.models.Borrowing;
import org.example.models.User;

import java.time.format.DateTimeFormatter;

@Data
@Builder
public class BorrowingReceipt {
    private String receiptNumber;
    private User user;
    private BoardGame game;
    private Borrowing borrowing;
    private String summary;

    public static class BorrowingReceiptBuilder {
        public BorrowingReceipt build() {
            String receiptId = "RCPT-" + borrowing.getId();
            String summary = String.format(
                    "Potwierdzenie wypożyczenia #%s\nUżytkownik: %s %s\nGra: %s\nData wypożyczenia: %s\nTermin zwrotu: %s\nStatus: %s",
                    borrowing.getId(),
                    user.getFirstName(), user.getLastName(),
                    game.getTitle(),
                    borrowing.getBorrowDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                    borrowing.getDueDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                    borrowing.getStatus()
            );
            return new BorrowingReceipt(receiptId, user, game, borrowing, summary);
        }
    }
}
