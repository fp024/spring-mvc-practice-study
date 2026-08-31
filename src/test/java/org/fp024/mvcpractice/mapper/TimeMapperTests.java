package org.fp024.mvcpractice.mapper;

import lombok.extern.slf4j.Slf4j;
import org.fp024.mvcpractice.config.RootConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@Slf4j
@SpringJUnitConfig(classes = {RootConfig.class})
class TimeMapperTests {

  @Autowired private TimeMapper timeMapper;

  @Test
  void testGetTime() {
    log.info("----------");
    log.info(timeMapper.getTime());
  }

  @Test
  void testGetTime2() {
    log.info("----------");
    log.info(timeMapper.getTime2());
  }
}
