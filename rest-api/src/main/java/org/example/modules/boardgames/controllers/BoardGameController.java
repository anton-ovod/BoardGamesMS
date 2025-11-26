package org.example.modules.boardgames.controllers;

import org.example.builders.BoardGameReport;
import org.example.models.BoardGame;
import org.example.modules.boardgames.services.BoardGameService;
import org.example.modules.borrowings.BorrowingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boardgames")
public class BoardGameController {

    private final BoardGameService service;
    private final BorrowingService borrowingService;

    public BoardGameController(BoardGameService service, BorrowingService borrowingService) {
        this.service = service;
        this.borrowingService = borrowingService;
    }

    @PostMapping
    public ResponseEntity<BoardGame> create(@RequestBody BoardGame game) {
        BoardGame created = service.create(game);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardGame> read(@PathVariable("id") Long id) {
        BoardGame game = service.read(id);
        return game != null ? ResponseEntity.ok(game) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<BoardGame>> readAll() {
        return ResponseEntity.ok(service.readAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardGame> update(@PathVariable("id") Long id, @RequestBody BoardGame game) {
        game.setId(id);
        BoardGame updated = service.update(game);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        int rows = service.delete(id);
        return rows > 0 ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/report")
    public ResponseEntity<BoardGameReport> getReport(@PathVariable("id") Long id) {
        BoardGame game = service.read(id);
        if (game == null) return ResponseEntity.notFound().build();

        var borrowings = borrowingService.getByGameId(id);
        BoardGameReport report = BoardGameReport.builder()
                .game(game)
                .borrowings(borrowings)
                .build();

        return ResponseEntity.ok(report);
    }
}
