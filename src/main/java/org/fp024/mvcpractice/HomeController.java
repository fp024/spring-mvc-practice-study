package org.fp024.mvcpractice;

import java.time.LocalDateTime;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/** Handles requests for the application home page. */
@Controller
@Slf4j
public class HomeController {

  /** Simply selects the home view to render by returning its name. */
  @GetMapping(path = "/home")
  public String home(Locale locale, Model model) {
    log.info("Welcome home! The client locale is {}.", locale);

    model.addAttribute("serverTime", LocalDateTime.now());

    return "home_example/home";
  }
}
