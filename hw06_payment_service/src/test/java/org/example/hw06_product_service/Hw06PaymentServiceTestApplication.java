package org.example.hw06_product_service;


import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * С помощью этого класса можно запустить приложение с настроенными как для тестов Тестконтейнерами.
 */
class Hw06PaymentServiceTestApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Hw06PaymentServiceApplication.class).run(args);
    }
}
