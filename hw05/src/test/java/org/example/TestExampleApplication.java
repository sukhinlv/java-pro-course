package org.example;

import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * С помощью этого класса можно запустить приложение с настроенными как для тестов Тестконтейнерами.
 */
public class TestExampleApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ExampleApplication.class)
                .initializers(applicationContext -> new TestcontainersInitializer().initialize(applicationContext))
                .run(args);
    }
}
