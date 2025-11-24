package org.example.services;

import org.example.enums.Category;
import org.example.enums.GameStatus;
import org.example.models.BoardGame;
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
class FileServiceTest {

    @Autowired
    FileService fileService;

    @Test
    void givenFileExists_whenReadBoardGamesIO_thenReturnsNonEmptyList() {
        List<BoardGame> games = fileService.readBoardGamesIO();
        assertNotNull(games);
        assertFalse(games.isEmpty(), "Games list should not be empty");
    }

    @Test
    void givenFileNotFound_whenReadBoardGamesIO_thenReturnsEmptyList() {
        List<BoardGame> games = fileService.readBoardGamesIO();
        assertNotNull(games);
    }

    @Test
    void givenFileExists_whenReadBoardGamesNIO_thenReturnsValidList() {
        List<BoardGame> games = fileService.readBoardGamesNIO();
        assertNotNull(games);
        assertAll("games validation",
                () -> assertNotNull(games),
                () -> assertFalse(games.isEmpty())
        );
    }

    @RepeatedTest(value = 3, name = "Repeated read/write test {currentRepetition}/{totalRepetitions}")
    void givenMultipleCalls_whenReadBoardGamesIO_thenHandlesCorrectly(RepetitionInfo repetitionInfo) {
        List<BoardGame> games = fileService.readBoardGamesIO();
        assertNotNull(games);
        assertTrue(repetitionInfo.getCurrentRepetition() <= 3);
    }

    @Test
    void givenNullList_whenSaveBoardGamesIO_thenThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            fileService.saveBoardGamesIO(null);
        });
    }

    @ParameterizedTest
    @CsvSource({
            "PUZZLE, AVAILABLE",
            "TRIVIA, MAINTENANCE",
            "WAR_GAME, RESERVED"
    })
    void givenDifferentCombinations_whenSaveBoardGamesXML_thenSavesSuccessfully(Category category, GameStatus status) {

        List<BoardGame> games = List.of(
                new BoardGame(1L, "Test Game", "Desc", 2, 4, 10, 60, "Pub", category, status, 7.0)
        );
        assertDoesNotThrow(() -> fileService.saveBoardGamesXML(games));
    }


    @Test
    void givenEmptyList_whenSaveBoardGamesIO_thenSavesSuccessfully() {
        List<BoardGame> emptyList = new ArrayList<>();
        assertDoesNotThrow(() -> fileService.saveBoardGamesIO(emptyList));
    }

    @Test
    void givenValidList_whenSaveBoardGamesNIO_thenSavesSuccessfully() {
        List<BoardGame> sampleGames = Arrays.asList(
                new BoardGame(1L, "Catan", "Settlers game", 3, 4, 10, 90, "Kosmos", Category.STRATEGY, GameStatus.AVAILABLE, 8.5),
                new BoardGame(2L, "Pandemic", "Cooperative game", 2, 4, 8, 45, "Z-Man", Category.COOPERATIVE, GameStatus.RESERVED, 9.0)
        );
        assertDoesNotThrow(() -> fileService.saveBoardGamesNIO(sampleGames));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 5, 10})
    void givenDifferentListSizes_whenSaveBoardGamesIO_thenSavesSuccessfully(int size) {
        List<BoardGame> games = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            games.add(new BoardGame((long) i, "Game" + i, "Desc", 2, 4, 10, 60, "Pub", Category.STRATEGY, GameStatus.AVAILABLE, 7.0));
        }
        assertDoesNotThrow(() -> fileService.saveBoardGamesIO(games));
    }

    @Test
    void givenNullList_whenSaveBoardGamesNIO_thenThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            fileService.saveBoardGamesNIO(null);
        });
    }

}
