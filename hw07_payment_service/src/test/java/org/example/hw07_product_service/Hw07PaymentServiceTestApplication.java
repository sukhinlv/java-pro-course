package org.example.hw07_product_service;


import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * С помощью этого класса можно запустить приложение с настроенными как для тестов Тестконтейнерами.
 */
class Hw07PaymentServiceTestApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Hw07PaymentServiceApplication.class).run(args);
    }
}
