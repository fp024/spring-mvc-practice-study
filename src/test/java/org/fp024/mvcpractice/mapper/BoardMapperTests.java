package org.fp024.mvcpractice.mapper;

import lombok.extern.slf4j.Slf4j;
import org.fp024.mvcpractice.config.RootConfig;
import org.fp024.mvcpractice.dto.BoardDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@Slf4j
@SpringJUnitConfig(classes = {RootConfig.class})
class BoardMapperTests {

  @Autowired private BoardMapper boardMapper;

  @Test
  void testInsert() {
    BoardDTO boardDTO =
        BoardDTO.builder() //
            .title("title")
            .content("content")
            .writer("user00")
            .build();

    int insertCount = boardMapper.insert(boardDTO);

    log.info("--------------------");
    log.info("insertCount: {}", insertCount);

    log.info("====================");
    log.info("BNO: {}", boardDTO.getBno());
  }
}
