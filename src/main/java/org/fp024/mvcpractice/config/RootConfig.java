package org.fp024.mvcpractice.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;

@Configuration
@ComponentScan(
    basePackages = "org.fp024.mvcpractice",
    excludeFilters = {
      @ComponentScan.Filter(
          type = FilterType.ANNOTATION,
          classes = {Controller.class}),
      @ComponentScan.Filter(
          type = FilterType.ASSIGNABLE_TYPE,
          classes = {ServletConfig.class})
    })
public class RootConfig {
  /*
   Root Context: defines shared resources visible to all other web components
  */
}
