package org.example.modules.boardgames;

import org.example.models.BoardGame;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BoardGameService {
    private final BoardGameRepository repo;

    public BoardGameService(BoardGameRepository repo) {
        this.repo = repo;
    }

    public List<BoardGame> getAll() { return repo.findAll(); }
    public BoardGame get(Long id) { return repo.findById(id); }
    public void create(BoardGame bg) { repo.save(bg); }
    public void updateRating(Long id, double rating) { repo.updateRating(id, rating); }
    public void delete(Long id) { repo.delete(id); }
}
