package org.example.services;

import org.example.enums.Category;
import org.example.enums.GameStatus;
import org.example.models.BoardGame;
import org.example.modules.boardgames.services.BoardGameFileService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("FileService Unit Tests")
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class BoardGameFileServiceTest {

    @Autowired
    BoardGameFileService boardGameFileService;

    @Test
    void givenFileExists_whenLoadTxt_thenReturnsNonEmptyList() {
        List<BoardGame> games = boardGameFileService.loadTxt();
        assertNotNull(games);
        assertFalse(games.isEmpty(), "Games list should not be empty");
    }

    @RepeatedTest(value = 3, name = "Repeated read/write test {currentRepetition}/{totalRepetitions}")
    void givenMultipleCalls_whenLoadTxt_thenHandlesCorrectly(RepetitionInfo repetitionInfo) {
        List<BoardGame> games = boardGameFileService.loadTxt();
        assertNotNull(games);
        assertTrue(repetitionInfo.getCurrentRepetition() <= 3);
    }

    @ParameterizedTest
    @CsvSource({
            "PUZZLE, AVAILABLE",
            "TRIVIA, MAINTENANCE",
            "WAR_GAME, RESERVED"
    })
    void givenDifferentCombinations_whenSaveXML_thenSavesSuccessfully(Category category, GameStatus status) {

        List<BoardGame> games = List.of(
                new BoardGame(1L, "Test Game", "Desc", 2, 4, 10, 60, "Pub", category, status, 7.0)
        );
        assertDoesNotThrow(() -> boardGameFileService.saveXml(games));
    }

    @Test
    void givenEmptyList_whenSaveXml_thenSavesSuccessfully() {
        List<BoardGame> emptyList = new ArrayList<>();
        assertDoesNotThrow(() -> boardGameFileService.saveXml(emptyList));
    }

    @Test
    void givenValidList_whenSaveXml_thenSavesSuccessfully() {
        List<BoardGame> sampleGames = Arrays.asList(
                new BoardGame(1L, "Catan", "Settlers game", 3, 4, 10, 90, "Kosmos", Category.STRATEGY, GameStatus.AVAILABLE, 8.5),
                new BoardGame(2L, "Pandemic", "Cooperative game", 2, 4, 8, 45, "Z-Man", Category.COOPERATIVE, GameStatus.RESERVED, 9.0)
        );
        assertDoesNotThrow(() -> boardGameFileService.saveXml(sampleGames));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 5, 10})
    void givenDifferentListSizes_whenSaveXml_thenSavesSuccessfully(int size) {
        List<BoardGame> games = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            games.add(new BoardGame((long) i, "Game" + i, "Desc", 2, 4, 10, 60, "Pub", Category.STRATEGY, GameStatus.AVAILABLE, 7.0));
        }
        assertDoesNotThrow(() -> boardGameFileService.saveXml(games));
    }

    @Test
    @DisplayName("LoadXML returns non-null list when file exists")
    void givenXmlFileExists_whenLoadXml_thenReturnsNonNullList() {
        List<BoardGame> games = boardGameFileService.loadXml();
        assertNotNull(games, "Loaded XML list should not be null");
    }

    @Test
    @DisplayName("SaveTXT and LoadTXT preserve data integrity")
    void givenSavedGames_whenLoadTxt_thenDataMatchesOriginal() {
        BoardGame original = new BoardGame(
                999L, "Test Game", "Test Description",
                2, 6, 12, 120,
                "Test Publisher", Category.ADVENTURE, GameStatus.AVAILABLE, 8.7
        );

        boardGameFileService.saveTxt(List.of(original));
        List<BoardGame> loaded = boardGameFileService.loadTxt();

        assertNotNull(loaded);
        assertTrue(loaded.stream().anyMatch(g ->
            g.getTitle().equals("Test Game") &&
            g.getPublisher().equals("Test Publisher")
        ), "Saved game should be present in loaded list");
    }

    @Test
    @DisplayName("SaveXML and LoadXML preserve data integrity")
    void givenSavedGames_whenLoadXml_thenDataMatchesOriginal() {
        List<BoardGame> originalGames = Arrays.asList(
                new BoardGame(100L, "XML Test 1", "Desc1", 2, 4, 8, 60, "Pub1", Category.CARD_GAME, GameStatus.AVAILABLE, 7.5),
                new BoardGame(101L, "XML Test 2", "Desc2", 3, 6, 10, 90, "Pub2", Category.WAR_GAME, GameStatus.RESERVED, 8.0)
        );

        boardGameFileService.saveXml(originalGames);
        List<BoardGame> loaded = boardGameFileService.loadXml();

        assertNotNull(loaded);
        assertTrue(loaded.size() >= 2, "Loaded list should contain at least the saved games");
        assertTrue(loaded.stream().anyMatch(g -> g.getTitle().equals("XML Test 1")));
        assertTrue(loaded.stream().anyMatch(g -> g.getTitle().equals("XML Test 2")));
    }

    @Test
    @DisplayName("SaveTXT creates file if it doesn't exist")
    void givenNoFile_whenSaveTxt_thenCreatesNewFile() {
        BoardGame game = new BoardGame(
                888L, "New Game", "Description",
                1, 4, 6, 45,
                "Publisher", Category.PARTY, GameStatus.AVAILABLE, 6.5
        );

        assertDoesNotThrow(() -> boardGameFileService.saveTxt(List.of(game)));
    }

    @Test
    @DisplayName("Games with special characters in title are handled correctly")
    void givenSpecialCharactersInTitle_whenSaveAndLoadXml_thenPreservesData() {
        BoardGame game = new BoardGame(
                500L, "Game: The Ultimate Edition™ & More!", "Desc with special chars: <>&\"'",
                2, 4, 10, 60, "Publisher & Co.", Category.ADVENTURE, GameStatus.AVAILABLE, 9.5
        );

        boardGameFileService.saveXml(List.of(game));
        List<BoardGame> loaded = boardGameFileService.loadXml();

        assertTrue(loaded.stream().anyMatch(g ->
            g.getTitle().contains("Ultimate Edition")
        ));
    }

    @Test
    @DisplayName("Games with extreme rating values are saved correctly")
    void givenExtremeRatings_whenSaveXml_thenHandlesCorrectly() {
        List<BoardGame> games = Arrays.asList(
                new BoardGame(600L, "Min Rating", "Desc", 2, 4, 10, 60, "Pub", Category.STRATEGY, GameStatus.AVAILABLE, 0.0),
                new BoardGame(601L, "Max Rating", "Desc", 2, 4, 10, 60, "Pub", Category.STRATEGY, GameStatus.AVAILABLE, 10.0),
                new BoardGame(602L, "Decimal Rating", "Desc", 2, 4, 10, 60, "Pub", Category.STRATEGY, GameStatus.AVAILABLE, 7.89)
        );

        assertDoesNotThrow(() -> boardGameFileService.saveXml(games));
    }

    @Test
    @DisplayName("Games with extreme player counts are handled")
    void givenExtremePlayerCounts_whenSaveTxt_thenHandlesCorrectly() {
        List<BoardGame> games = Arrays.asList(
                new BoardGame(700L, "Solo Game", "Desc", 1, 1, 10, 60, "Pub", Category.STRATEGY, GameStatus.AVAILABLE, 7.0),
                new BoardGame(701L, "Party Game", "Desc", 2, 20, 10, 60, "Pub", Category.PARTY, GameStatus.AVAILABLE, 8.0)
        );

        assertDoesNotThrow(() -> boardGameFileService.saveTxt(games));
    }

    @Test
    @DisplayName("Long playing time values are preserved")
    void givenLongPlayingTime_whenSaveAndLoad_thenPreservesValue() {
        BoardGame game = new BoardGame(
                800L, "Epic Game", "Desc",
                2, 4, 14, 600, // 10 hours
                "Pub", Category.WAR_GAME, GameStatus.AVAILABLE, 8.5
        );
        boardGameFileService.saveXml(List.of(game));
        List<BoardGame> loaded = boardGameFileService.loadXml();

        assertTrue(loaded.stream().anyMatch(g ->
            g.getTitle().equals("Epic Game") && g.getPlayingTimeMinutes() == 600
        ));
    }
}
