package org.example.service;

import org.example.models.BoardGame;
import org.example.enums.Category;
import org.example.enums.GameStatus;

import javax.xml.stream.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BoardGameXMLService {
    private static final String INPUT_FILE_PATH = "src/main/resources/boardgames.xml";
    private static final String OUTPUT_FILE_PATH = "src/main/resources/boardgames-modified.xml";

    //Writes list of games into XML file.
    public static void writeToXML(List<BoardGame> games) throws Exception {
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();

        try (FileOutputStream fos = new FileOutputStream(OUTPUT_FILE_PATH)) {
            XMLStreamWriter writer = outputFactory.createXMLStreamWriter(fos, "UTF-8");

            writer.writeStartDocument("UTF-8", "1.0");
            writer.writeStartElement("boardGames");

            for (BoardGame game : games) {
                writer.writeStartElement("boardGame");

                writer.writeStartElement("id");
                writer.writeCharacters(String.valueOf(game.getId()));
                writer.writeEndElement();

                writer.writeStartElement("title");
                writer.writeCharacters(game.getTitle());
                writer.writeEndElement();

                writer.writeStartElement("description");
                writer.writeCharacters(game.getDescription());
                writer.writeEndElement();

                writer.writeStartElement("minPlayers");
                writer.writeCharacters(String.valueOf(game.getMinPlayers()));
                writer.writeEndElement();

                writer.writeStartElement("maxPlayers");
                writer.writeCharacters(String.valueOf(game.getMaxPlayers()));
                writer.writeEndElement();

                writer.writeStartElement("recommendedAge");
                writer.writeCharacters(String.valueOf(game.getRecommendedAge()));
                writer.writeEndElement();

                writer.writeStartElement("playingTimeMinutes");
                writer.writeCharacters(String.valueOf(game.getPlayingTimeMinutes()));
                writer.writeEndElement();

                writer.writeStartElement("publisher");
                writer.writeCharacters(game.getPublisher());
                writer.writeEndElement();

                writer.writeStartElement("category");
                writer.writeCharacters(game.getCategory().name());
                writer.writeEndElement();

                writer.writeStartElement("status");
                writer.writeCharacters(game.getStatus().name());
                writer.writeEndElement();

                writer.writeStartElement("rating");
                writer.writeCharacters(String.valueOf(game.getRating()));
                writer.writeEndElement();

                writer.writeEndElement(); // </boardGame>
            }

            writer.writeEndElement(); // </boardGames>
            writer.writeEndDocument();
            writer.flush();
            writer.close();
        }
    }

    //Read from XML, exit if something wrong with data.
    public static List<BoardGame> readFromXML() throws Exception {
        List<BoardGame> games = new ArrayList<>();
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();

        try (FileInputStream fis = new FileInputStream(INPUT_FILE_PATH)) {
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
                        System.exit(1);
                    }

                } else if (event == XMLStreamConstants.END_ELEMENT) {
                    if (reader.getLocalName().equals("boardGame")) {
                        try {
                            if (title == null || description == null || category == null || status == null) {
                                System.err.println("❌ Missing required data for BoardGame (id=" + id + ")");
                                System.err.println("Aborting XML reading due to missing fields.");
                                System.exit(1);
                            }

                            BoardGame bg = new BoardGame(
                                    title, description, minPlayers, maxPlayers,
                                    recommendedAge, playingTime, publisher,
                                    category, status, rating
                            );
                            games.add(bg);

                        } catch (Exception e) {
                            System.err.println("❌ Failed to create BoardGame object (id=" + id + "): " + e.getMessage());
                            System.err.println("Aborting XML reading.");
                            System.exit(1);
                        }
                    }
                }
            }

            reader.close();
        } catch (FileNotFoundException e) {
            System.err.println("❌ Input XML file not found: " + INPUT_FILE_PATH);
            System.exit(1);
        } catch (XMLStreamException e) {
            System.err.println("❌ Error while reading XML: " + e.getMessage());
            System.exit(1);
        }

        return games;
    }
}
