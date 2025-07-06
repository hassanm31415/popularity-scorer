package me.redcare.popularity.scorer.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ScoredRepositoryDTO(
        @Schema(description = "Unique repository ID", example = "123456")
        Integer id,

        @Schema(description = "Repository name", example = "popularity-score-service")
        String name,

        @Schema(description = "Direct URL to the repository", example = "https://github.com/user/repo")
        String url,

        @Schema(description = "Primary programming language", example = "Java")
        String language,

        @Schema(description = "Number of stargazers", example = "150")
        Integer stars,

        @Schema(description = "Number of forks", example = "35")
        Integer forks,

        @Schema(description = "Last update timestamp (ISO 8601 format)", example = "2024-07-01T14:22:05Z")
        String updatedAt,

        @Schema(description = "Last commit timestamp (ISO 8601 format)", example = "2024-07-01T14:22:05Z")
        String pushedAt,

        @Schema(description = "Repository`s creation timestamp (ISO 8601 format)", example = "2024-07-01T14:22:05Z")
        String createdAt,

        @Schema(description = "Calculated popularity score", example = ".91")
        Double popularityScore)
{
}
