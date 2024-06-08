package org.example.hw07_product_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.hw07_product_service.dao.mapper.ProductMapper;
import org.example.hw07_product_service.dao.repository.ProductRepository;
import org.example.hw07_product_service.dao.repository.UserRepository;
import org.example.hw07_product_service.exception.UserNotFound;
import org.example.product_service.dao.model.generated.ProductDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductMapper productMapper;

    public List<ProductDto> getUserProducts(long userId) {
        log.info("Get list of products for user with id %d".formatted(userId));
        if (!userRepository.existsById(userId)) {
            throw new UserNotFound("User with id %d not found".formatted(userId));
        }
        return productMapper.productsToProductDtos(productRepository.findBUserId(userId));
    }
}
