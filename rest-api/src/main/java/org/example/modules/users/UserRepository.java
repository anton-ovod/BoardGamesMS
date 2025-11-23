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
            rs.getBoolean("active"),
            rs.getString("password")
    );

    public User create(User user) {
        Long id = jdbc.queryForObject(
                "INSERT INTO users(first_name, last_name, email, role, active, password) VALUES (?, ?, ?, ?, ?, ?) RETURNING id",
                Long.class,
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole().name(),
                user.getActive(),
                user.getPassword()
        );
        return new User(id, user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole(), user.getActive(), user.getPassword());
    }

    public User read(Long id) {
        List<User> users = jdbc.query("SELECT * FROM users WHERE id = ?", mapper, id);
        return users.isEmpty() ? null : users.get(0);
    }

    public User readByEmail(String email) {
        List<User> users = jdbc.query("SELECT * FROM users WHERE email = ?", mapper, email);
        return users.isEmpty() ? null : users.get(0);
    }

    public User update(User user) {
        int rows = jdbc.update(
                "UPDATE users SET first_name = ?, last_name = ?, email = ?, role = ?, active = ?, password = ? WHERE id = ?",
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole().name(),
                user.getActive(),
                user.getPassword(),
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
