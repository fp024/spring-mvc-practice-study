package org.fp024.mvcpractice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fp024.mvcpractice.dto.BoardDTO;
import org.fp024.mvcpractice.service.BoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/board")
@Slf4j
@RequiredArgsConstructor
public class BoardController {

  private final BoardService boardService;

  @GetMapping("/list")
  public void list(Model model) {
    log.info("------------------------------");
    log.info("board list");

    model.addAttribute("list", boardService.getList());
  }

  @GetMapping("/register")
  public void register() {
    log.info("------------------------------");
    log.info("board register");
  }

  @PostMapping("/register")
  public String registerPost(BoardDTO dto, RedirectAttributes rttr) {
    log.info("------------------------------");
    log.info("board register post");

    Long bno = boardService.register(dto);

    rttr.addFlashAttribute("result", bno);

    return "redirect:/board/list";
  }

  @GetMapping("/read/{bno}")
  public String read(@PathVariable("bno") Long bno, Model model) {
    log.info("------------------------------");
    log.info("board read");

    BoardDTO dto = boardService.read(bno);

    model.addAttribute("board", dto);

    return "board/read";
  }

  @GetMapping("/modify/{bno}")
  public String modifyGET(@PathVariable("bno") Long bno) {
    log.info("------------------------------");
    log.info("board modify get");

    return "board/modify";
  }

  @PostMapping("/modify")
  public String modifyPOST() {
    log.info("------------------------------");
    log.info("board modify post");
    return "redirect:/board/read/123";
  }

  @PostMapping("/remove")
  public String remove() {
    log.info("------------------------------");
    log.info("board remove post");

    return "redirect:/board/list";
  }
}
