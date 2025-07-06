package me.redcare.popularity.scorer.scorer;

import me.redcare.popularity.scorer.client.dto.GitHubRepoItem;
import me.redcare.popularity.scorer.scorer.impl.SimpleScorer;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleScorerTest {

    private final SimpleScorer underTest = new SimpleScorer();



    @Test
    void calculateScore_typicalValues() {
        GitHubRepoItem repo = mock(GitHubRepoItem.class);
        // when
        when(repo.forksCount()).thenReturn(300);
        when(repo.stargazersCount()).thenReturn(200);
        when(repo.updatedAt()).thenReturn(LocalDateTime.now().minusDays(5).format(DateTimeFormatter.ISO_DATE_TIME));


        double actual = underTest.calculateScore(repo);
        assertEquals(600, actual);
    }

    @Test
    void calculateScore_withRecencyBoost() {
        GitHubRepoItem repo = mock(GitHubRepoItem.class);
        // when
        when(repo.forksCount()).thenReturn(100);
        when(repo.stargazersCount()).thenReturn(100);
        when(repo.updatedAt()).thenReturn(LocalDateTime.now().minusDays(5).format(DateTimeFormatter.ISO_DATE_TIME));


        double actual = underTest.calculateScore(repo);
        assertEquals(300, actual);
    }

    @Test
    void calculateScore_withoutRecencyBoost() {
        GitHubRepoItem repo = mock(GitHubRepoItem.class);
        // when
        when(repo.forksCount()).thenReturn(100);
        when(repo.stargazersCount()).thenReturn(100);
        when(repo.updatedAt()).thenReturn(LocalDateTime.now().minusYears(1).minusDays(1).format(DateTimeFormatter.ISO_DATE_TIME));

        double actual = underTest.calculateScore(repo);
        assertEquals(200, actual);
    }
}