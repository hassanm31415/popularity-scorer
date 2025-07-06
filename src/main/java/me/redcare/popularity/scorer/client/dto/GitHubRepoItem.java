package me.redcare.popularity.scorer.client.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

public record GitHubRepoItem(
        Integer id,
        String name,
        @JsonProperty("html_url") String htmlUrl,
        String language,
        @JsonProperty("stargazers_count") Integer stargazersCount,
        @JsonProperty("forks_count") Integer forksCount,
        @JsonProperty("updated_at") String updatedAt,
        @JsonProperty("pushed_at") String pushedAt,
        @JsonProperty("created_at") String createdAt,
        @JsonProperty("score") Double gitHubScore
) {
}