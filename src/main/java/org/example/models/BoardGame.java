package org.example.models;

import jakarta.persistence.*;
import lombok.*;
import org.example.enums.Category;
import org.example.enums.GameStatus;


import java.util.Locale;

@Entity
@Table(name = "boardgames")
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class BoardGame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    @NonNull
    private String title;
    @Column(nullable = false)
    @NonNull
    private String description;
    @Column(nullable = false)
    private int minPlayers;
    @Column(nullable = false)
    private int maxPlayers;
    @Column(nullable = false)
    private int recommendedAge;
    @Column(nullable = false)
    private int playingTimeMinutes;
    @Column(nullable = false)
    @NonNull
    private String publisher;
    @Column(nullable = false)
    @NonNull
    private Category category;
    @Column(nullable = false)
    @NonNull
    private GameStatus status;
    @Column(nullable = false)
    private double rating;

    /**
     * Constructor that parses fields from a String[] (from a line of a text file)
     * Expected order:
     * id|title|description|minPlayers|maxPlayers|recommendedAge|playingTimeMinutes|publisher|category|status|rating
     */
    public BoardGame(String[] parts) {
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
