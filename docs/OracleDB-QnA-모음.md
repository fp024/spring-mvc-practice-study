# OracleDB QnA 모음

> 기본 베이스로 사용하고 있는 교제가 MariaDB인데, OracleDB로 환경을 만들어놔서..😅
>
> 전환 과정에서의 질문/답변을 모아두자!



## OracleDB를 사용할 때, Java 타입 LocalDateTime, 테이블 컬럼 타입 TIMESTAMP일 때..  해당 컬럼의 초기화 값은 어떻게 할까?

> ✨ MariaDB 환경에서는 이 경우 NOW()를 사용했었다.

- Oracle `TIMESTAMP` ↔ Java `LocalDateTime`
  - `LOCALTIMESTAMP` 권장
- Oracle `TIMESTAMP WITH TIME ZONE`
  - `SYSTIMESTAMP` 사용 가능
  - Java는 `OffsetDateTime` 또는 `ZonedDateTime`이 더 적절
- Oracle `DATE`
  - `SYSDATE`

`SYSTIMESTAMP`는 `TIMESTAMP WITH TIME ZONE` 값을 반환하므로, 현재 DTO처럼 시간대 정보가 없는 `LocalDateTime`을 사용한다면 다음 조합이 가장 깔끔합니다.

`UPDATEDATE = LOCALTIMESTAMP`

`REGDATE`도 DB 기본값을 설정한다면 동일하게 `LOCALTIMESTAMP`를 사용하면 됩니다. MyBatis는 일반적으로 Oracle `TIMESTAMP`와 Java `LocalDateTime`을 자동 매핑합니다.