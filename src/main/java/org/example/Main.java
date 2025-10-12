package org.example;

import org.example.enums.Category;
import org.example.enums.GameStatus;
import org.example.models.BoardGame;
import org.example.service.BoardGameXMLService;
import org.example.service.FileService;
import org.example.service.StreamService;

import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Welcome to the Board Game Library Management System!");

        FileService fileService = new FileService();
        StreamService streamService = new StreamService();

        // READ using java.io
        List<BoardGame> games = fileService.readBoardGamesIO();
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
        fileService.saveBoardGamesNIO(sorted);
        System.out.println("\nSaved filtered & sorted games to file.");



        /// XML SERVICE TESTING

        List<BoardGame> gamesForXML = List.of(
                new BoardGame("Catan", "Build", 3, 4, 10, 90, "Cosmos", Category.STRATEGY, GameStatus.AVAILABLE, 8.5),
                new BoardGame("Carcassonne", "BuildBuildBuild", 2, 5, 8, 45, "Hans im Glück", Category.FAMILY, GameStatus.AVAILABLE, 8.2)
        );

        //READ from BoardGame XML
        List<BoardGame> loaded = BoardGameXMLService.readFromXML();
        System.out.println("✅ Data read from XML:");
        loaded.forEach(System.out::println);

        //WRITE to BoardGame XML
        BoardGameXMLService.writeToXML(gamesForXML);
        System.out.println("✅ Data saved into XML.");


    }
}