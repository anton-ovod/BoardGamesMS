package org.example.modules.users;

import org.example.builders.UserReport;
import org.example.models.User;
import org.example.modules.borrowings.BorrowingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserService service;
    private final BorrowingService borrowingService;

    public UserController(UserService service, BorrowingService borrowingService) {
        this.service = service;
        this.borrowingService = borrowingService;
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        User created = service.create(user);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> read(@PathVariable("id") Long id) {
        User user = service.read(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<User>> readAll() {
        return ResponseEntity.ok(service.readAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable("id") Long id, @RequestBody User user) {
        user.setId(id);
        User updated = service.update(user);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        int rows = service.delete(id);
        return rows > 0 ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/report")
    public ResponseEntity<?> getReport(@PathVariable("id") Long id) {
        User user = service.read(id);

        try {
            var borrowings = borrowingService.getByUserId(id);
            UserReport report = UserReport.builder()
                    .user(user)
                    .borrowings(borrowings)
                    .build();

            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
