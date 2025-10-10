package org.example.model;

import org.example.enums.Category;
import org.example.enums.GameStatus;

import java.time.LocalDate;
import java.util.List;

public class BoardGame {
    private Long id;
    private String title;
    private String description;
    private int minPlayers;
    private int maxPlayers;
    private int recommendedAge;
    private int playingTimeMinutes;
    private String publisher;
    private LocalDate releaseDate;
    private Category category;
    private GameStatus status;
    private List<String> mechanics;
    private double rating;

    public BoardGame() {}

    public BoardGame(String title, int minPlayers, int maxPlayers, int recommendedAge, int playingTimeMinutes) {
        this.title = title;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.recommendedAge = recommendedAge;
        this.playingTimeMinutes = playingTimeMinutes;
        this.status = GameStatus.AVAILABLE;
        this.rating = 0.0;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getRecommendedAge() {
        return recommendedAge;
    }

    public void setRecommendedAge(int recommendedAge) {
        this.recommendedAge = recommendedAge;
    }

    public int getPlayingTimeMinutes() {
        return playingTimeMinutes;
    }

    public void setPlayingTimeMinutes(int playingTimeMinutes) {
        this.playingTimeMinutes = playingTimeMinutes;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public List<String> getMechanics() {
        return mechanics;
    }

    public void setMechanics(List<String> mechanics) {
        this.mechanics = mechanics;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
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
