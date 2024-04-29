package org.example.service;

import org.example.dao.ProductDao;
import org.example.dao.model.Product;
import org.example.error.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<Product> findByUserId(long userId) {
        return productDao.findAllByUserId(userId);
    }

    public Product findById(long id) {
        return productDao.findById(id)
                .orElseThrow(() -> new NotFoundException("Product with id %d not found".formatted(id)));
    }
}
