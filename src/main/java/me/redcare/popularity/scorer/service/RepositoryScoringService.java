package me.redcare.popularity.scorer.service;

import me.redcare.popularity.scorer.controller.dto.RepositoryScoringRequest;
import me.redcare.popularity.scorer.controller.dto.RepositoryScoringResponse;
import reactor.core.publisher.Mono;

public interface RepositoryScoringService {
    Mono<RepositoryScoringResponse> scoreRepositories(RepositoryScoringRequest request);
}
