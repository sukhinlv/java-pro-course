package org.example.hw07_product_service;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.example.hw07_product_service.utils.FileUtils.loadFileFromClasspath;

class AppCommonsTest extends AbstractIntegrationTest {

    @Value("${springdoc.swagger-ui.url}")
    String swaggerSpec;

    @Test
    void should_load_swagger_specification() {
        // Given
        String expectedSpec = loadFileFromClasspath("api" + swaggerSpec);

        // When
        String actualSpec = given()
                .contentType(ContentType.JSON)
                .basePath(swaggerSpec)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().asString();

        // Then
        assertThat(actualSpec).isEqualToNormalizingWhitespace(expectedSpec);
    }

    @Test
    void should_throw_no_handler_found() {
        // Given, When, Then
        given()
                .contentType(ContentType.JSON)
                .basePath("/payment111")
                .when()
                .put()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .contentType(ContentType.JSON);
    }

    @Test
    void should_throw_missing_servlet_request_parameter() {
        // Given, When, Then
        given()
                .contentType(ContentType.JSON)
                .basePath("/products/for-user")
                .queryParam("userId11", 12500L)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .contentType(ContentType.JSON);
    }
}
