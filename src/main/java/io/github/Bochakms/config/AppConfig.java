package io.github.Bochakms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class AppConfig {
    private final Environment env;

    public AppConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public String fromEmail() {
        return env.getProperty("app.email.from");
    }

    @Bean
    public String siteName() {
        return env.getProperty("app.email.site-name");
    }
}