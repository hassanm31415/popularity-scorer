package me.redcare.popularity.scorer.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record RepositoryScoringResponse(
        @Schema(description = "Total number of repositories matching the search criteria", example = "123456")
        Integer totalCount,
        @Schema(description = "List of scored repositories returned in the search result")
        List<ScoredRepositoryDTO> items) {
}
