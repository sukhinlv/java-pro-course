package org.example;

import org.jetbrains.annotations.NotNull;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.CompositeDatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

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
        populateDb();
    }

    private static void initProperties(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();

        Map<String, Object> myProperties = new HashMap<>();
        myProperties.put("spring.datasource.url", POSTGRES_CONTAINER.getJdbcUrl());
        myProperties.put("spring.datasource.username", POSTGRES_CONTAINER.getUsername());
        myProperties.put("spring.datasource.password", POSTGRES_CONTAINER.getPassword());

        MapPropertySource mapPropertySource = new MapPropertySource("testProperties", myProperties);
        MutablePropertySources propertySources = environment.getPropertySources();
        propertySources.addFirst(mapPropertySource);
    }

    private void populateDb() {
        try {
            CompositeDatabasePopulator populator = new CompositeDatabasePopulator();
            populator.addPopulators(
                    new ResourceDatabasePopulator(new ClassPathResource("init_db.sql"))
            );
            PGSimpleDataSource ds = new PGSimpleDataSource();
            ds.setServerNames(new String[]{POSTGRES_CONTAINER.getHost()});
            ds.setDatabaseName(POSTGRES_CONTAINER.getDatabaseName());
            ds.setPortNumbers(Stream.of(POSTGRES_CONTAINER.getFirstMappedPort())
                    .mapToInt(Integer::intValue)
                    .toArray());
            ds.setUser(POSTGRES_CONTAINER.getUsername());
            ds.setPassword(POSTGRES_CONTAINER.getPassword());
            try (Connection connection = ds.getConnection()) {
                populator.populate(connection);
            }
        } catch (SQLException | ScriptException e) {
            throw new RuntimeException(e);
        }
    }
}
