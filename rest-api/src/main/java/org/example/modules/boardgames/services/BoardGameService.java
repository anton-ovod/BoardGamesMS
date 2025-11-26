package org.example.modules.boardgames.services;

import org.example.models.BoardGame;
import org.example.modules.boardgames.BoardGameRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardGameService {
    private final BoardGameRepository repo;

    public BoardGameService(BoardGameRepository repo) {
        this.repo = repo;
    }

    public BoardGame create(BoardGame game) {
        return repo.create(game);
    }

    public BoardGame read(Long id) {
        return repo.read(id);
    }

    public List<BoardGame> readAll() {
        return repo.readAll();
    }

    public BoardGame update(BoardGame game) {
        return repo.update(game);
    }

    public int delete(Long id) {
        return repo.delete(id);
    }
}
