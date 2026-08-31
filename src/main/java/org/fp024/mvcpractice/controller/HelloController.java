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

  @GetMapping("/ex2")
  public String ex2() {
    return "sample/success";
  }

  @GetMapping("/ex3")
  public String ex3() {

    log.info("/sample/ex3");

    return "redirect:/sample/ex3re";
  }

  @GetMapping("/ex3re")
  public String ex3Re() {

    log.info("/sample/ex3Re");

    return "sample/ex3Result";
  }
}
