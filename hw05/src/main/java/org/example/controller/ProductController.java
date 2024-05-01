package org.example.controller;

import org.example.dao.model.Product;
import org.example.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ProductController.BASE_PATH)
public class ProductController {

    public static final String BASE_PATH = "/product";

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/by-user")
    public List<Product> findByUserId(@RequestParam(value = "userId") long userId) {
        return productService.findByUserId(userId);
    }

    @GetMapping("/")
    public Product findById(@RequestParam(value = "id") long id) {
        return productService.findById(id);
    }
}
