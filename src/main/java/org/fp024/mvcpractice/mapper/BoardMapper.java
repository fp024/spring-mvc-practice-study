package org.fp024.mvcpractice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.fp024.mvcpractice.dto.BoardDTO;

@Mapper
public interface BoardMapper {
  int insert(BoardDTO dto);
}
