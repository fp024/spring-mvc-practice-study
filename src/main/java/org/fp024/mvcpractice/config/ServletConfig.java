package org.fp024.mvcpractice.config;

import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

@Configuration
@ComponentScan(
    basePackages = "org.fp024.mvcpractice",
    useDefaultFilters = false,
    includeFilters = {
      @ComponentScan.Filter(
          type = FilterType.ANNOTATION,
          classes = {Controller.class})
    })
@EnableWebMvc
@RequiredArgsConstructor
public class ServletConfig implements WebMvcConfigurer {

  private final ApplicationContext applicationContext;

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry
        .addResourceHandler("/resources/**", "/favicon.ico")
        .addResourceLocations("classpath:/static/");

    registry
        .addResourceHandler("/webjars/**")
        .addResourceLocations("/webjars/")
        .resourceChain(false);
  }

  @Bean
  public SpringResourceTemplateResolver templateResolver() {
    SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
    templateResolver.setApplicationContext(applicationContext);
    templateResolver.setPrefix("classpath:/templates/");
    templateResolver.setSuffix(".html");
    templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
    templateResolver.setTemplateMode(TemplateMode.HTML);
    templateResolver.setCacheable(false);
    return templateResolver;
  }

  @Bean
  public SpringTemplateEngine templateEngine() {
    SpringTemplateEngine templateEngine = new SpringTemplateEngine();
    templateEngine.setTemplateResolver(templateResolver());
    templateEngine.setEnableSpringELCompiler(true);
    templateEngine.addDialect(new LayoutDialect());
    return templateEngine;
  }

  @Bean
  public ViewResolver thymeleafViewResolver() {
    ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
    viewResolver.setTemplateEngine(templateEngine());
    viewResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
    viewResolver.setOrder(1);
    return viewResolver;
  }
}
