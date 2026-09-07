package org.fp024.mvcpractice.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.fp024.mvcpractice.service.BoardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@ExtendWith(MockitoExtension.class)
class BoardControllerTests {
  private MockMvc mockMvc;

  @Mock private BoardService boardService;

  @InjectMocks private BoardController boardController;

  @BeforeEach
  void setUp() {
    mockMvc =
        MockMvcBuilders.standaloneSetup(boardController)
            .setViewResolvers(new InternalResourceViewResolver("/WEB-INF/views/", ".html"))
            .build();
  }

  @Test
  void testList() throws Exception {
    mockMvc
        .perform(get("/board/list"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(view().name("board/list"));
  }

  @Test
  void testModifyGET() throws Exception {
    mockMvc
        .perform(get("/board/modify/123"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(view().name("board/modify"));
  }

  @Test
  void testModifyPOST() throws Exception {
    mockMvc
        .perform(post("/board/modify"))
        .andDo(print())
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/board/read/123"));
  }

  @Test
  void testRead() throws Exception {
    mockMvc
        .perform(get("/board/read/123"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(view().name("board/read"));
  }

  @Test
  void testRegister() throws Exception {
    mockMvc
        .perform(get("/board/register"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(view().name("board/register"));
  }

  @Test
  void testRegisterPost() throws Exception {
    mockMvc
        .perform(post("/board/register"))
        .andDo(print())
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/board/list"));
  }

  @Test
  void testRemove() throws Exception {
    mockMvc
        .perform(post("/board/remove"))
        .andDo(print())
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/board/list"));
  }
}
