package me.redcare.popularity.scorer.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import me.redcare.popularity.scorer.config.GitHubConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

class GitHubClientTestIT {

    private static WireMockServer wireMockServer;
    private GitHubClient gitHubClient;

    @BeforeAll
    static void startWireMock() {
        wireMockServer = new WireMockServer(
                WireMockConfiguration.options().dynamicPort()
                        .usingFilesUnderDirectory("src/test/resources")
        );
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());
    }

    @AfterAll
    static void stopWireMock() {
        wireMockServer.stop();
    }

    @BeforeEach
    void setUp() {

        String baseUrl = "http://localhost:" + wireMockServer.port();
        WebClient.Builder builder = WebClient.builder()
                .baseUrl(baseUrl);

        GitHubConfig gitHubConfig = new GitHubConfig();
        gitHubConfig.setBaseUrl(baseUrl);
        gitHubClient = new GitHubClient(builder, gitHubConfig);
    }

    @Test
    void searchRepositories_returnsStubbedGitHubData() throws Exception {
        // Prepare stub
        stubFor(get(urlPathEqualTo("/search/repositories"))
                .withQueryParam("q", equalTo("spring"))
                .withQueryParam("page", equalTo("1"))
                .withQueryParam("per_page", equalTo("20"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("github_spring_response.json")));

        // Execute WebClient call
        StepVerifier.create(gitHubClient.searchRepositories("spring", 1, 20))
                .expectNextMatches(response ->
                        response.totalCount() == 484945
                )
                .verifyComplete();
    }

}