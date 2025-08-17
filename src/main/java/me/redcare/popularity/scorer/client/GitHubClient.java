package me.redcare.popularity.scorer.client;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import me.redcare.popularity.scorer.client.dto.GitHubSearchResponse;
import me.redcare.popularity.scorer.config.GitHubConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Client responsible for interacting with the GitHub Search API to fetch repository data.
 * <p>
 * Uses non-blocking WebClient and applies a circuit breaker for resilience.
 */
@Component
public class GitHubClient {

    private static final Logger logger = LoggerFactory.getLogger(GitHubClient.class);

    private final WebClient webClient;
    private final CircuitBreaker circuitBreaker;

    public GitHubClient(WebClient.Builder webClientBuilder, GitHubConfig gitHubConfig, CircuitBreakerRegistry circuitBreakerRegistry) {
        this.webClient = webClientBuilder
                .filter((request, next) -> {
                    logger.debug("WebClient Request: {} {}", request.method(), request.url());
                    return next.exchange(request);
                }).baseUrl(gitHubConfig.getBaseUrl()).build();
        this.circuitBreaker = circuitBreakerRegistry.circuitBreaker("githubClient");

        circuitBreaker.getEventPublisher()
                .onStateTransition(event ->
                        logger.info("CircuitBreaker State Changed: {}", event.getStateTransition()));
    }

    /**
     * Searches public GitHub repositories based on a query string.
     *
     * @param query Search query (supports full GitHub search syntax)
     * @param page  Page number to fetch (1-based index)
     * @param perPage  Number of results per page
     * @return Mono emitting the search response
     */
    public Mono<GitHubSearchResponse> searchRepositories(String query, int page, int perPage) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search/repositories")
                        .queryParam("q", query)
                        .queryParam("page", page)
                        .queryParam("per_page", perPage)
                        .build())
                .retrieve()
                .bodyToMono(GitHubSearchResponse.class)
                .transformDeferred(CircuitBreakerOperator.of(circuitBreaker))
                .doOnSuccess(response -> logger.info("GitHub client returned {} items", response.items() != null ? response.items().size() : 0))
                .doOnError(error -> logger.error("GitHub API call failed", error));
    }
}
