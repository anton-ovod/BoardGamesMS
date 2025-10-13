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

    private final RowMapper<BoardGame> mapper = (rs, rowNum) -> new BoardGame(
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

    public List<BoardGame> findAll() {
        return jdbc.query("SELECT * FROM board_games", mapper);
    }

    public BoardGame findById(Long id) {
        return jdbc.queryForObject("SELECT * FROM board_games WHERE id = ?", mapper, id);
    }

    public int save(BoardGame game) {
        return jdbc.update("""
                INSERT INTO board_games(title, description, min_players, max_players, 
                recommended_age, playing_time_minutes, publisher, category, status, rating)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """,
                game.getTitle(), game.toString(), 2, 6, 10, 60,
                game.getPublisher(), game.getCategory().name(), game.toString(), game.getRating());
    }

    public int updateRating(Long id, double rating) {
        return jdbc.update("UPDATE board_games SET rating = ? WHERE id = ?", rating, id);
    }

    public int delete(Long id) {
        return jdbc.update("DELETE FROM board_games WHERE id = ?", id);
    }
}
