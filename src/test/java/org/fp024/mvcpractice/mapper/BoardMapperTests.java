package org.fp024.mvcpractice.mapper;

import java.util.List;
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

  @Test
  void testSelectOne() {
    Long bno = 2L;
    BoardDTO boardDTO = boardMapper.selectOne(bno);

    log.info("--------------------");
    log.info("boardDTO: {}", boardDTO);
  }

  @Test
  void testRemove() {
    Long bno = 2L;
    int removeCount = boardMapper.remove(bno);

    log.info("--------------------");
    log.info("removeCount: {}", removeCount);
  }

  @Test
  void testUpdate() {
    BoardDTO boardDTO =
        BoardDTO.builder() //
            .bno(2L)
            .title("Update Title")
            .content("Update Content")
            .delFlag(false)
            .build();

    int updateCount = boardMapper.update(boardDTO);

    log.info("--------------------");
    log.info("updateCount: {}", updateCount);
  }

  @Test
  void testList() {
    List<BoardDTO> dtoList = boardMapper.list();

    log.info("dtoList");
    log.info("{}", dtoList);

    log.info("--------------------");
    dtoList.forEach(boardDTO -> log.info("{}", boardDTO));
  }
}
