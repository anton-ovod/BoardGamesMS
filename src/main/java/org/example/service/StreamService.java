package org.example.service;

import org.example.enums.Category;
import org.example.models.BoardGame;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StreamService {
    public List<BoardGame> filterByRating(List<BoardGame> games, double minRating) {
        return games.stream()
                .filter(g -> g.getRating() >= minRating)
                .collect(Collectors.toList());
    }

    public List<BoardGame> filterByPublisher(List<BoardGame> games, String publisher) {
        return games.stream()
                .filter(g -> g.getPublisher().equalsIgnoreCase(publisher))
                .collect(Collectors.toList());
    }

    public Map<Category, Long> countCategoryOccurrences(List<BoardGame> games) {
        return games.stream()
                .collect(Collectors.groupingBy(BoardGame::getCategory, Collectors.counting()));
    }

    public List<String> mapToTitles(List<BoardGame> games) {
        return games.stream()
                .map(BoardGame::getTitle)
                .collect(Collectors.toList());
    }

    public List<String> mapToTitleAndRating(List<BoardGame> games) {
        return games.stream()
                .map(g -> g.getTitle() + " (" + g.getRating() + ")")
                .collect(Collectors.toList());
    }

    public List<BoardGame> sortByRatingDescending(List<BoardGame> games) {
        return games.stream()
                .sorted(Comparator.comparingDouble(BoardGame::getRating).reversed())
                .collect(Collectors.toList());
    }
}
