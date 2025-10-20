package org.example.modules.users;

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
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getString("email"),
            org.example.enums.UserRole.valueOf(rs.getString("role"))
    );

    public List<User> findAll() {
        return jdbc.query("SELECT * FROM users WHERE active = TRUE", mapper);
    }

    public User findById(Long id) {
        return jdbc.queryForObject("SELECT * FROM users WHERE id = ?", mapper, id);
    }

    public int save(User user) {
        return jdbc.update("INSERT INTO users(first_name, last_name, email, role) VALUES (?, ?, ?, ?)",
                user.getFullName().split(" ")[0],
                user.getFullName().split(" ")[1],
                user.toString(),
                user.toString());
    }

    public int updateEmail(Long id, String newEmail) {
        return jdbc.update("UPDATE users SET email = ? WHERE id = ?", newEmail, id);
    }

    public int deactivate(Long id) {
        return jdbc.update("UPDATE users SET active = FALSE WHERE id = ?", id);
    }

    public int delete(Long id) {
        return jdbc.update("DELETE FROM users WHERE id = ?", id);
    }
}
