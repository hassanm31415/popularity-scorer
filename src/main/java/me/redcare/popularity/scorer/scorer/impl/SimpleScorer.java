package me.redcare.popularity.scorer.scorer.impl;

import me.redcare.popularity.scorer.client.dto.GitHubRepoItem;
import me.redcare.popularity.scorer.scorer.PopularityScorer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Simple popularity scorer based on raw repository metrics.
 *
 * <p>Scoring Formula:</p>
 * <pre>
 * score = stars + forks + recentBoost
 * </pre>
 *
 * Where:
 * - `stars` = Total stargazers count
 * - `forks` = Total forks count
 * - `recentBoost` = Additional 100 points if repository was updated within a year
 *
 * <p>This scorer prioritizes repositories with raw popularity (stars and forks),
 * with a simple boost for recent activity.
 */
@Component
public class SimpleScorer implements PopularityScorer {

    @Override
    public double calculateScore(GitHubRepoItem repo) {
        long recentBoost = isUpdatedWithinOneYear(repo.updatedAt()) ? 100 : 0;
        return repo.stargazersCount() + repo.forksCount() + recentBoost;
    }

    private boolean isUpdatedWithinOneYear(String dateTimeIso) {
        return LocalDateTime.parse(dateTimeIso, DateTimeFormatter.ISO_DATE_TIME)
                .isAfter(LocalDateTime.now(ZoneOffset.UTC).minusYears(1));
    }

    @Override
    public String getName() {
        return "simple";
    }
}