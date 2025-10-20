package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.example.enums.Category;
import org.example.enums.GameStatus;


import java.util.Locale;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class BoardGame {
    private Long id;
    @NonNull
    private String title;
    @NonNull
    private String description;
    @NonNull
    private int minPlayers;
    @NonNull
    private int maxPlayers;
    @NonNull
    private int recommendedAge;
    @NonNull
    private int playingTimeMinutes;
    @NonNull
    private String publisher;
    @NonNull
    private Category category;
    @NonNull
    private GameStatus status;
    @NonNull
    private double rating;

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
