package org.fp024.mvcpractice.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

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
@MapperScan(basePackages = {"org.fp024.mvcpractice.mapper"})
@EnableTransactionManagement
@EnableAspectJAutoProxy
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

  @Bean
  SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
    SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
    sqlSessionFactoryBean.setDataSource(dataSource);
    sqlSessionFactoryBean.setMapperLocations(
        new PathMatchingResourcePatternResolver() //
            .getResources("classpath:mappers/**/*.xml"));
    return sqlSessionFactoryBean.getObject();
  }

  @Bean
  PlatformTransactionManager transactionManager(DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
  }
}
