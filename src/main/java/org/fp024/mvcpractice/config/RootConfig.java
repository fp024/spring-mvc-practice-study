package org.fp024.mvcpractice.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;

@Configuration
@PropertySource({"classpath:database.properties"})
@ComponentScan(
    basePackages = {"org.fp024.mvcpractice"},
    excludeFilters = {
      @ComponentScan.Filter(
          type = FilterType.ANNOTATION,
          classes = {Controller.class}),
      @ComponentScan.Filter(
          type = FilterType.ASSIGNABLE_TYPE,
          classes = {ServletConfig.class})
    })
public class RootConfig {

  @Bean(destroyMethod = "close")
  HikariDataSource dataSource(HikariConfig hikariConfig) {
    return new HikariDataSource(hikariConfig);
  }

  @Bean
  HikariConfig hikariConfig(
      @Value("${jdbc.driver}") String driverClassName,
      @Value("${jdbc.url}") String url,
      @Value("${jdbc.username}") String userName,
      @Value("${jdbc.password}") String password) {
    HikariConfig hikariConfig = new HikariConfig();
    hikariConfig.setDriverClassName(driverClassName);
    hikariConfig.setJdbcUrl(url);
    hikariConfig.setUsername(userName);
    hikariConfig.setPassword(password);

    hikariConfig.setConnectionTimeout(30_000L);
    hikariConfig.setMinimumIdle(2);

    return hikariConfig;
  }
}
