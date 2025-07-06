package me.redcare.popularity.scorer.controller.dto;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.web.bind.annotation.RequestParam;

public record
RepositoryScoringRequest(
        @Parameter(description = "Search query for GitHub repositories (cannot be blank)", required = true)
        @RequestParam @NotBlank
        String query,

        @Parameter(description = "Optional filter by repository language", required = false)
        @RequestParam(required = false)
        String language,

        @Parameter(description = "Filter by creation date (YYYY-MM-DD)", example = "2024-01-01")
        @RequestParam(required = false)
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Date must be in YYYY-MM-DD format")
        String createdAfter,

        @Parameter(description = "Scoring strategy to apply (default or simple)")
        @RequestParam(defaultValue = "default")
        String scorer,

        @Parameter(description = "Page number for results (1-based index)")
        @RequestParam(defaultValue = "1")
        Integer page,

        @Parameter(description = "Number of results per page")
        @RequestParam(defaultValue = "20")
        Integer perPage
) {
    public RepositoryScoringRequest {
        scorer = (scorer == null || scorer.isBlank()) ? "default" : scorer;
        page = (page == null || page < 1) ? 1 : Math.min(page, 100);
        perPage = (perPage == null || perPage < 1) ? 20 : perPage;
    }

}
