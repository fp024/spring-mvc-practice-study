package org.fp024.mvcpractice.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.fp024.mvcpractice.dto.BoardDTO;

@Mapper
public interface BoardMapper {
  int insert(BoardDTO dto);

  BoardDTO selectOne(Long bno);

  int remove(Long bno);

  int update(BoardDTO dto);

  List<BoardDTO> list();
}
