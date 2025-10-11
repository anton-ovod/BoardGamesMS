package org.example.models;

import org.example.enums.Category;
import org.example.enums.GameStatus;


import java.util.Locale;
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
                     String description,
                     int minPlayers,
                     int maxPlayers,
                     int recommendedAge,
                     int playingTimeMinutes,
                     String publisher,
                     Category category,
                     GameStatus gameStatus,
                     double rating
                     )
    {
        this.id = ThreadLocalRandom.current().nextLong();
        this.title = title;
        this.description = description;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.recommendedAge = recommendedAge;
        this.playingTimeMinutes = playingTimeMinutes;
        this.publisher = publisher;
        this.category = category;
        this.status = gameStatus;
        this.rating = rating;
    }

    /**
     * Constructor that parses fields from a String[] (from a line of a text file)
     * Expected order:
     * id|title|description|minPlayers|maxPlayers|recommendedAge|playingTimeMinutes|publisher|category|status|rating
     */
    public BoardGame(String[] parts) {
        if (parts.length < 11) {
            throw new IllegalArgumentException("Invalid data format for BoardGame: expected 11 fields, got " + parts.length);
        }

        this.id = Long.parseLong(parts[0]);
        this.title = parts[1];
        this.description = parts[2];
        this.minPlayers = Integer.parseInt(parts[3]);
        this.maxPlayers = Integer.parseInt(parts[4]);
        this.recommendedAge = Integer.parseInt(parts[5]);
        this.playingTimeMinutes = Integer.parseInt(parts[6]);
        this.publisher = parts[7];
        this.category = Category.valueOf(parts[8]);
        this.status = GameStatus.valueOf(parts[9]);
        this.rating = Double.parseDouble(parts[10]);
    }

    public Long getId()
    {
        return id;
    }

    public Double getRating()
    {
        return rating;
    }

    public String getPublisher()
    {
        return publisher;
    }

    public Category getCategory()
    {
        return category;
    }

    public String getTitle()
    {
        return title;
    }

    public String toFileString() {
        return String.format(Locale.US, "%d|%s|%s|%d|%d|%d|%d|%s|%s|%s|%.1f",
                this.id,
                this.title,
                this.description,
                this.minPlayers,
                this.maxPlayers,
                this.recommendedAge,
                this.playingTimeMinutes,
                this.publisher,
                this.category,
                this.status,
                this.rating);
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
