package org.example.modules.users;

import org.example.models.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repo, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.repo = repo;
    }

    public User create(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
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
