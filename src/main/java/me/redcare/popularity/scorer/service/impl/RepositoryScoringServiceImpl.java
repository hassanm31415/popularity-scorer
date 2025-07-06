package me.redcare.popularity.scorer.service.impl;

import me.redcare.popularity.scorer.client.GitHubClient;
import me.redcare.popularity.scorer.controller.dto.RepositoryScoringRequest;
import me.redcare.popularity.scorer.controller.dto.RepositoryScoringResponse;
import me.redcare.popularity.scorer.controller.dto.ScoredRepositoryDTO;
import me.redcare.popularity.scorer.mapper.RepositoryMapper;
import me.redcare.popularity.scorer.scorer.PopularityScorer;
import me.redcare.popularity.scorer.service.RepositoryScoringService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
public class RepositoryScoringServiceImpl implements RepositoryScoringService {

    private static final Logger logger = LoggerFactory.getLogger(RepositoryScoringServiceImpl.class);

    private final Map<String, PopularityScorer> scorerMap;
    private final GitHubClient gitHubClient;
    private final RepositoryMapper repositoryMapper;

    public RepositoryScoringServiceImpl(Map<String, PopularityScorer> scorerMap, GitHubClient gitHubClient, RepositoryMapper repositoryMapper) {
        this.scorerMap = scorerMap;
        this.gitHubClient = gitHubClient;
        this.repositoryMapper = repositoryMapper;
    }

    /**
     * Searches repositories and applies the specified popularity scoring strategy.
     *
     * @param request The search request and options
     * @return List of repositories with scores
     */
    //@Async("popularityTaskExecutor")
    public Mono<RepositoryScoringResponse> scoreRepositories(RepositoryScoringRequest request) {
        logger.info("Scoring repositories with query: {}", request);

        StringBuilder searchQuery = new StringBuilder(request.query());

        if(!StringUtils.isBlank(request.language())){
            searchQuery.append("+language:").append(request.language());
        }

        if(!StringUtils.isBlank(request.createdAfter())){
            searchQuery.append("+created:>").append(request.createdAfter());
        }

        PopularityScorer scorer = scorerMap.getOrDefault(request.scorer(), scorerMap.get("default"));

        return gitHubClient.searchRepositories(searchQuery.toString(), request.page(), request.perPage())
                .map(response -> {
                    logger.info("Retrieved {} repositories from GitHub", response.items().size());
                    List<ScoredRepositoryDTO> results = Collections.emptyList();
                    results = response.items()
                            .stream()
                            .map(item -> {
                                double score = scorer.calculateScore(item);
                                return repositoryMapper.toDTO(item, score);
                            })
                            .sorted(Comparator.comparingDouble(ScoredRepositoryDTO::popularityScore).reversed()).toList();
                    return new RepositoryScoringResponse(response.totalCount(), results);
                });
    }


}
