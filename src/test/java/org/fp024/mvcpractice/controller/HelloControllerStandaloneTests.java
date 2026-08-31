package org.fp024.mvcpractice.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.fp024.mvcpractice.dto.SampleDTO;
import org.fp024.mvcpractice.service.HelloService;
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
class HelloControllerStandaloneTests {
  private MockMvc mockMvc;

  @Mock private HelloService helloService;

  @InjectMocks private HelloController helloController;

  @BeforeEach
  void setUp() {
    mockMvc =
        MockMvcBuilders.standaloneSetup(helloController)
            .setViewResolvers(new InternalResourceViewResolver("/WEB-INF/views/", ".html"))
            .build();
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

  @Test
  void testEx3() throws Exception {
    mockMvc
        .perform(get("/sample/ex3"))
        .andDo(print())
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/sample/ex3re"));
  }

  @Test
  void testEx4() throws Exception {
    mockMvc
        .perform(
            get("/sample/ex4") //
                .param("n1", "100")
                .param("name", "iLoveAli"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(view().name("sample/ex4"))
        .andExpect(model().attribute("num", 100))
        .andExpect(model().attribute("name", "iLoveAli"));
  }

  @Test
  void testEx5() throws Exception {
    mockMvc
        .perform(
            get("/sample/ex5") //
                .param("num", "200")
                .param("name", "Wago"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(view().name("sample/ex5"))
        .andExpect(model().attribute("dto", new SampleDTO(200, "Wago")));
  }

  @Test
  void testEx6() throws Exception {
    mockMvc
        .perform(get("/sample/ex6"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(view().name("sample/ex6"))
        .andExpect(model().attribute("name", "Hong Gil Dong"))
        .andExpect(model().attribute("age", 16));
  }

  @Test
  void testEx7() throws Exception {
    mockMvc
        .perform(get("/sample/ex7"))
        .andDo(print())
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/sample/ex8?name=Hong"))
        .andExpect(model().attribute("name", "Hong"))
        .andExpect(flash().attribute("age", 16));
  }
}
