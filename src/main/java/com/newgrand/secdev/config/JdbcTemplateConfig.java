package com.newgrand.secdev.config;

import com.newgrand.secdev.helper.IJdbcTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class JdbcTemplateConfig {
    @Bean
    IJdbcTemplate jdbcTemplateOrcle(@Qualifier("dsOrcle") DataSource dsOrcle) {
        return new IJdbcTemplate(dsOrcle);
    }
}