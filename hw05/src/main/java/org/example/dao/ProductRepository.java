package org.example.dao;

import org.example.annotations.Table;
import org.example.dao.model.Product;
import org.example.dao.model.ProductType;
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
public class ProductRepository implements AbstractRepository<Product, Long> {

    private final DataSource dataSource;
    private final String tableName;

    public ProductRepository(DataSource dataSource) {
        this.dataSource = dataSource;
        Class<Product> aClass = Product.class;
        if (!aClass.isAnnotationPresent(Table.class)) {
            throw new RuntimeException("Table " + aClass.getSimpleName() + " is not annotated with @Table");
        }
        this.tableName = aClass.getAnnotation(Table.class).name();
    }

    @Override
    public Optional<Product> findById(Long id) {
        Objects.requireNonNull(id);
        String sql = "SELECT * FROM %s WHERE id = ?".formatted(tableName);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(getProductFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
        return Optional.empty();
    }

    @Override
    public void save(Product product) {
        Objects.requireNonNull(product);
        String sql = """
                    INSERT INTO %s (id, user_id, account, balance, product_type)
                    VALUES (?, ?, ?, ?, ?)
                    ON CONFLICT (id) DO UPDATE SET user_id = ?, account = ?, balance = ?, product_type = ?
                """
                .formatted(tableName);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            int i = 1;
            statement.setLong(i++, product.getId());
            statement.setLong(i++, product.getUserId());
            statement.setString(i++, product.getAccount());
            statement.setLong(i++, product.getBalance());
            statement.setShort(i++, product.getProductType().getId());
            statement.setLong(i++, product.getUserId());
            statement.setString(i++, product.getAccount());
            statement.setLong(i++, product.getBalance());
            statement.setShort(i++, product.getProductType().getId());
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
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM %s".formatted(tableName);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                products.add(getProductFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
        return products;
    }

    public List<Product> findAllByUserId(long userId) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM %s WHERE user_id = ?".formatted(tableName);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                products.add(getProductFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
        return products;
    }

    private static Product getProductFromResultSet(ResultSet resultSet) throws SQLException {
        return new Product(
                resultSet.getLong("id"),
                resultSet.getLong("user_id"),
                resultSet.getString("account"),
                resultSet.getLong("balance"),
                ProductType.fromValue(resultSet.getShort("product_type"))
        );
    }
}
