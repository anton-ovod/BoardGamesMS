package org.example.modules.users;

import org.example.models.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public User create(User user) {
        return repo.create(user);
    }

    public User read(Long id) {
        return repo.read(id);
    }

    public List<User> readAll() {
        return repo.readAll();
    }

    public User update(User user) {
        return repo.update(user);
    }

    public int delete(Long id) {
        return repo.delete(id);
    }
}
