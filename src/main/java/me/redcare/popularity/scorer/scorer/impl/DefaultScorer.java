package me.redcare.popularity.scorer.scorer.impl;

import me.redcare.popularity.scorer.client.dto.GitHubRepoItem;
import me.redcare.popularity.scorer.scorer.PopularityScorer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Default popularity scorer for GitHub repositories.
 *
 * <p>This implementation assigns a score between 0 and 1 based on normalized metrics:
 * <ul>
 *     <li>Fork count</li>
 *     <li>Star count</li>
 *     <li>Recency of last update</li>
 *     <li>Recency of last push</li>
 * </ul>
 *
 * <p>Normalization is capped using reasonable maximum values derived from
 * real-world GitHub data:
 * <ul>
 *     <li>Max Stars: 100,000 (based on mean top-starred repos)</li>
 *     <li>Max Forks: 35,000 (based on mean top-forked repos)</li>
 *     <li>Max Inactivity Days: 365 days (1 year)</li>
 * </ul>
 *
 * <p>Reference: <a href="https://github.com/EvanLi/Github-Ranking">GitHub Ranking Statistics</a>
 */
@Component
public class DefaultScorer implements PopularityScorer {

    private static final Integer MAX_STAR_COUNT = 100000;
    private static final Integer MAX_FORK_COUNT = 35000;
    private static final Integer MAX_INACTIVITY_DAYS = 365;
    private static final Integer MAX_INACTIVITY_PUSHES_DAYS = 365;


    /**
     * Calculates a normalized popularity score for a given repository.
     * The score ranges from 0.00 to 100.0, rounded to two decimal places.
     *
     * @param repo the GitHub repository to evaluate
     * @return popularity score between 0 and 100
     */
    @Override
    public double calculateScore(GitHubRepoItem repo) {

        double normalizedForksCounts = getNormalizedValue(repo.forksCount(), MAX_FORK_COUNT);
        double normalizedStarsCounts = getNormalizedValue(repo.stargazersCount(), MAX_STAR_COUNT);

        int numberOfDaysToUpdateCapped = calculateCappedNumberOfDaysTo(repo.updatedAt(), MAX_INACTIVITY_DAYS);
        double normalizedUpdatesDays = getNormalizedValue(MAX_INACTIVITY_DAYS - numberOfDaysToUpdateCapped, MAX_INACTIVITY_DAYS);

        int numberOfDaysToPushCapped = calculateCappedNumberOfDaysTo(repo.pushedAt(), MAX_INACTIVITY_PUSHES_DAYS);
        double normalizedPushDays = getNormalizedValue(MAX_INACTIVITY_PUSHES_DAYS - numberOfDaysToPushCapped, MAX_INACTIVITY_PUSHES_DAYS);

        return Math.round(  (.35 * normalizedForksCounts +
                .35 * normalizedStarsCounts +
                .1 * normalizedUpdatesDays +
                .2 * normalizedPushDays) * 100 ) / 100.0;
    }

    private Integer calculateCappedNumberOfDaysTo(String dateTimeIso, Integer cappedDays) {
        LocalDateTime date = LocalDateTime.parse(dateTimeIso, DateTimeFormatter.ISO_DATE_TIME);
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        return (int) Math.min(ChronoUnit.DAYS.between(date, now), cappedDays);
    }

    private double getNormalizedValue(Integer value, Integer max) {
        return Math.round( (Math.min(value, max) * 1.0 / max) * 100);
    }

    @Override
    public String getName() {
        return "default";
    }
}