package org.example.modules.borrowings;

import org.example.models.Borrowing;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BorrowingService {
    private final BorrowingRepository repository;

    public BorrowingService(BorrowingRepository repository) {
        this.repository = repository;
    }

    public Borrowing create(Borrowing borrowing) {
        return repository.save(borrowing);
    }

    public Borrowing update(Borrowing borrowing) {
        return repository.update(borrowing);
    }

    public Borrowing delete(Long id) {
        return repository.delete(id);
    }

    public Borrowing getById(Long id) {
        return repository.findById(id);
    }

    public List<Borrowing> getAll() {
        return repository.findAll();
    }

    public List<Borrowing> getByUserId(Long userId) {
        return repository.findByUserId(userId);
    }

    public List<Borrowing> getByGameId(Long gameId) {
        return repository.findByGameId(gameId);
    }
}
