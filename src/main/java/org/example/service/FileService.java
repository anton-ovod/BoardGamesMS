package org.example.service;

import org.example.models.BoardGame;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileService
{
    private static final String FILE_PATH = "src/main/resources/boardgames.txt";
    private static final String HEADER = "id|title|description|minPlayers|maxPlayers|recommendedAge|playingTimeMinutes|publisher|category|status|rating";

    public List<BoardGame> readBoardGamesIO() {
        List<BoardGame> games = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            boolean header = true;
            while ((line = reader.readLine()) != null) {
                if (header)
                {
                    header = false;
                    continue;
                }
                String[] parts = line.split("\\|");

                BoardGame g = new BoardGame(parts);
                games.add(g);
            }

            System.out.println("✅ Data read using java.io successfully!");
        }
        catch(FileNotFoundException e) {
            System.out.println("❌ File not found: " + FILE_PATH);
            System.err.println(e.getMessage());
        }
        catch (IOException e) {
            System.out.println("❌ Error reading file: " + FILE_PATH);
            System.err.println(e.getMessage());
        }

        return games;
    }

    public void saveBoardGamesIO(List<BoardGame> games) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(FILE_PATH)))) {
                writer.write(HEADER);
                writer.newLine();

            for (BoardGame g : games) {
                writer.write(g.toFileString());
                writer.newLine();
            }

            System.out.println("✅ Data saved using java.io successfully!");
        } catch (IOException e) {
            System.out.println("❌ Error writing to file: " + FILE_PATH);
            System.err.println(e.getMessage());
        }
    }

    public List<BoardGame> readBoardGamesNIO() {
        List<BoardGame> games = new ArrayList<>();

        try{
            Path path = Paths.get(FILE_PATH);
            List<String> lines = Files.readAllLines(path);
            for(int i = 1; i < lines.size(); i++) // skip header
            {
                String[] parts = lines.get(i).split("\\|");

                BoardGame g = new BoardGame(parts);
                games.add(g);
            }

            System.out.println("✅ Data read using java.nio successfully!");
        }
        catch (InvalidPathException e) {
            System.out.println("❌ File not found: " + FILE_PATH);
            System.err.println(e.getMessage());
        }
        catch (IOException e) {
            System.out.println("❌ Error reading file: " + FILE_PATH);
            System.err.println(e.getMessage());
        }

        return games;
    }

    public void saveBoardGamesNIO(List<BoardGame> games) {
        List<String> lines = new ArrayList<>();
        lines.add(HEADER);
        for (BoardGame g : games) {
            lines.add(g.toFileString());
        }

        try {
            Path path = Paths.get(FILE_PATH);
            Files.write(path, lines);

            System.out.println("✅ Data saved using java.nio successfully!");
        } catch (InvalidPathException e) {
            System.out.println("❌ File not found: " + FILE_PATH);
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("❌ Error writing to file: " + FILE_PATH);
            System.err.println(e.getMessage());
        }
    }
}

