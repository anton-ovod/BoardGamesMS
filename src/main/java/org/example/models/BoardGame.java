package org.example.models;

import org.example.enums.Category;
import org.example.enums.GameStatus;


import java.util.concurrent.ThreadLocalRandom;

public class BoardGame {
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

    public BoardGame() {}

    public BoardGame(String title,
                     int minPlayers,
                     int maxPlayers,
                     int recommendedAge,
                     int playingTimeMinutes,
                     Category category,
                     GameStatus gameStatus,
                     double rating
                     )
    {
        this.id = ThreadLocalRandom.current().nextLong();
        this.title = title;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.recommendedAge = recommendedAge;
        this.playingTimeMinutes = playingTimeMinutes;
        this.category = category;
        this.status = gameStatus;
        this.rating = rating;
    }

    public Long getId()
    {
        return id;
    }

    public String getTitle()
    {
        return title;
    }


    @Override
    public String toString() {
        return "BoardGame{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", minPlayers=" + minPlayers +
                ", maxPlayers=" + maxPlayers +
                ", recommendedAge=" + recommendedAge +
                ", playingTimeMinutes=" + playingTimeMinutes +
                ", publisher='" + publisher + '\'' +
                ", category=" + category +
                ", status=" + status +
                ", rating=" + rating +
                '}';
    }
}
