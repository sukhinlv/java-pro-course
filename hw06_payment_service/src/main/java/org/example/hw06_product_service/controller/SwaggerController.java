package org.example.hw06_product_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.example.hw06_product_service.utils.FileUtils.loadFileFromClasspath;

@RestController
public class SwaggerController {

    @GetMapping("/payment-service-rest-v1.yaml")
    public String paymentServiceRestV1() {
        return loadFileFromClasspath("api/payment-service-rest-v1.yaml");
    }
}
