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

    @Test
    void givenNullList_whenSaveTxt_thenThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            boardGameFileService.saveTxt(null);
        });
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
    void givenNullList_whenSaveXml_thenThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            boardGameFileService.saveXml(null);
        });
    }

}
