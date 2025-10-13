package org.example.modules.boardgames;

import org.example.models.BoardGame;
import org.example.enums.Category;
import org.example.enums.GameStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BoardGameRepository {

    private final JdbcTemplate jdbc;

    public BoardGameRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // --- MAPPER ---
    private final RowMapper<BoardGame> mapper = (rs, rowNum) -> {
        BoardGame game = new BoardGame(
                rs.getString("title"),
                rs.getString("description"),
                rs.getInt("min_players"),
                rs.getInt("max_players"),
                rs.getInt("recommended_age"),
                rs.getInt("playing_time_minutes"),
                rs.getString("publisher"),
                Category.valueOf(rs.getString("category")),
                GameStatus.valueOf(rs.getString("status")),
                rs.getDouble("rating")
        );
        // Set the ID from the DB
        game.setId(rs.getLong("id"));
        return game;
    };

    // --- READ ---
    public List<BoardGame> findAll() {
        return jdbc.query("SELECT * FROM board_games", mapper);
    }

    public BoardGame findById(Long id) {
        try {
            return jdbc.queryForObject("SELECT * FROM board_games WHERE id = ?", mapper, id);
        } catch (Exception e) {
            return null; // Return null if not found
        }
    }

    // --- CREATE ---
    public int create(BoardGame game) {
        return jdbc.update("""
                INSERT INTO board_games(id, title, description, min_players, max_players, 
                recommended_age, playing_time_minutes, publisher, category, status, rating)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """,
                game.getId(),                     // ID is now inserted
                game.getTitle(),
                game.getDescription(),
                game.getMinPlayers(),
                game.getMaxPlayers(),
                game.getRecommendedAge(),
                game.getPlayingTimeMinutes(),
                game.getPublisher(),
                game.getCategory().name(),
                game.getStatus().name(),
                game.getRating()
        );
    }

    // --- UPDATE ---
    public int update(BoardGame game) {
        return jdbc.update("""
                UPDATE board_games
                SET title = ?, description = ?, min_players = ?, max_players = ?,
                    recommended_age = ?, playing_time_minutes = ?, publisher = ?,
                    category = ?, status = ?, rating = ?
                WHERE id = ?
                """,
                game.getTitle(),
                game.getDescription(),
                game.getMinPlayers(),
                game.getMaxPlayers(),
                game.getRecommendedAge(),
                game.getPlayingTimeMinutes(),
                game.getPublisher(),
                game.getCategory().name(),
                game.getStatus().name(),
                game.getRating(),
                game.getId()
        );
    }

    // --- UPDATE RATING ONLY ---
    public int updateRating(Long id, double rating) {
        return jdbc.update("UPDATE board_games SET rating = ? WHERE id = ?", rating, id);
    }

    // --- DELETE ---
    public int delete(Long id) {
        return jdbc.update("DELETE FROM board_games WHERE id = ?", id);
    }
}
