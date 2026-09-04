package org.fp024.mvcpractice.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TestMapper {

  @Insert(
      """
      INSERT INTO TBL_TEST_A (COL1)
      VALUES (#{str})
      """)
  int insertA(@Param("str") String str);

  @Insert(
      """
      INSERT INTO TBL_TEST_B (COL2)
      VALUES (#{str})
      """)
  int insertB(@Param("str") String str);
}
