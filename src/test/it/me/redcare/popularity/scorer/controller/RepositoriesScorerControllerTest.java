package me.redcare.popularity.scorer.controller;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.cloud.contract.wiremock.WireMockSpring.options;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureWireMock(port = 0)
class RepositoriesScorerControllerTest {

    @LocalServerPort
    private int port;

    static final int WIREMOCK_PORT = 8888;

    static WireMockServer wireMockServer;

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("github.base-url", () -> "http://localhost:" + WIREMOCK_PORT);
    }

    @BeforeAll
    void startWireMock() {
        wireMockServer = new WireMockServer(options().port(WIREMOCK_PORT));
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());
    }

    @AfterAll
    void stopWireMock() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    @Autowired
    private WebTestClient webTestClient;


    @Test
    void scoreRepositories_ValidRequest() {
        WireMock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/search/repositories"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("github_spring_response.json")));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/repositories/scores")
                        .queryParam("query", "spring")
                        .queryParam("language", "java")
                        .queryParam("scorer", "default")
                        .queryParam("page", "1")
                        .queryParam("perPage", "20")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.totalCount").isEqualTo(484945);
    }




    @Test
    void scoreRepositories_InValidRequest() {
        WireMock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/search/repositories"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("github_spring_response.json")));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/repositories/scores")
                        .queryParam("query", "")
                        .queryParam("language", "java")
                        .queryParam("scorer", "default")
                        .queryParam("page", "1")
                        .queryParam("perPage", "20")
                        .build())
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.BAD_REQUEST);

    }

}