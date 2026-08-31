package org.fp024.mvcpractice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TimeMapper {

  @Select("SELECT SYSDATE FROM DUAL")
  String getTime();

  String getTime2();
}
