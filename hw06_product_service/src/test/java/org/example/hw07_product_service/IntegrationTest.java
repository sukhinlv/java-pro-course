package org.example.hw07_product_service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.restassured.http.ContentType;
import org.example.hw07_product_service.dao.repository.ProductRepository;
import org.example.product_service.dao.model.generated.PaymentDto;
import org.example.product_service.dao.model.generated.ProductDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.example.hw07_product_service.utils.FileUtils.loadFileFromClasspath;

class IntegrationTest extends AbstractIntegrationTest {

    @Autowired
    ProductRepository productRepository;

    @Test
    void should_get_all_products_by_user_id() throws JsonProcessingException {
        // Given, When
        String actualResponseBody = given()
                .contentType(ContentType.JSON)
                .basePath("/products/for-user")
                .queryParam("userId", 125L)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .extract().asPrettyString();

        // Then
        List<ProductDto> expectedDto = objectMapper.readValue(
                loadFileFromClasspath("json/products.json"), new TypeReference<>() {
                });
        List<ProductDto> actualDto =
                objectMapper.readValue(actualResponseBody, new TypeReference<>() {
                });
        assertThat(actualDto)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expectedDto);
    }

    @Test
    void should_return_error_when_user_does_not_exist() {
        // Given, When, Then
        given()
                .contentType(ContentType.JSON)
                .basePath("/products/for-user")
                .queryParam("userId", 12500L)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(ContentType.JSON);
    }

    @Test
    void should_succeed_made_payment() {
        // Given
        @SuppressWarnings("OptionalGetWithoutIsPresent")
        Long expectedBalance = productRepository.findById(1L).get().getBalance() - 500L;

        // When
        given()
                .contentType(ContentType.JSON)
                .basePath("/payment")
                .body(new PaymentDto(1L, 500L))
                .when()
                .put()
                .then()
                .statusCode(HttpStatus.OK.value());

        // Then
        @SuppressWarnings("OptionalGetWithoutIsPresent")
        Long actualBalance = productRepository.findById(1L).get().getBalance();
        assertThat(actualBalance).isEqualTo(expectedBalance);
    }

    @Test
    void should_return_error_when_product_does_not_exist() {
        // Given, When, Then
        given()
                .contentType(ContentType.JSON)
                .basePath("/payment")
                .body(new PaymentDto(123456L, 1000L))
                .when()
                .put()
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(ContentType.JSON);
    }

    @Test
    void should_return_error_when_insufficient_funds() {
        // Given, When, Then
        given()
                .contentType(ContentType.JSON)
                .basePath("/payment")
                .body(new PaymentDto(1L, 1_000_000L))
                .when()
                .put()
                .then()
                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .contentType(ContentType.JSON);
    }
}
