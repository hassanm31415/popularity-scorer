package me.redcare.popularity.scorer.client;

class GitHubClientUnitTest {

    /*private GitHubClient gitHubClient;
    private WebClient webClient;
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    void setUp() {
        webClient = mock(WebClient.class);
        requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        responseSpec = mock(WebClient.ResponseSpec.class);

        gitHubClient = new GitHubClient(builder -> webClient);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void searchRepositories_ReturnsSuccessResponse() {
        GitHubSearchResponse expectedResponse = new GitHubSearchResponse();
        when(responseSpec.bodyToMono(GitHubSearchResponse.class))
                .thenReturn(Mono.just(expectedResponse));

        StepVerifier.create(gitHubClient.searchRepositories("test", 1, 5))
                .expectNext(expectedResponse)
                .verifyComplete();
    }

    @Test
    void searchRepositories_HandlesApiFailure() {
        when(responseSpec.bodyToMono(GitHubSearchResponse.class))
                .thenReturn(Mono.error(new RuntimeException("API failure")));

        StepVerifier.create(gitHubClient.searchRepositories("test", 1, 5))
                .expectError(RuntimeException.class)
                .verify();
    }*/
}
