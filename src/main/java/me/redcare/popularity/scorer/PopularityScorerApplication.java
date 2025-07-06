package me.redcare.popularity.scorer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class PopularityScorerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PopularityScorerApplication.class, args);
	}

}
