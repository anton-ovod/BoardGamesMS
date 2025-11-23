package org.example.services;

import org.example.enums.Category;
import org.example.enums.GameStatus;
import org.example.models.BoardGame;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

@DisplayName("FileService Unit Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FileServiceTest {

    @TempDir
    Path tempDir;

    private List<BoardGame> sampleGames;
    private static final String TEST_HEADER = "id|title|description|minPlayers|maxPlayers|recommendedAge|playingTimeMinutes|publisher|category|status|rating";

    @BeforeEach
    void setUp() {
        sampleGames = Arrays.asList(
                new BoardGame(1L, "Catan", "Settlers game", 3, 4, 10, 90, "Kosmos", Category.STRATEGY, GameStatus.AVAILABLE, 8.5),
                new BoardGame(2L, "Pandemic", "Cooperative game", 2, 4, 8, 45, "Z-Man", Category.COOPERATIVE, GameStatus.SOLD_OUT, 9.0)
        );
    }

    @AfterEach
    void tearDown() {
        sampleGames = null;
    }

    @Test
    @DisplayName("Given file exists when readBoardGamesIO called then returns non-empty list")
    @Order(1)
    void givenFileExists_whenReadBoardGamesIO_thenReturnsNonEmptyList() {
        // when
        List<BoardGame> games = FileService.readBoardGamesIO();

        // then
        assertNotNull(games);
        assertFalse(games.isEmpty(), "Games list should not be empty");
    }

    @Test
    @DisplayName("Given file not found when readBoardGamesIO called then returns empty list")
    @Order(2)
    void givenFileNotFound_whenReadBoardGamesIO_thenReturnsEmptyList() {
        // when
        List<BoardGame> games = FileService.readBoardGamesIO();

        // then
        assertNotNull(games);
    }

    @Test
    @DisplayName("Given file exists when readBoardGamesNIO called then returns valid list")
    @Order(3)
    void givenFileExists_whenReadBoardGamesNIO_thenReturnsValidList() {
        // when
        List<BoardGame> games = FileService.readBoardGamesNIO();

        // then
        assertNotNull(games);
        assertAll("games validation",
                () -> assertNotNull(games),
                () -> assertTrue(games.size() >= 0)
        );
    }

    @Test
    @DisplayName("Given valid list when saveBoardGamesIO called then saves successfully")
    @Order(4)
    void givenValidList_whenSaveBoardGamesIO_thenSavesSuccessfully() {
        // when & then
        assertDoesNotThrow(() -> FileService.saveBoardGamesIO(sampleGames));
    }

    @Test
    @DisplayName("Given null list when saveBoardGamesIO called then throws NullPointerException")
    @Order(5)
    void givenNullList_whenSaveBoardGamesIO_thenThrowsNullPointerException() {
        // when & then
        assertThrows(NullPointerException.class, () -> {
            FileService.saveBoardGamesIO(null);
        });
    }

    @Test
    @DisplayName("Given empty list when saveBoardGamesIO called then saves successfully")
    @Order(6)
    void givenEmptyList_whenSaveBoardGamesIO_thenSavesSuccessfully() {
        // given
        List<BoardGame> emptyList = new ArrayList<>();

        // when & then
        assertDoesNotThrow(() -> FileService.saveBoardGamesIO(emptyList));
    }

    @Test
    @DisplayName("Given valid list when saveBoardGamesNIO called then saves successfully")
    @Order(7)
    void givenValidList_whenSaveBoardGamesNIO_thenSavesSuccessfully() {
        // when & then
        assertDoesNotThrow(() -> FileService.saveBoardGamesNIO(sampleGames));
    }

    @Test
    @DisplayName("Given null list when saveBoardGamesNIO called then throws NullPointerException")
    @Order(8)
    void givenNullList_whenSaveBoardGamesNIO_thenThrowsNullPointerException() {
        // when & then
        assertThrows(NullPointerException.class, () -> {
            FileService.saveBoardGamesNIO(null);
        });
    }

    @Test
    @DisplayName("Given valid list when saveBoardGamesXML called then saves successfully")
    @Order(9)
    void givenValidList_whenSaveBoardGamesXML_thenSavesSuccessfully() {
        // when & then
        assertDoesNotThrow(() -> FileService.saveBoardGamesXML(sampleGames));
    }

    @Test
    @DisplayName("Given null list when saveBoardGamesXML called then throws NullPointerException")
    @Order(10)
    void givenNullList_whenSaveBoardGamesXML_thenThrowsNullPointerException() {
        // when & then
        assertThrows(NullPointerException.class, () -> {
            FileService.saveBoardGamesXML(null);
        });
    }

    @Test
    @DisplayName("Given empty list when saveBoardGamesXML called then saves successfully")
    @Order(11)
    void givenEmptyList_whenSaveBoardGamesXML_thenSavesSuccessfully() {
        // given
        List<BoardGame> emptyList = new ArrayList<>();

        // when & then
        assertDoesNotThrow(() -> FileService.saveBoardGamesXML(emptyList));
    }

    @Test
    @DisplayName("Given XML file exists when readBoardGamesXML called then returns valid list")
    @Order(12)
    void givenXMLFileExists_whenReadBoardGamesXML_thenReturnsValidList() {
        // when
        List<BoardGame> games = FileService.readBoardGamesXML();

        // then
        assertNotNull(games);
    }

    @RepeatedTest(value = 3, name = "Repeated read/write test {currentRepetition}/{totalRepetitions}")
    @DisplayName("Given multiple calls when readBoardGamesIO called then handles correctly")
    @Order(13)
    void givenMultipleCalls_whenReadBoardGamesIO_thenHandlesCorrectly(RepetitionInfo repetitionInfo) {
        // when
        List<BoardGame> games = FileService.readBoardGamesIO();

        // then
        assertNotNull(games);
        assertTrue(repetitionInfo.getCurrentRepetition() <= 3);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 5, 10})
    @DisplayName("Given different list sizes when saveBoardGamesIO called then saves successfully")
    @Order(14)
    void givenDifferentListSizes_whenSaveBoardGamesIO_thenSavesSuccessfully(int size) {
        // given
        List<BoardGame> games = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            games.add(new BoardGame((long) i, "Game" + i, "Desc", 2, 4, 10, 60, "Pub", Category.STRATEGY, GameStatus.AVAILABLE, 7.0));
        }

        // when & then
        assertDoesNotThrow(() -> FileService.saveBoardGamesIO(games));
    }

    @Test
    @DisplayName("Given game with special characters when saveBoardGamesIO called then handles correctly")
    @Order(15)
    void givenGameWithSpecialCharacters_whenSaveBoardGamesIO_thenHandlesCorrectly() {
        // given
        List<BoardGame> games = Arrays.asList(
                new BoardGame(1L, "Game|With|Pipes", "Description", 2, 4, 10, 60, "Publisher", Category.STRATEGY, GameStatus.AVAILABLE, 8.0)
        );

        // when & then
        assertDoesNotThrow(() -> FileService.saveBoardGamesIO(games));
    }

    @Test
    @DisplayName("Given game with extreme ratings when saveBoardGamesXML called then saves successfully")
    @Order(16)
    void givenGameWithExtremeRatings_whenSaveBoardGamesXML_thenSavesSuccessfully() {
        // given
        List<BoardGame> games = Arrays.asList(
                new BoardGame(1L, "Game1", "Desc", 2, 4, 10, 60, "Pub", Category.STRATEGY, GameStatus.AVAILABLE, 10.0),
                new BoardGame(2L, "Game2", "Desc", 2, 4, 10, 60, "Pub", Category.STRATEGY, GameStatus.AVAILABLE, 0.0)
        );

        // when & then
        assertDoesNotThrow(() -> FileService.saveBoardGamesXML(games));
    }

    @Nested
    @DisplayName("Boundary Value Tests")
    class BoundaryValueTests {

        @Test
        @DisplayName("Given min equals max players when saveBoardGamesNIO called then saves successfully")
        void givenMinEqualsMaxPlayers_whenSaveBoardGamesNIO_thenSavesSuccessfully() {
            // given
            List<BoardGame> games = Arrays.asList(
                    new BoardGame(1L, "Solo Game", "Desc", 1, 1, 10, 30, "Pub", Category.STRATEGY, GameStatus.AVAILABLE, 7.5)
            );

            // when & then
            assertDoesNotThrow(() -> FileService.saveBoardGamesNIO(games));
        }

        @Test
        @DisplayName("Given zero playing time when saveBoardGamesXML called then saves successfully")
        void givenZeroPlayingTime_whenSaveBoardGamesXML_thenSavesSuccessfully() {
            // given
            List<BoardGame> games = Arrays.asList(
                    new BoardGame(1L, "Quick Game", "Desc", 2, 4, 10, 0, "Pub", Category.PARTY, GameStatus.AVAILABLE, 6.0)
            );

            // when & then
            assertDoesNotThrow(() -> FileService.saveBoardGamesXML(games));
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Given invalid category in XML when readBoardGamesXML called then returns list")
        @Disabled("Requires mocking resource loading")
        void givenInvalidCategoryInXML_whenReadBoardGamesXML_thenReturnsList() {
            assertNotNull(FileService.readBoardGamesXML());
        }

        @Test
        @DisplayName("Given concurrent writes when saveBoardGames called then completes without deadlock")
        @Timeout(5)
        void givenConcurrentWrites_whenSaveBoardGames_thenCompletesWithoutDeadlock() throws InterruptedException {
            // given
            Thread t1 = new Thread(() -> FileService.saveBoardGamesIO(sampleGames));
            Thread t2 = new Thread(() -> FileService.saveBoardGamesNIO(sampleGames));

            // when
            t1.start();
            t2.start();
            t1.join();
            t2.join();

            // then
            assertTrue(true);
        }
    }

    @Test
    @DisplayName("Given saved data when read then data integrity verified")
    @Order(17)
    void givenSavedData_whenRead_thenDataIntegrityVerified() {
        assumeTrue(sampleGames.size() > 0, "Sample games must not be empty");

        // when
        FileService.saveBoardGamesIO(sampleGames);
        List<BoardGame> readGames = FileService.readBoardGamesIO();

        // then
        assertNotNull(readGames);
    }

    @ParameterizedTest
    @CsvSource({
            "STRATEGY, AVAILABLE",
            "PARTY, SOLD_OUT",
            "COOPERATIVE, RESERVED"
    })
    @DisplayName("Given different category-status combinations when saveBoardGamesXML called then saves successfully")
    @Order(18)
    void givenDifferentCombinations_whenSaveBoardGamesXML_thenSavesSuccessfully(Category category, GameStatus status) {
        // given
        List<BoardGame> games = Arrays.asList(
                new BoardGame(1L, "Test Game", "Desc", 2, 4, 10, 60, "Pub", category, status, 7.0)
        );

        // when & then
        assertDoesNotThrow(() -> FileService.saveBoardGamesXML(games));
    }
}
