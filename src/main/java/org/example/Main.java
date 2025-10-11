package org.example;

import org.example.enums.Category;
import org.example.enums.GameStatus;
import org.example.models.BoardGame;
import org.example.service.FileService;

import java.util.*;

public class Main {
    public static void main(String[] args)
    {
        System.out.println("Welcome to the Board Game Library Management System!");

        FileService fileService = new FileService();

        // IO EXAMPLES OF READING AND WRITING BOARD GAMES
        List<BoardGame> games = fileService.readBoardGamesIO();

        System.out.println("[JAVA.IO] Available Board Games:");
        for (BoardGame game : games) {
            System.out.println(game);
        }

        // Adding a new board game
        BoardGame newGame = new BoardGame(
            "Dominion",
            "Deck-building game where players compete to build the best kingdom.",
            2,
            4,
            10,
            30,
            "Rio Grande Games",
            Category.STRATEGY,
            GameStatus.AVAILABLE,
            8.4
        );
        games.add(newGame);
        fileService.saveBoardGamesIO(games);

        // NIO EXAMPLES OF READING AND WRITING BOARD GAMES
        games = fileService.readBoardGamesNIO();
        System.out.println("[JAVA.NIO] Available Board Games:");
        for (BoardGame game : games) {
            System.out.println(game);
        }

        // Removing a board game by ID (for example, the one we just added)
        games.removeIf(g -> g.getId().equals(newGame.getId()));
        fileService.saveBoardGamesNIO(games);
    }
}