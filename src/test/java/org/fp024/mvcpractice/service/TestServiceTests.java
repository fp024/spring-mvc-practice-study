package org.fp024.mvcpractice.service;

import lombok.extern.slf4j.Slf4j;
import org.fp024.mvcpractice.config.RootConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@Slf4j
@SpringJUnitConfig(classes = {RootConfig.class})
class TestServiceTests {

  @Autowired private TestService testService;

  @Test
  void testInsertAll() {
    String str =
        "Alice was Beginning to get very tired of sitting by her sister on the bank, and of having"
            + " nothing todo";

    testService.insertAll(str);
  }
}
