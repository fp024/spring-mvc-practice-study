package org.fp024.mvcpractice.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.fp024.mvcpractice.config.RootConfig;
import org.fp024.mvcpractice.config.ServletConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringJUnitWebConfig(classes = {RootConfig.class, ServletConfig.class})
class HelloControllerTests {

  private MockMvc mockMvc;

  @Autowired private WebApplicationContext context;

  @BeforeEach
  void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
  }

  @Test
  void testEx1() throws Exception {
    mockMvc
        .perform(get("/sample/ex1"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(view().name("sample/ex1"));
  }

  @Test
  void testEx2() throws Exception {
    mockMvc
        .perform(get("/sample/ex2"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(view().name("sample/success"));
  }
}
