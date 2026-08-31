package org.fp024.mvcpractice.controller;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.fp024.mvcpractice.service.HelloService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@ToString
@RequestMapping("/sample")
public class HelloController {
  private final HelloService helloService;

  @GetMapping("/ex1")
  public void ex1() {
    log.info("/sample/ex1");
  }
}
