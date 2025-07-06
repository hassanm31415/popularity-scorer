package me.redcare.popularity.scorer.scorer;

import me.redcare.popularity.scorer.client.dto.GitHubRepoItem;
import me.redcare.popularity.scorer.scorer.impl.DefaultScorer;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultScorerTest {

    private final DefaultScorer underTest = new DefaultScorer();

    @Test
    void calculateScore_typicalValue() {
        // given
        GitHubRepoItem repo = mock(GitHubRepoItem.class);
        Integer forksCount = 10000;
        Integer startsCount = 3500;
        String recentUpdateDate = LocalDateTime.now().minusDays(5).format(DateTimeFormatter.ISO_DATE_TIME);
        String recentCommitDate = LocalDateTime.now().minusDays(50).format(DateTimeFormatter.ISO_DATE_TIME);

        // when
        when(repo.forksCount()).thenReturn(forksCount);
        when(repo.stargazersCount()).thenReturn(startsCount);
        when(repo.updatedAt()).thenReturn(recentUpdateDate);
        when(repo.pushedAt()).thenReturn(recentCommitDate);

        double actual = underTest.calculateScore(repo);
        assertTrue(actual > 0 && actual < 100, "Score should be within expected range");
        assertEquals(38.85, actual);
    }

    @Test
    void calculateScore_upperLimit() {
        // given
        GitHubRepoItem repo = mock(GitHubRepoItem.class);
        Integer forksCount = 35000;
        Integer startsCount = 100000;
        String recentUpdateDate = LocalDateTime.now().minusDays(0).format(DateTimeFormatter.ISO_DATE_TIME);
        String recentCommitDate = LocalDateTime.now().minusDays(0).format(DateTimeFormatter.ISO_DATE_TIME);

        // when
        when(repo.forksCount()).thenReturn(forksCount);
        when(repo.stargazersCount()).thenReturn(startsCount);
        when(repo.updatedAt()).thenReturn(recentUpdateDate);
        when(repo.pushedAt()).thenReturn(recentCommitDate);

        double actual = underTest.calculateScore(repo);
        assertEquals(100, actual, "Score should be 100");
    }

    @Test
    void calculateScore_aboveLimit() {
        // given
        GitHubRepoItem repo = mock(GitHubRepoItem.class);
        Integer forksCount = 350000;
        Integer startsCount = 1000000;
        String recentUpdateDate = LocalDateTime.now().minusDays(0).format(DateTimeFormatter.ISO_DATE_TIME);
        String recentCommitDate = LocalDateTime.now().minusDays(0).format(DateTimeFormatter.ISO_DATE_TIME);

        // when
        when(repo.forksCount()).thenReturn(forksCount);
        when(repo.stargazersCount()).thenReturn(startsCount);
        when(repo.updatedAt()).thenReturn(recentUpdateDate);
        when(repo.pushedAt()).thenReturn(recentCommitDate);

        double actual = underTest.calculateScore(repo);
        assertEquals(100, actual, "Score should be 100");
    }


    @Test
    void calculateScore_lowerLimit() {
        // given
        GitHubRepoItem repo = mock(GitHubRepoItem.class);
        Integer forksCount = 0;
        Integer startsCount = 0;
        String recentUpdateDate = LocalDateTime.now().minusDays(365).format(DateTimeFormatter.ISO_DATE_TIME);
        String recentCommitDate = LocalDateTime.now().minusDays(365).format(DateTimeFormatter.ISO_DATE_TIME);

        // when
        when(repo.forksCount()).thenReturn(forksCount);
        when(repo.stargazersCount()).thenReturn(startsCount);
        when(repo.updatedAt()).thenReturn(recentUpdateDate);
        when(repo.pushedAt()).thenReturn(recentCommitDate);

        double actual = underTest.calculateScore(repo);
        assertEquals(0, actual, "Score should be 0");
    }
}