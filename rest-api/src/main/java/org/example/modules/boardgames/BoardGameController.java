package org.example.modules.boardgames;

import org.example.models.BoardGame;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boardgames")
public class BoardGameController {

    private final BoardGameService service;

    public BoardGameController(BoardGameService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<BoardGame> create(@RequestBody BoardGame game) {
        BoardGame created = service.create(game);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardGame> read(@PathVariable Long id) {
        BoardGame game = service.read(id);
        return game != null ? ResponseEntity.ok(game) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<BoardGame>> readAll() {
        return ResponseEntity.ok(service.readAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardGame> update(@PathVariable Long id, @RequestBody BoardGame game) {
        game.setId(id);
        BoardGame updated = service.update(game);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        int rows = service.delete(id);
        return rows > 0 ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
