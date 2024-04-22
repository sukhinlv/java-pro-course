package org.example.dao;

import org.example.annotations.Table;
import org.example.dao.model.User;
import org.example.error.DataAccessException;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class UserDao implements Dao<User, Long> {

    private final DataSource dataSource;
    private final String tableName;

    public UserDao(DataSource dataSource) {
        this.dataSource = dataSource;
        Class<User> aClass = User.class;
        if (!aClass.isAnnotationPresent(Table.class)) {
            throw new RuntimeException("Table " + aClass.getSimpleName() + " is not annotated with @Table");
        }
        this.tableName = aClass.getAnnotation(Table.class).name();
    }

    @Override
    public Optional<User> findById(Long id) {
        Objects.requireNonNull(id);
        String sql = "SELECT * FROM %s WHERE id = ?".formatted(tableName);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(getUserFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
        return Optional.empty();
    }

    @Override
    public void save(User user) {
        Objects.requireNonNull(user);
        String sql = "INSERT INTO %s (id, username) VALUES (?, ?) ON CONFLICT (id) DO UPDATE SET username = ?".formatted(tableName);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, user.getId());
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getUsername());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public void deleteById(Long id) {
        Objects.requireNonNull(id);
        String sql = "DELETE FROM %s WHERE id = ?".formatted(tableName);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM %s".formatted(tableName);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);

             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                users.add(getUserFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
        return users;
    }

    private static User getUserFromResultSet(ResultSet resultSet) throws SQLException {
        return new User(resultSet.getLong("id"), resultSet.getString("username"));
    }
}
