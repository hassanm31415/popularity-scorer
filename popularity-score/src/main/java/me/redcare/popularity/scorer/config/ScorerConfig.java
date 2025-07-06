package me.redcare.popularity.scorer.config;

import me.redcare.popularity.scorer.scorer.PopularityScorer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class ScorerConfig {

    @Bean
    public Map<String, PopularityScorer> scorerMap(List<PopularityScorer> scorers) {
        return scorers.stream()
                .collect(Collectors.toUnmodifiableMap(PopularityScorer::getName, Function.identity()));
    }
}
