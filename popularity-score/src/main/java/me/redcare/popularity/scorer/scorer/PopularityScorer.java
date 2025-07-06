package me.redcare.popularity.scorer.scorer;

import me.redcare.popularity.scorer.client.dto.GitHubRepoItem;

/**
 * Strategy interface for calculating a popularity score for GitHub repositories.
 *
 * <p>This interface can be implemented to define custom scoring mechanisms
 * based on various repository attributes such as stars, forks, watchers,
 * contributor activity, etc.</p>
 */
public interface PopularityScorer {

    /**
     * Calculates a numerical score representing the popularity of a given
     * GitHub repository.
     *
     * @param repo the GitHub repository item to score
     * @return a double representing the calculated popularity score
     */
    double calculateScore(GitHubRepoItem repo);

    /**
     * Returns the name or identifier of the scoring strategy.
     * This can be used for logging, debugging, or displaying
     * the currently used strategy in a user interface.
     *
     * @return the name of the popularity scoring strategy
     */

    String getName();
}
