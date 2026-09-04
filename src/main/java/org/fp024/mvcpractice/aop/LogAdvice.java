package org.fp024.mvcpractice.aop;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class LogAdvice {

  @Before("execution(* org.fp024.mvcpractice.service.*.*(..))")
  public void logParams(JoinPoint jp) {
    log.info("----------");
    log.info("logParams");

    Object[] params = jp.getArgs();

    log.info(Arrays.toString(params));

    Object target = jp.getTarget();

    log.info("{}", target);

    log.info("----------");
  }

  @Around("execution(* org.fp024.mvcpractice.service.*.*(..))")
  public Object logTime(ProceedingJoinPoint pjp) throws Throwable {
    log.info("----------");
    log.info("logTimes");

    long start = System.currentTimeMillis();

    Object result = pjp.proceed();

    long end = System.currentTimeMillis();

    log.info("----------");
    log.info("TIME: {}", (end - start));

    return result;
  }
}
