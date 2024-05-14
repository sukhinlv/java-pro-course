package org.example.hw06_product_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.hw06_product_service.dao.model.Product;
import org.example.hw06_product_service.dao.repository.ProductRepository;
import org.example.hw06_product_service.exception.InsufficientFunds;
import org.example.hw06_product_service.exception.ProductNotFound;
import org.example.product_service.dao.model.generated.PaymentDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final ProductRepository productRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    public void putPayment(PaymentDto paymentDto) {
        Long productId = paymentDto.getProductId();
        log.info("Make payment for product with id %d".formatted(productId));

        Product product = productRepository
                .findById(productId)
                .orElseThrow(() -> new ProductNotFound("Product with id %d not found".formatted(productId)));

        if (product.getBalance() < paymentDto.getAmount()) {
            throw new InsufficientFunds("Insufficient funds on product with id %d to make payment".formatted(productId));
        }

        product.setBalance(product.getBalance() - paymentDto.getAmount());
    }
}
