package org.example.modules.boardgames;

import org.example.enums.Category;
import org.example.enums.GameStatus;
import org.example.models.BoardGame;
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

    private final RowMapper<BoardGame> mapper = (rs, rowNum) -> {
        BoardGame game = new BoardGame(
                rs.getLong("id"),
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
        return game;
    };

    public BoardGame create(BoardGame game) {
        Long id = jdbc.queryForObject(
                """
                INSERT INTO board_games(title, description, min_players, max_players,
                                        recommended_age, playing_time_minutes, publisher, category, status, rating)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                RETURNING id
                """,
                Long.class,
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

        return new BoardGame(
                id,
                game.getTitle(),
                game.getDescription(),
                game.getMinPlayers(),
                game.getMaxPlayers(),
                game.getRecommendedAge(),
                game.getPlayingTimeMinutes(),
                game.getPublisher(),
                game.getCategory(),
                game.getStatus(),
                game.getRating()
        );
    }

    public BoardGame read(Long id) {
        List<BoardGame> games = jdbc.query("SELECT * FROM board_games WHERE id = ?", mapper, id);
        return games.isEmpty() ? null : games.get(0);
    }

    public List<BoardGame> readAll() {
        return jdbc.query("SELECT * FROM board_games", mapper);
    }


    public BoardGame update(BoardGame game) {
        int rows = jdbc.update("""
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

        if (rows == 0) return null;
        return read(game.getId());
    }

    public int delete(Long id) {
        return jdbc.update("DELETE FROM board_games WHERE id = ?", id);
    }
}
