package com.woo.board.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.servlet.http.HttpSession;

@Configuration
@EnableJpaAuditing
@RequiredArgsConstructor
public class AuditConfig {

    private final HttpSession httpSession;

    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl(httpSession);
    }
}
