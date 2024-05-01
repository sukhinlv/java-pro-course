package org.example.service;

import org.example.dao.ProductRepository;
import org.example.dao.model.Product;
import org.example.error.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findByUserId(long userId) {
        return productRepository.findAllByUserId(userId);
    }

    public Product findById(long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product with id %d not found".formatted(id)));
    }
}
