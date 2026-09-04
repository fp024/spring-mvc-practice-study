package org.fp024.mvcpractice.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class LogAdvice {

  @Before("execution(* org.fp024.mvcpractice.service.*.*(..))")
  public void logParams() {
    log.info("----------");
    log.info("logParams");
    log.info("----------");
  }
}
