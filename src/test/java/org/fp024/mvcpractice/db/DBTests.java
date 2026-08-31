package org.fp024.mvcpractice.db;

import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.fp024.mvcpractice.config.RootConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(classes = RootConfig.class)
@Slf4j
class DBTests {

  @Autowired private DataSource dataSource;

  @Test
  void testConnection() {
    log.info("----------");
    log.info("{}", dataSource);
    log.info("----------");
  }
}
