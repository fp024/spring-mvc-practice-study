package org.fp024.mvcpractice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class HelloController {

  public HelloController() {
    log.info("helloController constructor");
  }
}
