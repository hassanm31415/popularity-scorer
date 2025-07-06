package me.redcare.popularity.scorer.mapper;

import me.redcare.popularity.scorer.client.dto.GitHubRepoItem;
import me.redcare.popularity.scorer.controller.dto.ScoredRepositoryDTO;
import org.springframework.stereotype.Component;

@Component
public class RepositoryMapper {

    public ScoredRepositoryDTO toDTO(GitHubRepoItem item, double score) {
        return new ScoredRepositoryDTO(
                item.id(),
                item.name(),
                item.htmlUrl(),
                item.language(),
                item.stargazersCount(),
                item.forksCount(),
                item.updatedAt(),
                item.pushedAt(),
                item.createdAt(),
                score
        );
    }
}