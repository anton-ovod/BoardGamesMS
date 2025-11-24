package org.example.modules.boardgames;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.example.models.BoardGame;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

@Service
public class BoardGameFileService
{
    @Value("${file.txt}")
    private String TXT_FILE_NAME;
    @Value("${file.xml}")
    private String XML_FILE_NAME;
    @Value("${data.dir}")
    private String DATA_DIR;

    private static final String HEADER = "id|title|description|minPlayers|maxPlayers|recommendedAge|playingTimeMinutes|publisher|category|status|rating";

    private BoardGame parseTxtLine(String line) {
        String[] parts = line.split("\\|");
        if (parts.length != 11) {
            throw new IllegalArgumentException(
                    "Invalid TXT format: expected 11 fields, got " + parts.length
            );
        }
        return new BoardGame(parts);
    }

    private Path txtPath() {
        return Paths.get(DATA_DIR).resolve(TXT_FILE_NAME);
    }

    private Path xmlPath() {
        return Paths.get(DATA_DIR).resolve(XML_FILE_NAME);
    }

    public List<BoardGame> loadTxt() {
        Path path = txtPath();

        if (!Files.exists(path))
            return new ArrayList<>();

        try {
            return Files.readAllLines(path)
                    .stream()
                    .skip(1) // skip header
                    .map(this::parseTxtLine)
                    .toList();

        } catch (Exception e) {
            throw new RuntimeException("Cannot read TXT file: " + path, e);
        }
    }

    public void saveTxt(List<BoardGame> games) {
        Path path = txtPath();

        try {
            Files.createDirectories(path.getParent());
            List<String> lines = new ArrayList<>();

            if (!Files.exists(path)) {
                lines.add(HEADER);
            }

            games.forEach(g -> lines.add(g.toFileString()));

            Files.write(path, lines,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);

        } catch (Exception e) {
            throw new RuntimeException("Cannot write TXT file: " + path, e);
        }
    }

    public void saveXml(List<BoardGame> newGames) {
        Path path = xmlPath();

        try {
            Files.createDirectories(path.getParent());

            XmlMapper mapper = new XmlMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            List<BoardGame> existing = new ArrayList<>();

            if (Files.exists(path)) {
                InputStream is = Files.newInputStream(path);
                existing = mapper.readValue(is, mapper.getTypeFactory()
                                    .constructCollectionType(List.class, BoardGame.class));

            }

            existing.addAll(newGames);
            mapper.writeValue(Files.newOutputStream(path,
                            StandardOpenOption.CREATE,
                            StandardOpenOption.TRUNCATE_EXISTING), existing);

        } catch (Exception e) {
            throw new RuntimeException("Cannot write XML file: " + path, e);
        }
    }


    public List<BoardGame> loadXml() {
        Path path = xmlPath();

        if (!Files.exists(path))
            return new ArrayList<>();

        try (InputStream is = Files.newInputStream(path)) {

            XmlMapper mapper = new XmlMapper();

            return mapper.readValue(is,
                    mapper.getTypeFactory().constructCollectionType(List.class, BoardGame.class));

        } catch (Exception e) {
            throw new RuntimeException("Cannot read XML file: " + path, e);
        }
    }


}

