package org.example.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.enums.Category;
import org.example.enums.GameStatus;
import org.example.models.BoardGame;

@Data
@AllArgsConstructor
public class BoardGameBackupDto {
    private Long id;
    private String title;
    private String description;
    private int minPlayers;
    private int maxPlayers;
    private int recommendedAge;
    private int playingTimeMinutes;
    private String publisher;
    private Category category;
    private GameStatus status;
    private double rating;

    public BoardGameBackupDto(BoardGame boardGame)
    {
        this.id = boardGame.getId();
        this.title = boardGame.getTitle();
        this.description = boardGame.getDescription();
        this.minPlayers = boardGame.getMinPlayers();
        this.maxPlayers = boardGame.getMaxPlayers();
        this.recommendedAge = boardGame.getRecommendedAge();
        this.playingTimeMinutes = boardGame.getPlayingTimeMinutes();
        this.publisher = boardGame.getPublisher();
        this.category = boardGame.getCategory();
        this.status = boardGame.getStatus();
        this.rating = boardGame.getRating();
    }

    public BoardGame toEntity()
    {
        return new BoardGame(
                id,
                title,
                description,
                minPlayers,
                maxPlayers,
                recommendedAge,
                playingTimeMinutes,
                publisher,
                category,
                status,
                rating
        );
    }
}
