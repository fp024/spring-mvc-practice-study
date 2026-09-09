package org.fp024.mvcpractice.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fp024.mvcpractice.dto.BoardDTO;
import org.fp024.mvcpractice.mapper.BoardMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BoardService {

  private final BoardMapper boardMapper;

  public List<BoardDTO> getList() {
    return boardMapper.list();
  }

  public Long register(BoardDTO dto) {
    int insertCount = boardMapper.insert(dto);

    log.info("insertCount: {}", insertCount);

    return dto.getBno();
  }

  public BoardDTO read(Long bno) {
    BoardDTO boardDTO = boardMapper.selectOne(bno);

    return boardDTO;
  }
}
