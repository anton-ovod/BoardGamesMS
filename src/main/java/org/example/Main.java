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
import org.example.modules.users.UserService;
import org.example.service.FileService;
import org.example.service.StreamService;
import org.example.modules.boardgames.BoardGameService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.time.LocalDateTime;
import java.util.*;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to the Board Game Library Management System!");

        ApplicationContext context = SpringApplication.run(Main.class, args);

        StreamService streamService = new StreamService();
        BoardGameService boardGameservice = context.getBean(BoardGameService.class);
        UserService userService = context.getBean(UserService.class);

        // READ using java.io
        //MB try-catch needed
        List<BoardGame> games = FileService.readBoardGamesIO();
        System.out.println("Read " + games.size() + " games from file.");

        // ADD new games
        BoardGame newGame = new BoardGame(
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
        List<BoardGame> filtered = streamService.filterByRating(games, 8.0);
        System.out.println("\nFiltered games (rating >= 8.0):");
        filtered.forEach(g -> System.out.println(" - " + g.getTitle() + " (" + g.getRating() + ")"));

        // SORT games by rating descending
        List<BoardGame> sorted = streamService.sortByRatingDescending(filtered);
        System.out.println("\nSorted by rating (descending):");
        sorted.forEach(g -> System.out.println(" - " + g.getTitle() + " (" + g.getRating() + ")"));

        // MAP get titles only
        List<String> titles = streamService.mapToTitles(sorted);
        System.out.println("\nTitles of sorted games:");
        titles.forEach(System.out::println);

        // COUNT games per category
        Map<Category, Long> counts = streamService.countCategoryOccurrences(games);
        System.out.println("\nCategory counts:");
        counts.forEach((cat, count) -> System.out.println(cat + ": " + count));

        // SAVE to file using java.nio
        FileService.saveBoardGamesNIO(sorted);
        System.out.println("\nSaved filtered & sorted games to file.");



        // XML SERVICE TESTING

        List<BoardGame> gamesForXML = List.of(
                new BoardGame("Catan", "Build", 3, 4, 10, 90, "Cosmos", Category.STRATEGY, GameStatus.AVAILABLE, 8.5),
                new BoardGame("Carcassonne", "BuildBuildBuild", 2, 5, 8, 45, "Hans im Glück", Category.FAMILY, GameStatus.AVAILABLE, 8.2)
        );


        //WRITE to BoardGame XML
        // try catch
        FileService.saveBoardGamesXML(gamesForXML);
        System.out.println("✅ Data saved into XML.");


        //READ from BoardGame XML
        List<BoardGame> loaded = FileService.readBoardGamesXML();
        System.out.println("✅ Data read from XML:");
        loaded.forEach(System.out::println);

        // Example usage of builders classes

        User user = new User("Jan", "Kowalski", "jan.kowalski@example.com", UserRole.MEMBER, true);
        BoardGame game = new BoardGame("Catan", "Handel i rozwój osad", 3, 4, 10, 90, "Kosmos",
                Category.STRATEGY, GameStatus.AVAILABLE, 8.6);
        Borrowing borrowing = new Borrowing(100L, 1L, 10L,
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



        game = boardGameservice.create(game);
        System.out.println("Created game: " + game.getTitle());

        user = userService.create(user);
        System.out.println("Created user: " + user.getFullName());

        System.out.println("\nAll games:");
        boardGameservice.readAll().forEach(g ->
                System.out.println("Found game: " + g.getTitle()));

        System.out.println("\nAll users:");
        userService.readAll().forEach(u ->
                System.out.println("Found user: " + u.getFullName() + ", email: " + u.getEmail()));

        BoardGame readGame = boardGameservice.read(game.getId());
        System.out.println("Read single game: " + readGame.getTitle() + ", rating: " + readGame.getRating());

        User readUser = userService.read(user.getId());
        System.out.println("Read single user: " + readUser.getFullName() + ", email: " + readUser.getEmail());

        game.setRating(9.5);
        game = boardGameservice.update(game);
        System.out.println("Updated game rating to: " + game.getRating());

        user.setEmail("alice.new@example.com");
        user = userService.update(user);
        System.out.println("Updated user email to: " + user.getEmail());

        boardGameservice.delete(game.getId());
        System.out.println("Deleted game: " + game.getTitle());

        userService.delete(user.getId());
        System.out.println("Deleted user: " + user.getFullName());

        System.out.println("\nRemaining games:");
        boardGameservice.readAll().forEach(g -> System.out.println("Remaining game: " + g.getTitle()));

        System.out.println("\nRemaining users:");
        userService.readAll().forEach(u -> System.out.println("Remaining user: " + u.getFullName()));






        //SAFE/HARD delete test

        User user1 = new User(null, "Alice", "Smith", "alice.smith@example.com", UserRole.MEMBER, true);
        User user2 = new User(null, "Bob", "Johnson", "bob.johnson@example.com", UserRole.MEMBER, true);

        user1 = userService.create(user1);
        user2 = userService.create(user2);

        System.out.println("Created users:");
        userService.readAll().forEach(u -> System.out.println(u.getFullName() + " | active=" + u.getActive()));

        user1.setActive(false);
        userService.update(user1);

        System.out.println("\nAfter soft delete (inactive) of Alice:");
        userService.readAll().forEach(u -> System.out.println(u.getFullName() + " | active=" + u.getActive()));

        userService.delete(user2.getId());

        System.out.println("\nAfter hard delete of Bob:");
        userService.readAll().forEach(u -> System.out.println(u.getFullName() + " | active=" + u.getActive()));


    }
}