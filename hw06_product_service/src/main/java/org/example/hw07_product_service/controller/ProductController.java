package org.example.hw07_product_service.controller;

import lombok.RequiredArgsConstructor;
import org.example.hw07_product_service.service.ProductService;
import org.example.product_service.api.ProductControllerApi;
import org.example.product_service.dao.model.generated.ProductDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController implements ProductControllerApi {

    private final ProductService productService;

    @Override
    public ResponseEntity<List<ProductDto>> getUserProducts(Long userId) {
        return ResponseEntity.ok(productService.getUserProducts(userId));
    }
}
