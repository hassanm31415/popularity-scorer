package me.redcare.popularity.scorer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.redcare.popularity.scorer.controller.dto.RepositoryScoringRequest;
import me.redcare.popularity.scorer.controller.dto.RepositoryScoringResponse;
import me.redcare.popularity.scorer.service.RepositoryScoringService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Tag(name = "Popularity Scorer ", description = "Score GitHub repositories with customizable search")
@Validated
@RestController
@RequestMapping("/api/repositories")
public class RepositoriesScorerController {

    private static final Logger logger = LoggerFactory.getLogger(RepositoriesScorerController.class);

    private final RepositoryScoringService repositoryService;

    public RepositoriesScorerController(RepositoryScoringService repositoryService){
        this.repositoryService = repositoryService;
    }

    @Operation(
            summary = "Score repositories with customizable search",
            description = """
                This endpoint score public GitHub repositories using a flexible query and applies popularity score.

                ### Scoring Strategies:
                
                - **default**: Combines normalized metrics (forks, stars, recency) with weights, final score normalized to 0-100.
                - **simple**: Basic sum of stars and forks plus 100 points if repository was updated within a year.

                """
    )
    @GetMapping("/scores")
    public Mono<RepositoryScoringResponse> scoreRepositories(@ParameterObject @ModelAttribute @Validated RepositoryScoringRequest request) {
        logger.info("Received repository scoring request: {}", request);
        return repositoryService.scoreRepositories(request);
    }
}
