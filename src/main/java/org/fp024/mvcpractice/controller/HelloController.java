package org.fp024.mvcpractice.controller;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.fp024.mvcpractice.service.HelloService;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@ToString
public class HelloController {
  private final HelloService helloService;
}
