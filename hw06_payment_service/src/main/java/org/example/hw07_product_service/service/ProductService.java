package org.example.hw07_product_service.service;

import lombok.extern.slf4j.Slf4j;
import org.example.hw07_product_service.exception.ProductServiceException;
import org.example.hw07_product_service.mapper.ProductMapper;
import org.example.payment_service.dao.model.generated.ProductDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

import static java.util.Objects.nonNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@Service
public class ProductService {

    private final RestClient restClient;
    private final ProductMapper productMapper;

    public ProductService(
            @Qualifier("productServiceRestClient") RestClient restClient,
            ProductMapper productMapper
    ) {
        this.restClient = restClient;
        this.productMapper = productMapper;
    }

    public List<ProductDto> getUserProducts(long userId) {
        log.info("Try to get list of products for user with id %d".formatted(userId));
        List<org.example.product_service.dao.model.generated.ProductDto> productsFromService = restClient.get()
                .uri("/products/for-user?userId={userId}", userId)
                .accept(APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    throw new ProductServiceException(
                            "Failed to get list of products. Product service error %d %s. Message: %s".formatted(
                                    response.getStatusCode().value(), response.getStatusText(), new String(response.getBody().readAllBytes())),
                            response.getStatusCode());
                })
                .body(new ParameterizedTypeReference<>() {
                });
        List<ProductDto> productDtos = nonNull(productsFromService)
                ? productsFromService.stream().map(productMapper::productProductDtoToProductDto).toList()
                : List.of();
        log.info("Got list of products for user with id %d".formatted(userId));
        return productDtos;
    }
}
