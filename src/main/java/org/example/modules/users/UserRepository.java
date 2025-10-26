package org.example.modules.users;

import org.example.enums.UserRole;
import org.example.models.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbc;

    public UserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<User> mapper = (rs, rowNum) -> new User(
            rs.getLong("id"),
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getString("email"),
            UserRole.valueOf(rs.getString("role")),
            rs.getBoolean("active")
    );

    public User create(User user) {
        Long id = jdbc.queryForObject(
                "INSERT INTO users(first_name, last_name, email, role, active) VALUES (?, ?, ?, ?, ?) RETURNING id",
                Long.class,
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole().name(),
                user.getActive()
        );
        return new User(id, user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole(), user.getActive());
    }

    public User read(Long id) {
        List<User> users = jdbc.query("SELECT * FROM users WHERE id = ?", mapper, id);
        return users.isEmpty() ? null : users.get(0);
    }

    public User update(User user) {
        int rows = jdbc.update(
                "UPDATE users SET first_name = ?, last_name = ?, email = ?, role = ?, active = ? WHERE id = ?",
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole().name(),
                user.getActive(),
                user.getId()
        );

        if (rows == 0) return null;
        return read(user.getId());
    }

    public int delete(Long id) {
        return jdbc.update("DELETE FROM users WHERE id = ?", id);
    }

    public List<User> readAll() {
        return jdbc.query("SELECT * FROM users", mapper);
    }
}
