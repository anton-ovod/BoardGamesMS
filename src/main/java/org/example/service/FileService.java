package org.example.service;

import org.example.models.BoardGame;

import java.io.*;
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
}

