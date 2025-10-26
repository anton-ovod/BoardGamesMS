package org.example.service;

import org.example.enums.Category;
import org.example.enums.GameStatus;
import org.example.models.BoardGame;

import javax.xml.stream.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileService
{
    private static final String INPUT_FILE_PATH = "src/main/resources/boardgames.txt";
    private static final String OUTPUT_FILE_PATH = "src/main/resources/boardgames-modified.txt";
    private static final String INPUT_FILE_PATH_XML = "src/main/resources/boardgames.xml";
    private static final String OUTPUT_FILE_PATH_XML = "src/main/resources/boardgames-modified.xml";
    private static final String HEADER = "id|title|description|minPlayers|maxPlayers|recommendedAge|playingTimeMinutes|publisher|category|status|rating";

    public static List<BoardGame> readBoardGamesIO() {
        List<BoardGame> games = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(INPUT_FILE_PATH))) {
            String line;
            boolean header = true;
            while ((line = reader.readLine()) != null) {
                if (header)
                {
                    header = false;
                    continue;
                }
                String[] parts = line.split("\\|");
                if (parts.length < 11) {
                    throw new IllegalArgumentException("Invalid data format for BoardGame: expected 11 fields, got " + parts.length);
                }

                BoardGame g = new BoardGame(parts);
                games.add(g);
            }

            System.out.println("✅ Data read using java.io successfully!");
        }
        catch(FileNotFoundException e) {
            System.out.println("❌ File not found: " + INPUT_FILE_PATH);
            System.err.println(e.getMessage());
        }
        catch(IllegalArgumentException e) {
            System.out.println("❌ Data format error in file: " + INPUT_FILE_PATH);
            System.err.println(e.getMessage());
        }
        catch (IOException e) {
            System.out.println("❌ Error reading file: " + INPUT_FILE_PATH);
            System.err.println(e.getMessage());
        }

        return games;
    }

    public static void saveBoardGamesIO(List<BoardGame> games) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(OUTPUT_FILE_PATH)))) {
                writer.write(HEADER);
                writer.newLine();

            for (BoardGame g : games) {
                writer.write(g.toFileString());
                writer.newLine();
            }

            System.out.println("✅ Data saved using java.io successfully!");
        } catch (IOException e) {
            System.out.println("❌ Error writing to file: " + INPUT_FILE_PATH);
            System.err.println(e.getMessage());
        }
    }

    public static List<BoardGame> readBoardGamesNIO() {
        List<BoardGame> games = new ArrayList<>();

        try{
            Path path = Paths.get(INPUT_FILE_PATH);
            List<String> lines = Files.readAllLines(path);
            for(int i = 1; i < lines.size(); i++)
            {
                String[] parts = lines.get(i).split("\\|");
                if (parts.length < 11) {
                    throw new IllegalArgumentException("Invalid data format for BoardGame: expected 11 fields, got " + parts.length);
                }

                BoardGame g = new BoardGame(parts);
                games.add(g);
            }

            System.out.println("✅ Data read using java.nio successfully!");
        }
        catch (InvalidPathException e) {
            System.out.println("❌ File not found: " + INPUT_FILE_PATH);
            System.err.println(e.getMessage());
        }
        catch(IllegalArgumentException e) {
            System.out.println("❌ Data format error in file: " + INPUT_FILE_PATH);
            System.err.println(e.getMessage());
        }
        catch (IOException e) {
            System.out.println("❌ Error reading file: " + INPUT_FILE_PATH);
            System.err.println(e.getMessage());
        }

        return games;
    }

    public static void saveBoardGamesNIO(List<BoardGame> games) {
        List<String> lines = new ArrayList<>();
        lines.add(HEADER);
        for (BoardGame g : games) {
            lines.add(g.toFileString());
        }

        try {
            Path path = Paths.get(OUTPUT_FILE_PATH);
            Files.write(path, lines);

            System.out.println("✅ Data saved using java.nio successfully!");
        } catch (InvalidPathException e) {
            System.out.println("❌ File not found: " + INPUT_FILE_PATH);
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("❌ Error writing to file: " + INPUT_FILE_PATH);
            System.err.println(e.getMessage());
        }
    }

    private static void writeElem(XMLStreamWriter writer, String name, String value) throws XMLStreamException {
        writer.writeStartElement(name);
        writer.writeCharacters(value);
        writer.writeEndElement();
    }

    public static void saveBoardGamesXML(List<BoardGame> games) {
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();

        try (FileOutputStream fos = new FileOutputStream(OUTPUT_FILE_PATH_XML)) {
            XMLStreamWriter writer = outputFactory.createXMLStreamWriter(fos, "UTF-8");

            writer.writeStartDocument("UTF-8", "1.0");
            writer.writeStartElement("boardGames");

            for (BoardGame game : games) {
                writer.writeStartElement("boardGame");

                writeElem(writer, "id", String.valueOf(game.getId()));
                writeElem(writer, "title", game.getTitle());
                writeElem(writer, "description", game.getDescription());
                writeElem(writer, "minPlayers", String.valueOf(game.getMinPlayers()));
                writeElem(writer, "maxPlayers", String.valueOf(game.getMaxPlayers()));
                writeElem(writer, "recommendedAge", String.valueOf(game.getRecommendedAge()));
                writeElem(writer, "playingTimeMinutes", String.valueOf(game.getPlayingTimeMinutes()));
                writeElem(writer, "publisher", game.getPublisher());
                writeElem(writer, "category", game.getCategory().name());
                writeElem(writer, "status", game.getStatus().name());
                writeElem(writer, "rating", String.valueOf(game.getRating()));

                writer.writeEndElement(); // </boardGame>
            }

            writer.writeEndElement(); // </boardGames>
            writer.writeEndDocument();
            writer.flush();
            writer.close();
        } catch (IOException | XMLStreamException e) {
            System.err.println("❌ Cannot create or write to file: " + OUTPUT_FILE_PATH_XML);
            System.err.println(e.getMessage());
        }
    }

    public static List<BoardGame> readBoardGamesXML() {
        List<BoardGame> games = new ArrayList<>();
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();

        try (FileInputStream fis = new FileInputStream(INPUT_FILE_PATH_XML)) {
            XMLStreamReader reader = inputFactory.createXMLStreamReader(fis);

            String currentTag = null;

            Long id = null;
            String title = null, description = null, publisher = null;
            int minPlayers = 0, maxPlayers = 0, recommendedAge = 0, playingTime = 0;
            Category category = null;
            GameStatus status = null;
            double rating = 0;

            while (reader.hasNext()) {
                int event = reader.next();

                if (event == XMLStreamConstants.START_ELEMENT) {
                    currentTag = reader.getLocalName();
                    if (currentTag.equals("boardGame")) {
                        id = null; title = null; description = null; publisher = null;
                        minPlayers = 0; maxPlayers = 0; recommendedAge = 0; playingTime = 0;
                        category = null; status = null; rating = 0;
                    }

                } else if (event == XMLStreamConstants.CHARACTERS) {
                    String text = reader.getText().trim();
                    if (text.isEmpty()) continue;

                    try {
                        switch (currentTag) {
                            case "id" -> id = Long.parseLong(text);
                            case "title" -> title = text;
                            case "description" -> description = text;
                            case "minPlayers" -> minPlayers = Integer.parseInt(text);
                            case "maxPlayers" -> maxPlayers = Integer.parseInt(text);
                            case "recommendedAge" -> recommendedAge = Integer.parseInt(text);
                            case "playingTimeMinutes" -> playingTime = Integer.parseInt(text);
                            case "publisher" -> publisher = text;
                            case "category" -> category = Category.valueOf(text);
                            case "status" -> status = GameStatus.valueOf(text);
                            case "rating" -> rating = Double.parseDouble(text);
                        }
                    } catch (IllegalArgumentException ex) {
                        System.err.println("❌ Invalid value for <" + currentTag + ">: \"" + text + "\"");
                        System.err.println("Aborting XML reading due to invalid data.");
                    }

                } else if (event == XMLStreamConstants.END_ELEMENT) {
                    if (reader.getLocalName().equals("boardGame")) {
                        try {
                            BoardGame bg = new BoardGame(id,
                                    title, description, minPlayers, maxPlayers,
                                    recommendedAge, playingTime, publisher,
                                    category, status, rating
                            );
                            games.add(bg);

                        } catch (Exception e) {
                            System.err.println("❌ Failed to create BoardGame object (id=" + id + "): " + e.getMessage());
                            System.err.println("Aborting XML reading.");
                        }
                    }
                }
            }

            reader.close();
        } catch (FileNotFoundException e) {
            System.err.println("❌ Input XML file not found: " + INPUT_FILE_PATH_XML);
        } catch (XMLStreamException e) {
            System.err.println("❌ Error while reading XML: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("❌ Error while r/w XML: " + e.getMessage());
        }

        return games;
    }
}

