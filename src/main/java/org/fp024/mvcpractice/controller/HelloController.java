package org.fp024.mvcpractice.controller;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.fp024.mvcpractice.dto.SampleDTO;
import org.fp024.mvcpractice.service.HelloService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    helloService.hello1();
  }

  @GetMapping("/ex2")
  public String ex2() {
    log.info("/sample/ex2");
    helloService.hello2("Hong Gil Dong");
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

  @GetMapping("/ex4")
  public void ex4(
      @RequestParam(name = "n1", defaultValue = "1") int num,
      @RequestParam(name = "name") String name,
      Model model) {
    log.info("/sample/ex4");
    log.info("num : {}", num);
    log.info("name : {}", name);

    model.addAttribute("num", num);
    model.addAttribute("name", name);
  }

  @GetMapping("/ex5")
  public void ex5(SampleDTO dto, Model model) {
    log.info("/sample/ex5");
    log.info("{}", dto);

    model.addAttribute("dto", dto);
  }

  @GetMapping("/ex6")
  public void ex6(Model model) {
    model.addAttribute("name", "Hong Gil Dong");
    model.addAttribute("age", 16);
  }

  @GetMapping("/ex7")
  public String ex7(RedirectAttributes rttr) {
    rttr.addAttribute("name", "Hong");
    rttr.addFlashAttribute("age", 16);

    return "redirect:/sample/ex8";
  }

  @GetMapping("/ex8")
  public void ex8() {
    log.info("/sample/ex8");
  }
}
