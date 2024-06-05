package org.example.hw07_product_service;

import org.jetbrains.annotations.NotNull;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Stream;

import static org.example.hw07_product_service.utils.FileUtils.loadFileFromClasspath;

/**
 * Поднимает тестконтейнеры и устанавливает их параметры в application.yml.
 * Заполняет БД и так далее.
 *
 * @see <a href="https://maciejwalkowiak.com/blog/testcontainers-spring-boot-setup/">The best way to use Testcontainers with Spring Boot</a>
 */
public class TestcontainersInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public static final String POSTGRES_IMAGE = "postgres:15-alpine";

    public static final PostgreSQLContainer<?> POSTGRES_CONTAINER =
            new PostgreSQLContainer<>(DockerImageName.parse(POSTGRES_IMAGE));

    @Override
    public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
        Startables.deepStart(
                POSTGRES_CONTAINER
        ).join();

        Runtime.getRuntime().addShutdownHook(new Thread(POSTGRES_CONTAINER::close));

        initProperties(applicationContext);
        initDb();
    }

    private static void initProperties(ConfigurableApplicationContext applicationContext) {
        TestPropertyValues.of(
                "spring.datasource.url: " + POSTGRES_CONTAINER.getJdbcUrl(),
                "spring.datasource.username: " + POSTGRES_CONTAINER.getUsername(),
                "spring.datasource.password: " + POSTGRES_CONTAINER.getPassword()
        ).applyTo(applicationContext.getEnvironment());
    }

    private static void initDb() {
        PGSimpleDataSource ds = getPgSimpleDataSource();
        try (Connection connection = ds.getConnection()) {
            List.of(
                    "create_users.sql",
                    "create_products.sql"
            ).forEach(script -> executeScript(script, connection));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static @NotNull PGSimpleDataSource getPgSimpleDataSource() {
        PGSimpleDataSource ds = new PGSimpleDataSource();
        ds.setServerNames(new String[]{POSTGRES_CONTAINER.getHost()});
        ds.setDatabaseName(POSTGRES_CONTAINER.getDatabaseName());
        ds.setPortNumbers(Stream.of(POSTGRES_CONTAINER.getFirstMappedPort())
                .mapToInt(Integer::intValue)
                .toArray());
        ds.setUser(POSTGRES_CONTAINER.getUsername());
        ds.setPassword(POSTGRES_CONTAINER.getPassword());
        return ds;
    }

    private static void executeScript(String script, Connection connection) {
        String sql = loadFileFromClasspath(script);
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
