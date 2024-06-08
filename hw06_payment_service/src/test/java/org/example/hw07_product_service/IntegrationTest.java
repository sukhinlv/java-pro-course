package org.example.hw07_product_service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.restassured.http.ContentType;
import org.example.payment_service.dao.model.generated.PaymentDto;
import org.example.payment_service.dao.model.generated.ProductDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.notFound;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.status;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.example.hw07_product_service.utils.FileUtils.loadFileFromClasspath;

class IntegrationTest extends AbstractIntegrationTest {

    @AfterEach
    void tearDown() {
        wmProductService.removeMappings();
    }

    @Test
    void should_get_all_products_by_user_id() throws JsonProcessingException {
        // Given
        stubSuccessfullyReturnProducts();

        // When
        String actualResponseBody = given()
                .contentType(ContentType.JSON)
                .basePath("/products/for-user")
                .queryParam("userId", 125)
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
        // Given
        stubReturnErrorForUserId12500();

        // When, Then
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
    void should_succeed_made_payment() throws JsonProcessingException {
        // Given
        stubSuccessfullyMakePayment();

        // When, Then
        given()
                .contentType(ContentType.JSON)
                .basePath("/payment")
                .body(new PaymentDto(1L, 500L))
                .when()
                .put()
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void should_return_error_when_product_does_not_exist() throws JsonProcessingException {
        // Given
        stubReturnErrorForProductId123456();

        // When, Then
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
    void should_return_error_when_insufficient_funds() throws JsonProcessingException {
        // Given
        stubReturnErrorWhenInsufficientFunds();

        // When, Then
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


    // *****************************************************************************************************************
    // ЗАГЛУШКИ WIREMOCK
    // *****************************************************************************************************************

    void stubSuccessfullyReturnProducts() {
        wmProductService.register(get(urlPathEqualTo("/products/for-user"))
                .withQueryParam("userId", equalTo("125"))
                .willReturn(okJson(loadFileFromClasspath("json/products.json")))
        );
    }

    void stubReturnErrorForUserId12500() {
        wmProductService.register(get(urlPathEqualTo("/products/for-user"))
                .withQueryParam("userId", equalTo("12500"))
                .willReturn(notFound())
        );
    }

    void stubSuccessfullyMakePayment() throws JsonProcessingException {
        wmProductService.register(put(urlPathEqualTo("/payment"))
                .withRequestBody(containing(objectMapper.writeValueAsString(new PaymentDto(1L, 500L))))
                .willReturn(ok())
        );
    }

    void stubReturnErrorForProductId123456() throws JsonProcessingException {
        wmProductService.register(put(urlPathEqualTo("/payment"))
                .withRequestBody(containing(objectMapper.writeValueAsString(new PaymentDto(123456L, 1000L))))
                .willReturn(notFound())
        );
    }

    void stubReturnErrorWhenInsufficientFunds() throws JsonProcessingException {
        wmProductService.register(put(urlPathEqualTo("/payment"))
                .withRequestBody(containing(objectMapper.writeValueAsString(new PaymentDto(1L, 1_000_000L))))
                .willReturn(status(HttpStatus.UNPROCESSABLE_ENTITY.value()))
        );
    }
}
