package org.example;

import org.example.builders.BoardGameReport;
import org.example.builders.BorrowingReceipt;
import org.example.builders.UserReport;
import org.example.dtos.BoardGameBackupDto;
import org.example.dtos.BorrowingBackupDto;
import org.example.dtos.UserBackupDto;
import org.example.enums.BorrowingStatus;
import org.example.enums.Category;
import org.example.enums.GameStatus;
import org.example.enums.UserRole;
import org.example.models.BoardGame;
import org.example.models.Borrowing;
import org.example.models.User;
import org.example.service.FileService;
import org.example.service.StreamService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.*;


@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to the Board Game Library Management System!");

        // READ using java.io
        List<BoardGame> games = FileService.readBoardGamesIO();
        System.out.println("Read " + games.size() + " games from file.");

        // ADD new games
        BoardGame newGame = new BoardGame(
                games.size() + 1L,
                "The Crew",
                "Cooperative trick-taking space adventure.",
                2,
                5,
                10,
                20,
                "Kosmos",
                Category.COOPERATIVE,
                GameStatus.AVAILABLE,
                8.3
        );
        games.add(newGame);
        System.out.println("Added new game: " + newGame.getTitle());

        // FILTER get games with rating >= 8.0
        List<BoardGame> filtered = StreamService.filterByRating(games, 8.0);
        System.out.println("\nFiltered games (rating >= 8.0):");
        filtered.forEach(g -> System.out.println(" - " + g.getTitle() + " (" + g.getRating() + ")"));

        // SORT games by rating descending
        List<BoardGame> sorted = StreamService.sortByRatingDescending(filtered);
        System.out.println("\nSorted by rating (descending):");
        sorted.forEach(g -> System.out.println(" - " + g.getTitle() + " (" + g.getRating() + ")"));

        // MAP get titles only
        List<String> titles = StreamService.mapToTitles(sorted);
        System.out.println("\nTitles of sorted games:");
        titles.forEach(System.out::println);

        // COUNT games per category
        Map<Category, Long> counts = StreamService.countCategoryOccurrences(games);
        System.out.println("\nCategory counts:");
        counts.forEach((cat, count) -> System.out.println(cat + ": " + count));

        // SAVE to file using java.nio
        FileService.saveBoardGamesNIO(sorted);

        // XML SERVICE TESTING

        //READ from BoardGame XML
        List<BoardGame> xmlGames = FileService.readBoardGamesXML();
        System.out.println("\n✅ Data read from XML:");
        xmlGames.forEach(System.out::println);

        // ADD new games to list
        List<BoardGame> newGames = List.of(
                new BoardGame(xmlGames.size() + 1L, "Gloomhaven", "Fantasy adventure board game.", 1, 4, 14, 120, "Cephalofair Games", Category.ADVENTURE, GameStatus.AVAILABLE, 9.0),
                new BoardGame(xmlGames.size() + 2L, "Pandemic", "Cooperative disease-fighting game", 2, 4, 8, 60, "Gamewright", Category.COOPERATIVE, GameStatus.AVAILABLE, 8.4)
        );
        xmlGames.addAll(newGames);

        //WRITE to BoardGame XML
        FileService.saveBoardGamesXML(xmlGames);
        System.out.println("✅ Data saved into XML.");

        // Example usage of builders classes

        User user = new User(1L,"Jan", "Kowalski", "jan.kowalski@example.com", UserRole.MEMBER, true);
        BoardGame game = new BoardGame(1L, "Catan", "Handel i rozwój osad", 3, 4, 10, 90, "Kosmos",
                Category.STRATEGY, GameStatus.AVAILABLE, 8.6);
        Borrowing borrowing = new Borrowing(100L,
                1L, null,
                1L, null,
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now().plusDays(5),
                null, BorrowingStatus.ACTIVE, "Brak uwag");

        BorrowingReceipt receipt = BorrowingReceipt.builder()
                .user(user)
                .game(game)
                .borrowing(borrowing)
                .build();

        BoardGameReport gameReport = BoardGameReport.builder()
                .game(game)
                .borrowings(List.of(borrowing))
                .build();

        UserReport userReport = UserReport.builder()
                .user(user)
                .borrowings(List.of(borrowing))
                .build();

        System.out.println("\n--- Borrowing Receipt ---");
        System.out.println(receipt.getSummary());
        System.out.println("\n--- Board Game Report ---");
        System.out.println(gameReport.getSummary());
        System.out.println("\n--- User Report ---");
        System.out.println(userReport.getSummary());

        // Backup Dtos examples

        System.out.println("\n--- BoardGame Backup DTO ---");
        var boardGameBackup = new BoardGameBackupDto(game);

        game.setTitle("Catan - Edycja Specjalna");
        System.out.println("After modification: " + game);

        game = boardGameBackup.toEntity();
        System.out.println("After restoring from backup: " + game);

        System.out.println("\n--- Borrowing Backup DTO ---");
        var borrowingBackup = new BorrowingBackupDto(borrowing);

        borrowing.setStatus(BorrowingStatus.RETURNED);
        System.out.println("After modification: " + borrowing);

        borrowing = borrowingBackup.toEntity();
        System.out.println("After restoring from backup: " + borrowing);

        System.out.println("\n--- User Backup DTO ---");
        var userBackup = new UserBackupDto(user);

        user.setEmail("aftermodification@gmail.com");
        System.out.println("After modification: " + user);

        user = userBackup.toEntity();
        System.out.println("After restoring from backup: " + user.toString());

        SpringApplication.run(Main.class, args);
    }
}