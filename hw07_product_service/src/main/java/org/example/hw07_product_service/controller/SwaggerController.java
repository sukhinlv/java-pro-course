package org.example.hw07_product_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.example.hw07_product_service.utils.FileUtils.loadFileFromClasspath;

@RestController
public class SwaggerController {

    @GetMapping("/product-service-rest-v1.yaml")
    public String productServiceRestV1() {
        return loadFileFromClasspath("api/product-service-rest-v1.yaml");
    }

}
