package org.example.hw07_product_service;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Stream;

import static org.example.hw07_product_service.TestcontainersInitializer.POSTGRES_CONTAINER;
import static org.example.hw07_product_service.utils.FileUtils.loadFileFromClasspath;

/**
 * С помощью этого класса можно запустить приложение с настроенными как для тестов Тестконтейнерами.
 */
@Slf4j
class Hw07ProductServiceTestApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Hw07ProductServiceApplication.class)
                .initializers(applicationContext -> new TestcontainersInitializer().initialize(applicationContext))
                .listeners(event -> {
                    if (event instanceof ApplicationReadyEvent) {
                        initDb();
                    }
                })
                .run(args);
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
        log.info("Executing script {}", script);
        String sql = loadFileFromClasspath(script);
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
