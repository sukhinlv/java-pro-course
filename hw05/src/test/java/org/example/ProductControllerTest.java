package org.example;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.restassured.http.ContentType;
import org.example.controller.ProductController;
import org.example.dao.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.example.test_utils.FileUtils.loadFileFromClasspath;

class ProductControllerTest extends AbstractIntegrationTest {

    @Test
    void should_get_product_by_id() throws JsonProcessingException {
        // Given, When
        String actualResponseBody = given()
                .contentType(ContentType.JSON)
                .basePath(ProductController.BASE_PATH + "/")
                .queryParam("id", 3L)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .extract().asPrettyString();

        // Then
        Product expectedDto = objectMapper.readValue(
                loadFileFromClasspath("json/product.json"), Product.class);
        Product actualDto =
                objectMapper.readValue(actualResponseBody, Product.class);
        assertThat(actualDto)
                .usingRecursiveComparison()
                .isEqualTo(expectedDto);
    }

    @Test
    void should_get_all_products_by_user_id() throws JsonProcessingException {
        // Given, When
        String actualResponseBody = given()
                .contentType(ContentType.JSON)
                .basePath(ProductController.BASE_PATH + "/by-user")
                .queryParam("userId", 125L)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .extract().asPrettyString();

        // Then
        List<Product> expectedDto = objectMapper.readValue(
                loadFileFromClasspath("json/products.json"), new TypeReference<>() {
                });
        List<Product> actualDto =
                objectMapper.readValue(actualResponseBody, new TypeReference<>() {
                });
        assertThat(actualDto)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expectedDto);
    }
}
