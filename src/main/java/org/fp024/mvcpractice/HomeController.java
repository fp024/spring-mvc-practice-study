package org.fp024.mvcpractice;

import java.text.DateFormat;
import java.util.Date;
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
  @GetMapping(path = "/")
  public String home(Locale locale, Model model) {
    log.info("Welcome home! The client locale is {}.", locale);

    Date date = new Date();
    DateFormat dateFormat =
        DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

    String formattedDate = dateFormat.format(date);

    model.addAttribute("serverTime", formattedDate);

    return "home";
  }
}
