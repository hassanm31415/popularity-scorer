package me.redcare.popularity.scorer.service.impl;

import me.redcare.popularity.scorer.client.GitHubClient;
import me.redcare.popularity.scorer.client.dto.GitHubRepoItem;
import me.redcare.popularity.scorer.client.dto.GitHubSearchResponse;
import me.redcare.popularity.scorer.controller.dto.RepositoryScoringRequest;
import me.redcare.popularity.scorer.controller.dto.RepositoryScoringResponse;
import me.redcare.popularity.scorer.controller.dto.ScoredRepositoryDTO;
import me.redcare.popularity.scorer.mapper.RepositoryMapper;
import me.redcare.popularity.scorer.scorer.PopularityScorer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class RepositoryServiceImplTest {

    private static final RepositoryScoringRequest DEFAULT_SCORING_REQUEST = new RepositoryScoringRequest("microservice", "java",
            "2023-01-01", "default", 1, 5);
    private static final RepositoryScoringRequest SCORING_REQUEST_WITH_SIMPLE_SCORER = new RepositoryScoringRequest("microservice", "java",
            null, "simple", 1, 5);

    private static final GitHubRepoItem REPOS_ITEM_1 = new GitHubRepoItem( 123456,"repo1", "https://123.com",
            "Java", 50, 20, "2025-06-11T10:59:49Z", "2025-04-11T10:59:49Z", "2023-04-11T10:59:49Z", 1.0);
    private static final GitHubRepoItem REPOS_ITEM_2 = new GitHubRepoItem( 654321,"repo2", "https://321.com",
            "Java", 70, 80, "2025-06-11T10:59:49Z", "2025-04-11T10:59:49Z", "2023-04-11T10:59:49Z", 1.0);

    private static final ScoredRepositoryDTO SCORED_REPO_1 = new ScoredRepositoryDTO(123456,"repo1", "https://123.com",
            "Java", 50, 20, "2025-06-11T10:59:49Z", "2025-04-11T10:59:49Z", "2023-04-11T10:59:49Z", 50.0);
    private static final ScoredRepositoryDTO SCORED_REPO_2 = new ScoredRepositoryDTO(654321,"repo2", "https://321.com",
            "Java", 70, 80, "2025-06-11T10:59:49Z", "2025-04-11T10:59:49Z", "2023-04-11T10:59:49Z", 50.0);
    private static final ScoredRepositoryDTO SCORED_REPO_SIMPLE = new ScoredRepositoryDTO(123456,"repo1", "https://123.com",
            "Java", 50, 20, "2025-06-11T10:59:49Z", "2025-04-11T10:59:49Z", "2023-04-11T10:59:49Z", 20.0);

    @Mock
    private GitHubClient gitHubClient;

    @Mock
    private RepositoryMapper repositoryMapper;


    private Map<String, PopularityScorer> scorerMap;

    @InjectMocks
    private RepositoryScoringServiceImpl repositoryScoringService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        scorerMap = new HashMap<>();
        PopularityScorer defaultScorer = Mockito.mock(PopularityScorer.class);
        when(defaultScorer.calculateScore(any())).thenReturn(50.0);

        PopularityScorer simpleScorer = Mockito.mock(PopularityScorer.class);
        when(simpleScorer.calculateScore(any())).thenReturn(20.0);
        scorerMap.put("default", defaultScorer);
        scorerMap.put("simple", simpleScorer);

        repositoryScoringService = new RepositoryScoringServiceImpl(scorerMap, gitHubClient, repositoryMapper);
    }

    @Test
    void testScoreRepositories_withValidFilters_returnsRepositories() throws Exception {

        GitHubSearchResponse response = new GitHubSearchResponse(1000, List.of(REPOS_ITEM_1, REPOS_ITEM_2));

        when(gitHubClient.searchRepositories(anyString(), eq(1), eq(5))).thenReturn(Mono.just(response));
        when(repositoryMapper.toDTO(REPOS_ITEM_1, 50.0)).thenReturn(SCORED_REPO_1);
        when(repositoryMapper.toDTO(REPOS_ITEM_2, 50.0)).thenReturn(SCORED_REPO_2);

        Mono<RepositoryScoringResponse> resultMono = repositoryScoringService.scoreRepositories(DEFAULT_SCORING_REQUEST);


        RepositoryScoringResponse repositoryScoringResponse = resultMono.block();
        verify(repositoryMapper).toDTO(REPOS_ITEM_1, 50.0);

        List<ScoredRepositoryDTO> result = repositoryScoringResponse.items();
        assertEquals(2, result.size());
        assertEquals("repo1", result.getFirst().name());

    }

    @Test
    void testScoreRepositories_withEmptyResults_returnsEmptyList() throws Exception {

        GitHubSearchResponse response = new GitHubSearchResponse(0, Collections.emptyList());

        when(gitHubClient.searchRepositories(anyString(), eq(1), eq(5))).thenReturn(Mono.just(response));

        Mono<RepositoryScoringResponse> resultMono = repositoryScoringService.scoreRepositories(DEFAULT_SCORING_REQUEST);

        List<ScoredRepositoryDTO> result = resultMono.block().items();

        assertTrue(result.isEmpty());
    }



    @Test
    void testScoreRepositories_withNonDefaultScorer() throws Exception {

        GitHubSearchResponse response = new GitHubSearchResponse(1000, List.of(REPOS_ITEM_1));

        when(gitHubClient.searchRepositories(anyString(), eq(1), eq(5))).thenReturn(Mono.just(response));
        when(repositoryMapper.toDTO(REPOS_ITEM_1, 20.0)).thenReturn(SCORED_REPO_SIMPLE);

        Mono<RepositoryScoringResponse> resultMono = repositoryScoringService.scoreRepositories(SCORING_REQUEST_WITH_SIMPLE_SCORER);


        RepositoryScoringResponse repositoryScoringResponse = resultMono.block();
        verify(repositoryMapper).toDTO(REPOS_ITEM_1, 20.0);

        List<ScoredRepositoryDTO> result = repositoryScoringResponse.items();
        assertEquals(1, result.size());
        assertEquals(20.0, result.getFirst().popularityScore());

    }


}
