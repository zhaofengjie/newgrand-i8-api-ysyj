package com.newgrand.secdev.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class JdbcTemplateConfig {
    @Bean
    JdbcTemplate jdbcTemplateOrcle(@Qualifier("dsOrcle") DataSource dsOrcle) {
        return new JdbcTemplate(dsOrcle);
    }
}