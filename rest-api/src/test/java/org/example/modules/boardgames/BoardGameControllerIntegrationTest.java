package org.example.modules.boardgames;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.models.BoardGame;
import org.example.enums.Category;
import org.example.enums.GameStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
@ActiveProfiles("test")
class BoardGameControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenValidBoardGame_whenCreate_thenCreatedAnd200OK() throws Exception {
        BoardGame boardGame = new BoardGame();
        boardGame.setTitle("Catan");
        boardGame.setDescription("A strategic board game about resource management");
        boardGame.setMinPlayers(3);
        boardGame.setMaxPlayers(4);
        boardGame.setRecommendedAge(10);
        boardGame.setPlayingTimeMinutes(90);
        boardGame.setPublisher("Catan Studio");
        boardGame.setCategory(Category.STRATEGY);
        boardGame.setStatus(GameStatus.AVAILABLE);
        boardGame.setRating(4.5);

        mockMvc.perform(post("/boardgames")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(boardGame)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Catan"))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    void givenExistingBoardGame_whenUpdate_thenUpdatedAnd200OK() throws Exception {
        BoardGame boardGame = new BoardGame();
        boardGame.setTitle("Chess");
        boardGame.setDescription("Classic two-player strategy game");
        boardGame.setMinPlayers(2);
        boardGame.setMaxPlayers(2);
        boardGame.setRecommendedAge(6);
        boardGame.setPlayingTimeMinutes(60);
        boardGame.setPublisher("Various Publishers");
        boardGame.setCategory(Category.STRATEGY);
        boardGame.setStatus(GameStatus.AVAILABLE);
        boardGame.setRating(4.8);

        String createResponse = mockMvc.perform(post("/boardgames")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(boardGame)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        long id = objectMapper.readTree(createResponse).get("id").asLong();

        boardGame.setTitle("Chess Deluxe");
        boardGame.setDescription("Premium edition of classic chess");
        mockMvc.perform(put("/boardgames/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(boardGame)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Chess Deluxe"));
    }
}
