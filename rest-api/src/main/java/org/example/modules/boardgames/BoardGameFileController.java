package org.example.modules.boardgames;

import org.example.models.BoardGame;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/boardgames/files")
public class BoardGameFileController {
    private final BoardGameFileService fileService;

    public BoardGameFileController(BoardGameFileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/txt")
    public ResponseEntity<?> loadTxt() {
        try {
            return ResponseEntity.ok(fileService.loadTxt());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to read TXT file.");
        }
    }

    @PostMapping("/txt")
    public ResponseEntity<?> saveTxt(@RequestBody List<BoardGame> games) {
        try {
            fileService.saveTxt(games);
            return ResponseEntity
                    .created(URI.create("/boardgames/file/txt"))
                    .build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to write TXT file.");
        }
    }

    @GetMapping("/xml")
    public ResponseEntity<?> loadXml() {
        try {
            return ResponseEntity.ok(fileService.loadXml());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to read XML file.");
        }
    }

    @PostMapping("/xml")
    public ResponseEntity<?> saveXml(@RequestBody List<BoardGame> games) {
        try {
            fileService.saveXml(games);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .location(URI.create("/boardgames/file/xml"))
                    .build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to write XML file.");
        }
    }
}
