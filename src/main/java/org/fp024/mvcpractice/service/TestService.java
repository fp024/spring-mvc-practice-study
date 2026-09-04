package org.fp024.mvcpractice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fp024.mvcpractice.mapper.TestMapper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestService {
  private final TestMapper testMapper;

  public void insertAll(String str) {
    int resultA = testMapper.insertA(str);

    log.info("insertA {}", resultA);

    int resultB = testMapper.insertB(str);

    log.info("insertB {}", resultB);
  }
}
