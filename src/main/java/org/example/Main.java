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
        List<BoardGame> games = fileService.readBoardGamesIO();

        System.out.println("Available Board Games:");
        for (BoardGame game : games) {
            System.out.println(game);
        }

        // Example: Adding a new board game
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
    }
}