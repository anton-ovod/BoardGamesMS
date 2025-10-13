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

    public List<User> getAll() { return repo.findAll(); }
    public User get(Long id) { return repo.findById(id); }
    public void create(User user) { repo.save(user); }
    public void deactivate(Long id) { repo.deactivate(id); }
    public void delete(Long id) { repo.delete(id); }
}
