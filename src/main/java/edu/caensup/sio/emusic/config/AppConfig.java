package edu.caensup.sio.emusic.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import edu.caensup.sio.emusic.repositories.CustomRepositoryImpl;

@Configuration
@ComponentScan("io.github.jeemv.springboot.vuejs")
@EnableJpaRepositories(basePackages = "edu.caensup.sio.emusic.repositories",
    repositoryBaseClass = CustomRepositoryImpl.class)
public class AppConfig {
}
