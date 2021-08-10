package com.drinking.game.backend.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("com.drinking.game.backend.repository")
public class JpaConfiguration {
}
