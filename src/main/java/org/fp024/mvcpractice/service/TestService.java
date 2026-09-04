package org.fp024.mvcpractice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fp024.mvcpractice.mapper.TestMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TestService {
  private final TestMapper testMapper;

  public void insertAll(String str) {

    int resultA = testMapper.insertA(str);

    log.info("insertA {}", resultA);

    int resultB = testMapper.insertB(str);

    log.info("insertB {}", resultB);
  }
}
