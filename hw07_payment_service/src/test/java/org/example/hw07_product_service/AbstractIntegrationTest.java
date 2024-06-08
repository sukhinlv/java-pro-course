package org.example.hw07_product_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;
import org.wiremock.integrations.testcontainers.WireMockContainer;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
public abstract class AbstractIntegrationTest {

    @Autowired
    ObjectMapper objectMapper;

    @Value("${server.port}")
    int serverPort;

    private static final String WIREMOCK_IMAGE = "wiremock/wiremock:3.5.2-alpine";
    public static final WireMockContainer WM_PRODUCT_SERVICE_CONTAINER = prepareWireMockHttpsContainer();
    public static WireMock wmProductService;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost:" + serverPort;
    }

    @DynamicPropertySource
    static void initProperties(DynamicPropertyRegistry registry) {
        Startables.deepStart(
                WM_PRODUCT_SERVICE_CONTAINER
        ).join();
        wmProductService = prepareWireMockClient(WM_PRODUCT_SERVICE_CONTAINER);

        registry.add("product-service.base-url", WM_PRODUCT_SERVICE_CONTAINER::getBaseUrl);
    }

    @NotNull
    private static WireMock prepareWireMockClient(WireMockContainer container) {
        return new WireMock("http", container.getHost(), container.getFirstMappedPort());
    }

    private static WireMockContainer prepareWireMockHttpsContainer() {
        return new WireMockContainer(DockerImageName.parse(WIREMOCK_IMAGE));
    }
}
