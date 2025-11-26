package org.example.modules.borrowings;

import org.example.builders.BorrowingReceipt;
import org.example.models.Borrowing;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/borrowings")
public class BorrowingController {
    private final BorrowingService borrowingService;

    public BorrowingController(BorrowingService borrowingService) {
        this.borrowingService = borrowingService;
    }

    @PostMapping
    public ResponseEntity<Borrowing> create(@RequestBody Borrowing borrowing) {
        Borrowing saved = borrowingService.create(borrowing);
        return saved != null ?
                ResponseEntity.ok(saved) :
                ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Borrowing> getById(@PathVariable("id") Long id) {
        Borrowing borrowing = borrowingService.getById(id);
        return borrowing == null ?
                ResponseEntity.notFound().build() :
                ResponseEntity.ok(borrowing);
    }

    @GetMapping
    public ResponseEntity<List<Borrowing>> getAll() {
        return ResponseEntity.ok(borrowingService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Borrowing> update(@PathVariable("id") Long id, @RequestBody Borrowing borrowing) {
        borrowing.setId(id);
        Borrowing updated = borrowingService.update(borrowing);
        return updated != null ?
                ResponseEntity.ok(updated) :
                ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        Borrowing deleted = borrowingService.delete(id);
        return deleted != null ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/receipt")
    public ResponseEntity<BorrowingReceipt> getReceipt(@PathVariable("id") Long id) {
        Borrowing borrowing = borrowingService.getById(id);
        if (borrowing == null) return ResponseEntity.notFound().build();

        BorrowingReceipt receipt = BorrowingReceipt.builder()
                .borrowing(borrowing)
                .user(borrowing.getUser())
                .game(borrowing.getGame())
                .build();

        return ResponseEntity.ok(receipt);
    }
}
